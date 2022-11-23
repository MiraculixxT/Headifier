package de.miraculixx.headifier.utils.gui.data

import de.miraculixx.headifier.utils.gui.item.printJSONData
import de.miraculixx.headifier.utils.messages.*
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.MenuType

abstract class CustomInventory(size: Int, private val title: Component) : SimpleContainer(size), MenuProvider {
    private val viewers: MutableList<Player> = mutableListOf()
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

    /**
     * Internal function to create the GUI. [inventory] is act the player inventory and is ignored in 99% of all cases
     */
    override fun createMenu(size: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
        val height = containerSize / 9
        val menuType = when (height) {
            1 -> MenuType.GENERIC_9x1
            2 -> MenuType.GENERIC_9x2
            3 -> MenuType.GENERIC_9x3
            4 -> MenuType.GENERIC_9x4
            5 -> MenuType.GENERIC_9x5
            else -> MenuType.GENERIC_9x6
        }
        return ChestMenu(menuType, size, inventory, this, height)
    }

    /**
     * Internal event to detect players opening the GUI
     */
    override fun startOpen(player: Player) {
        viewers += player
    }

    /**
     * Internal event ot detect players closing the GUI
     */
    override fun stopOpen(player: Player) {
        viewers -= player
        getItem(0).printJSONData()
        if (viewers.isEmpty()) {
            InventoryManager.remove(id)
            if (debug) consoleAudience?.sendMessage(prefix + cmp("Removing GUI '$id' from cache"))
        }
    }

    /**
     * Internal function to get GUI title
     */
    override fun getDisplayName(): net.minecraft.network.chat.Component {
        return title.toMC()
    }
}