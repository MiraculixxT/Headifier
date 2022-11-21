package de.miraculixx.headifier.utils.gui.data

import de.miraculixx.headifier.utils.messages.*
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player

interface CustomInventory : MenuProvider {
    val inventory: Container
    val viewers: MutableList<Player>
    val id: String

    /**
     * Get the final inventory object for further operations.
     * @return Crafted storage GUI
     */
    fun get(): Container {
        return inventory
    }

    /**
     * Close the GUI for all viewers
     */
    fun close() {
        viewers.forEach {
            inventory.stopOpen(it)
        }
        viewers.clear()
    }

    /**
     * Close the GUI for a specific player
     * @param player The targeting player
     * @return False if the player is not a viewer
     */
    fun close(player: Player): Boolean {
        return if (viewers.contains(player)) {
            inventory.stopOpen(player)
            true
        } else false
    }

    /**
     * Open this GUI for a player. All players mentioned in the builder phase are automatically forced to open the GUI
     * @param player Target Player
     */
    fun open(player: Player) {
        inventory.startOpen(player)
    }

    /**
     * Open this GUI for multiple players. All players mentioned in the builder phase are automatically forced to open the GUI
     * @param players Target Player collection
     */
    fun open(players: Collection<Player>) {
        players.forEach {
            inventory.startOpen(it)
            if (debug) consoleAudience?.sendMessage(prefix + cmp("Open GUI '$id' to ${it.scoreboardName}"))
        }
    }
}