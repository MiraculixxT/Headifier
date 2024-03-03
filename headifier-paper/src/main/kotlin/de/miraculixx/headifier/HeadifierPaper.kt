package de.miraculixx.headifier

import de.miraculixx.headifier.commands.HeadifierCommand
import de.miraculixx.headifier.utils.config.Config
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.*
import de.miraculixx.kpaper.extensions.console
import de.miraculixx.kpaper.main.KPaper
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeadifierPaper : KPaper() {
    companion object {
        lateinit var INSTANCE: KPaper
        lateinit var settings: Config
    }

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(false))
    }

    override fun startup() {
        INSTANCE = this
        CommandAPI.onEnable()

        debug = true
        consoleAudience = console
        settings = Config("settings")

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
            console.sendMessage(prefix + cmp("Successfully cached ${headCache.getHeads().size} block heads!"))
            HeadifierCommand()
        }
    }

    override fun shutdown() {
        CommandAPI.onDisable()
    }
}

val PluginManager by lazy { HeadifierPaper.INSTANCE }