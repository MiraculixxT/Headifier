package de.miraculixx.headifier.utils.gui.data

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

abstract class CustomInventory(private val player: Player?) {
    abstract val inventory: Inventory

    /**
     * Get the final inventory object for further operations.
     * @return Crafted storage GUI
     */
    fun get(): Inventory {
        return inventory
    }

    /**
     * Open the final inventory for a player.
     * @param player Target Player - Not needed if already set in builder
     */
    fun open(player: Player? = null) {
        player?.openInventory(inventory)
        this.player?.openInventory(inventory)
    }
}