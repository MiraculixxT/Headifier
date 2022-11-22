package de.miraculixx.headifier.utils.gui.data

import de.miraculixx.headifier.utils.messages.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player

abstract class CustomInventory(size: Int) : SimpleContainer(size), MenuProvider {
    abstract val viewers: MutableList<Player>
    abstract val id: String

    /**
     * Get the final inventory object for further operations.
     * @return Crafted storage GUI
     */
    fun get(): Container {
        return this
    }

    /**
     * Close the GUI for all viewers
     */
    fun close() {
        viewers.forEach {
            stopOpen(it)
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
            stopOpen(player)
            true
        } else false
    }

    /**
     * Open this GUI for a player. All players mentioned in the builder phase are automatically forced to open the GUI
     * @param player Target Player
     */
    fun open(player: Player) {
        if (debug) consoleAudience?.sendMessage(prefix + cmp("Open GUI '$id' to ${player.scoreboardName}"))
        (player as ServerPlayer).openMenu(this)
    }

    /**
     * Open this GUI for multiple players. All players mentioned in the builder phase are automatically forced to open the GUI
     * @param players Target Player collection
     */
    fun open(players: Collection<Player>) {
        players.forEach { open(it) }
    }
}