package de.miraculixx.headifier

import de.miraculixx.headifier.utils.messages.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.io.File
import java.net.URL
import java.util.*

class HeadCache(private val configFolder: File) {
    private var heads = emptyList<Head>()
    private var materialRegex = Regex("-|_")
    private val base64Decoder = Base64.getDecoder()
    var ready = false

    fun getHeads(): List<Head> {
        return heads
    }

    fun getActivatedHeads(material: String): List<Head> {
        return getHeads(material).filter { it.activated }
    }

    fun getHeads(material: String): List<Head> {
        val key = material.replace(materialRegex, " ")
        return heads.filter { head -> head.name.equals(key, true) }
    }

    private fun filterResponse(response: String, status: Map<String, Boolean>): List<Head> {
        val regex = Regex("\\[.*.]")
        return if (response.matches(regex)) {
            json.decodeFromString<List<Head>>(response).filter {
                it.tags.contains("Vanilla Block")
            }.map {
                it.name = it.name.replace(Regex(".\\(.*\\).*"), "")
                it.activated = status[it.uuid] ?: true
                val decodedValue = String(base64Decoder.decode(it.value))
                it.url = decodedValue.removeSuffix("\"}}}").removePrefix("{\"textures\":{\"SKIN\":{\"url\":\"")
                it
            }
        } else {
            consoleAudience?.sendMessage(prefix + cmp("Failed to load block set! https://minecraft-heads.com returned invalid data!", cError))
            emptyList()
        }
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val responseBlocks = client.get("${mhAPI}cat=blocks&tags=true")
            val responseDecoration = client.get("${mhAPI}cat=decoration&tags=true")
            val responsePlants = client.get("${mhAPI}cat=plants&tags=true")

            if (!configFolder.exists()) {
                if (debug) consoleAudience?.sendMessage(prefix + cmp("Creating config folder: ${configFolder.path}"))
                configFolder.mkdirs()
            }
            val config = File("${configFolder.path}/head-status.json")
            if (!config.exists()) {
                withContext(Dispatchers.IO) {
                    if (debug) consoleAudience?.sendMessage(prefix + cmp("Creating config file: ${config.path}"))
                    config.createNewFile()
                    config.writeText("{}")
                }
            }

            val status = json.decodeFromString<Map<String, Boolean>>(config.readText())

            heads = buildList {
                addAll(filterResponse(responseBlocks.body(), status))
                addAll(filterResponse(responseDecoration.body(), status))
                addAll(filterResponse(responsePlants.body(), status))
            }

            ready = true
        }
    }

    /**
     * @param name Display name
     * @param value Encoded Base64 representation
     * @param uuid Fixed owner UUID for stacking
     * @param tags Head tags
     * @param activated Whether this head should be in the loot table
     * @param url Direct url to the skin
     */
    @Serializable
    data class Head(
        var name: String,
        val value: String,
        val uuid: String,
        val tags: String,
        var activated: Boolean = true,
        var url: String? = null
        )
}