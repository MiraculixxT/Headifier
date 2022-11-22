package de.miraculixx.headifier.utils.gui

import de.miraculixx.headifier.utils.gui.data.CustomInventory
import de.miraculixx.headifier.utils.gui.data.InventoryManager
import de.miraculixx.headifier.utils.gui.item.itemStack
import de.miraculixx.headifier.utils.gui.item.setCustomName
import de.miraculixx.headifier.utils.messages.*
import net.kyori.adventure.text.Component
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class CustomBuilder(
    private val content: Map<ItemStack, Int>,
    private val title: Component,
    override val id: String,
    player: List<Player>,
    size: Int,
) : SimpleContainer(size * 9), CustomInventory {
    override val inventory = get()
    override val viewers: MutableList<Player> = player.toMutableList()

    constructor(builder: CustomInventoryBuilder) : this(
        builder.content,
        builder.title,
        builder.id,
        buildList {
            addAll(builder.players)
            builder.player?.let { add(it) }
        },
        builder.size
    )

    private fun build() {
        content.forEach { (item, slot) ->
            inventory.setItem(slot, item)
        }
    }

    private fun fillPlaceholder() {
        val primaryPlaceholder = itemStack(Items.GRAY_STAINED_GLASS_PANE) { setCustomName(cmp(" ")) }
        val secondaryPlaceholder = itemStack(Items.BLACK_STAINED_GLASS_PANE) { setCustomName(cmp(" ")) }

        val size = inventory.containerSize
        repeat(size) {
            inventory.setItem(it, primaryPlaceholder)
        }
        if (size != 9) {
            inventory.setItem(17, secondaryPlaceholder)
            inventory.setItem(size - 18, secondaryPlaceholder)
            repeat(2) { inventory.setItem(it, secondaryPlaceholder) }
            repeat(3) { inventory.setItem(it + 7, secondaryPlaceholder) }
            repeat(3) { inventory.setItem(size - it - 8, secondaryPlaceholder) }
            repeat(2) { inventory.setItem(size - it - 1, secondaryPlaceholder) }
        } else {
            inventory.setItem(0, secondaryPlaceholder)
            inventory.setItem(8, secondaryPlaceholder)
        }
    }

    override fun getDisplayName(): net.minecraft.network.chat.Component {
        return title.toMC()
    }

    override fun createMenu(i: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
        val height = containerSize / 9
        val menuType = when (height) {
            1 -> MenuType.GENERIC_9x1
            2 -> MenuType.GENERIC_9x2
            3 -> MenuType.GENERIC_9x3
            4 -> MenuType.GENERIC_9x4
            5 -> MenuType.GENERIC_9x5
            else -> MenuType.GENERIC_9x6
        }
        return ChestMenu(menuType, i, inventory, inventory, height)
    }

    override fun stopOpen(player: Player) {
        viewers -= player
        if (viewers.isEmpty()) {
            InventoryManager.remove(id)
            if (debug) consoleAudience?.sendMessage(prefix + cmp("Removing GUI '$id' from cache"))
        }
        println("stop open")
    }

    override fun startOpen(player: Player) {
        viewers += player
        println("start open")
    }

    init {
        println("init")
        if (viewers.isEmpty()) {
            consoleAudience?.sendMessage(prefix + cmp("Creating GUI without player - Unexpected behaviour", cError))
            InventoryManager.remove(id)
        } else {
            println("fill placeholder")
            fillPlaceholder()
            println("build")
            build()
            println("open")
            open(viewers)
        }
    }
}

class CustomInventoryBuilder(val id: String) {
    /**
     * Import items to the custom GUI.
     */
    var content: Map<ItemStack, Int> = emptyMap()

    /**
     * Connect players to this GUI instance. Providing no player will lead to an instant removal of this GUI from cache.
     *
     * Use [player] for only one player
     */
    var players: List<Player> = emptyList()

    /**
     * Connect a player to this GUI instance. Providing no player will lead to an instant removal of this GUI from cache.
     *
     * Use [players] for multi-view
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