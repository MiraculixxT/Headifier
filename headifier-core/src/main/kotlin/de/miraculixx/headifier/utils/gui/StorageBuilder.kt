package de.miraculixx.headifier.utils.gui

import de.miraculixx.headifier.utils.gui.data.CustomInventory
import de.miraculixx.headifier.utils.gui.item.*
import de.miraculixx.headifier.utils.messages.cHighlight
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.headifier.utils.messages.emptyComponent
import de.miraculixx.headifier.utils.messages.plus
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments

class StorageGUI(
    private val content: Map<ItemStack, Boolean>,
    private val header: List<ItemStack>,
    private val filterable: Boolean,
    filterName: String?,
    private val scrollable: Boolean,
    private val players: List<Player>,
    title: Component,
    override val id: String
) : CustomInventory(6 * 9, title) {
    private var filter: String? = filterName

    private constructor(builder: Builder) : this(
        buildMap {
            putAll(builder.markableItems)
            builder.items.forEach { put(it, false) }
        },
        buildList {
            addAll(builder.headers)
            builder.header?.let { add(it) }
        },
        builder.filterable,
        builder.filterName,
        builder.scrollable,
        buildList {
            addAll(builder.players)
            builder.player?.let { add(it) }
        },
        builder.title,
        builder.id
    )

    class Builder(val id: String) {
        /**
         * Import items that are markable. Marked items will be displayed with either an enchanting glint or and be replaced with a shiny green glass pane, if they not support enchanting glints.
         * @see items
         */
        var markableItems: Map<ItemStack, Boolean> = emptyMap()

        /**
         * Import items to the storage GUI.
         * @see markableItems
         */
        var items: List<ItemStack> = emptyList()

        /**
         * Decorate the storage header (first row) with custom items. You can set
         * - 0 Items for no header
         * - 1 Item ----o----
         * - 2 Items ---o-o---
         * - 3 Items --o--o--o--
         * - 4 Items -o-o-o-o-
         * @see header
         */
        var headers: List<ItemStack> = emptyList()

        /**
         * Decorate the storage header (first row) with a custom item. It will be centered
         * @see headers
         */
        var header: ItemStack? = null

        /**
         * Configure the GUI as filterable. Filterable storage GUIs will have a placeholder row at the bottom with a centered filter switcher
         *
         * **Default: false**
         * @see filterName
         */
        var filterable: Boolean = false

        /**
         * Set the GUI filter. By default, it is "No Filter".
         *
         * **ONLY works if GUI is filterable!**
         * @see filterName
         */
        var filterName: String? = null

        /**
         * Configure the GUI as scrollable. Scrollable storage GUIs will have up and down arrows to navigate on overflow. On disabled scroll GUIs, overflow will be ignored.
         *
         * **Default: false**
         */
        var scrollable: Boolean = false

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
         * Sets the inventory title for this storage GUI.
         */
        var title: Component = emptyComponent()

        /**
         * Internal use. No need to call it inlined
         */
        fun build() = StorageGUI(this)
    }

    private fun build() {
        //Header
        when (header.size) {
            1 -> setItem(4, header[0])
            2 -> {
                setItem(3, header[0])
                setItem(5, header[1])
            }

            3 -> {
                setItem(2, header[0])
                setItem(4, header[1])
                setItem(6, header[2])
            }

            4 -> {
                setItem(1, header[0])
                setItem(3, header[1])
                setItem(5, header[2])
                setItem(7, header[2])
            }
        }

        //Filter Apply
        if (filterable) {
            setItem(49, itemStack(Items.HOPPER, 1) {
                setCustomModel(205)
                setCustomName(cmp("Filters", cHighlight, bold = true))
                setLore(listOf(
                    emptyComponent(),
                    cmp("Filter", cHighlight, underlined = true),
                    cmp("∙ ${filter ?: "No Filter"}"),
                    emptyComponent(),
                    cmp("Click ", cHighlight) + cmp("≫ Change Filter")
                ))
                setPDCValue("gui.storage.filter", filter ?: "No Filter")
            })
        }

        //Content
        var counter = 0
        content.forEach { (item, activated) ->
            val finalItem = if (activated) {
                 val i = when (item.item) {
                    Items.PLAYER_HEAD, Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.CHEST,
                    Items.ENDER_CHEST, Items.TRAPPED_CHEST -> item.copyAsMaterial(Items.LIME_STAINED_GLASS_PANE)
                    else -> item
                }

                i.enchant(Enchantments.MENDING, 1)
                i.addHideFlags(HideFlag.HIDE_ENCHANTS)
                i
            } else item
            if (((scrollable || filterable) && counter >= 24) || counter >= 30) return
            setItem(9 + counter, finalItem)
            counter++
        }
    }

    private fun fillPlaceholder() {
        val darkHolder = itemStack(Items.GRAY_STAINED_GLASS_PANE) { setCustomName(emptyComponent()) }
        val lightHolder = itemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE) { setCustomName(emptyComponent()) }
        (0..8).forEach { setItem(it, darkHolder) }
        (9..53).forEach { setItem(it, lightHolder) }
        if (scrollable || filterable) (45..53).forEach { setItem(it, darkHolder) }
    }

    init {
        fillPlaceholder()
        build()
    }
}

