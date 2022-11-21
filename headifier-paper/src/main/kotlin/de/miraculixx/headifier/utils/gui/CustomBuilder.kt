package de.miraculixx.headifier.utils.gui

import de.miraculixx.headifier.utils.gui.data.CustomInventory
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.headifier.utils.messages.emptyComponent
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Inline Builder for GUI type - Storages
 * Use storage GUIs to display a lot of content with a minimal of placeholders. They can be filtered, scrollable and supports menus
 * @author Miraculixx
 */
inline fun inventoryBuilder(builder: CustomBuilder.Builder.() -> Unit) = CustomBuilder.Builder().apply(builder).build()

class CustomBuilder(
    private val content: Map<ItemStack, Int>,
    player: Player?,
    title: Component,
    size: Int
) : CustomInventory(player) {
    override val inventory = Bukkit.createInventory(null, size * 9, title)

    private constructor(builder: Builder) : this(
        builder.content,
        builder.player,
        builder.title,
        builder.size
    )

    class Builder {
        /**
         * Import items to the custom GUI.
         */
        var content: Map<ItemStack, Int> = emptyMap()

        /**
         * Connect a player to this GUI instance. This will let run further operations without player context.
         */
        var player: Player? = null

        /**
         * Sets the inventory title for this custom GUI.
         */
        var title: Component = emptyComponent()

        /**
         * Sets the inventory size. It defines the row count, [size] 2 will create a GUI with 18 slots (2 rows)
         */
        var size: Int = 1

        /**
         * Internal use. No need to call it inlined
         */
        fun build() = CustomBuilder(this)
    }

    private fun build() {
        content.forEach { (item, slot) ->
            inventory.setItem(slot, item)
        }
    }

    private fun fillPlaceholder() {
        val primaryPlaceholder = itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { name = cmp(" ") }}
        val secondaryPlaceholder = itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = cmp(" ") }}

        repeat(inventory.size) {
            inventory.setItem(it, primaryPlaceholder)
        }
        if (inventory.size != 9) {
            inventory.setItem(17, secondaryPlaceholder)
            inventory.setItem(inventory.size - 18, secondaryPlaceholder)
            repeat(2) { inventory.setItem(it, secondaryPlaceholder) }
            repeat(3) { inventory.setItem(it + 7, secondaryPlaceholder) }
            repeat(3) { inventory.setItem(inventory.size - it - 8, secondaryPlaceholder) }
            repeat(2) { inventory.setItem(inventory.size - it - 1, secondaryPlaceholder) }
        } else {
            inventory.setItem(0, secondaryPlaceholder)
            inventory.setItem(8, secondaryPlaceholder)
        }
    }

    init {
        fillPlaceholder()
        build()
    }
}