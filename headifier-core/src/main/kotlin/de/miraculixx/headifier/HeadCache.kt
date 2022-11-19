package de.miraculixx.headifier

import de.miraculixx.headifier.utils.messages.cError
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.headifier.utils.messages.plus
import de.miraculixx.headifier.utils.messages.prefix
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.kyori.adventure.audience.Audience

class HeadCache(private val consoleAudience: Audience) {
    private var heads = emptyList<Head>()
    var success = true

    fun getHeads(): List<Head> {
        return heads
    }

    fun getHeads(material: String): List<Head> {
        return heads.filter { head -> head.name.equals(material, true) }
    }

    private fun filterResponse(response: String): List<Head> {
        val regex = Regex("\\{.*.}")
        return if (response.matches(regex)) {
            Json.decodeFromString<List<Head>>(response).filter {
                it.tags.contains("Vanilla Block")
            }.map {
                it.name = it.name.replace(Regex(".\\(.*\\).*"), "")
                it
            }
        } else {
            consoleAudience.sendMessage(prefix + cmp("Failed to load block set! https://minecraft-heads.com returned invalid data!", cError))
            success = false
            emptyList()
        }
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val responseBlocks = client.get("https://minecraft-heads.com/scripts/api.php?cat=blocks&tags=true")
            val responseDecoration = client.get("https://minecraft-heads.com/scripts/api.php?cat=decoration&tags=true")
            val responsePlants = client.get("https://minecraft-heads.com/scripts/api.php?cat=plants&tags=true")

            heads = buildList {
                addAll(filterResponse(responseBlocks.body()))
                addAll(filterResponse(responseDecoration.body()))
                addAll(filterResponse(responsePlants.body()))
            }
        }
    }

    @Serializable
    data class Head(var name: String, val value: String, val tags: String)
}