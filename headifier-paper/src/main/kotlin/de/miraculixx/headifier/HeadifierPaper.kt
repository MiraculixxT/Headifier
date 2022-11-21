package de.miraculixx.headifier

import de.miraculixx.headifier.commands.HeadifierCommand
import de.miraculixx.headifier.utils.entities.LateInitLoading
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.extensions.console
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.sync

class HeadifierPaper: KSpigot() {
    companion object {
        lateinit var INSTANCE: KSpigot
    }

    override fun startup() {
        INSTANCE = this

        consoleAudience = console
        val lateInitData = listOf(HeadifierCommand())

        headCache
        CoroutineScope(Dispatchers.Default).launch {
            var abort = 0
            while (!headCache.ready) {
                if (abort >= 100) {
                    console.sendMessage(prefix + cmp("Abort boot up! https://minecraft-heads.com is not responding for 10 seconds!", cError))
                    break
                }
                delay(100)
                abort++
            }

            if (headCache.ready) sync { boot(lateInitData) }
        }
    }

    private fun boot(lateInits: List<LateInitLoading>) {
        lateInits.forEach { it.loadData() }
    }

    override fun shutdown() {

    }
}

val PluginManager by lazy { HeadifierPaper.INSTANCE }