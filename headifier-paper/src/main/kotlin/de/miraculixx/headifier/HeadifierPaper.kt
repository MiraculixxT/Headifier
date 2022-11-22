package de.miraculixx.headifier

import de.miraculixx.headifier.commands.HeadifierCommand
import de.miraculixx.headifier.utils.entities.LateInitLoading
import de.miraculixx.headifier.utils.gui.CustomBuilder
import de.miraculixx.headifier.utils.gui.data.InventoryManager
import de.miraculixx.headifier.utils.gui.item.itemStack
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.console
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.sync
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.event.player.PlayerToggleSneakEvent

class HeadifierPaper: KSpigot() {
    companion object {
        lateinit var INSTANCE: KSpigot
    }

    override fun startup() {
        INSTANCE = this

        debug = true
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

        // ONLY FOR TESTING!!! Commands consume all errors what is... uncomfortable
        listen<PlayerToggleSneakEvent> {
            if (it.isSneaking) return@listen
            println(1)
            /*InventoryManager.inventoryBuilder("TEST") {
                this.player = (it.player as CraftPlayer).handle
                println("size")
                size = 1
                println("title")
                title = cmp("Activate/Deactivate Heads", cHighlight)
                println("content")
                content = mapOf(itemStack(Items.STONE, 1) {} to 4)
                println("finish")
            }
             */
            CustomBuilder(mapOf(itemStack(Items.STONE, 1) {} to 4), cmp("Activate/Deactivate Heads", cHighlight), "TEST", listOf((it.player as CraftPlayer).handle), 1)
            println(2)
        }
    }

    override fun shutdown() {

    }
}

val PluginManager by lazy { HeadifierPaper.INSTANCE }