package de.miraculixx.headifier.modules

import de.miraculixx.headifier.utils.entities.Listener
import de.miraculixx.headifier.utils.enumOf
import de.miraculixx.headifier.utils.gui.items.getItem
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.*
import de.miraculixx.kpaper.event.SingleListener
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.event.register
import de.miraculixx.kpaper.items.customModel
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.items.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType

class HeadifierTool(config: FileConfiguration) : Listener<BlockBreakEvent> {
    private val maxUses = config.getInt("tool-max-uses").toShort()
    private val cooldown = config.getInt("tool-cooldown")
    private val usesKey = NamespacedKey("de.miraculixx.api", "custom-item.headifier.uses")
    private val item = itemStack(enumOf<Material>(config.getString("tool-material")) ?: Material.GOLDEN_PICKAXE) {
        meta {
            this.name = miniMessages.deserialize(config.getString("tool-name") ?: "<light_purple><!i>Headifier")
            lore(buildList {
                addAll(config.getStringList("tool-lore").map { miniMessages.deserialize(it) })
                if (maxUses > 0) {
                    add(emptyComponent())
                    add(cmp("Uses: 0/$maxUses", cMark))
                }
            })
            customModel = 1009
            addEnchant(Enchantment.SILK_TOUCH, 1, true)
            addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
            isUnbreakable = true
            persistentDataContainer.set(usesKey, PersistentDataType.SHORT, 0)
        }
    }
    override val listener: SingleListener<BlockBreakEvent> = listen(register = false) {
        val player = it.player
        val item = player.inventory.itemInMainHand
        if (item.itemMeta?.customModel != 1009) return@listen
        val type = item.type
        if (player.hasCooldown(type)) {
            it.isCancelled = true
            return@listen
        }

        val name = it.block.type.name
        val heads = headCache.getActivatedHeads(name)
        if (heads.isEmpty()) {
            it.isCancelled = true
            player.sendMessage(prefix + cmp("This block has no minified head :(", cError))
            if (debug) player.sendMessage(prefix + cmp("Loaded Blocks: ${headCache.getHeads().size} || Block Name: $name"))
            return@listen
        }

        it.isDropItems = false
        player.setCooldown(type, 20 * cooldown)
        it.block.world.dropItem(it.block.location.add(.5, .5, .5), heads.random().getItem())
        val meta = item.itemMeta
        val uses = meta.persistentDataContainer.get(usesKey, PersistentDataType.SHORT) ?: 0
        if (uses == maxUses) {
            player.inventory.setItemInMainHand(null)
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
        } else {
            meta.persistentDataContainer.set(usesKey, PersistentDataType.SHORT, (uses + 1).toShort())
            meta.lore(listOf(cmp("Uses: ${uses + 1}/$maxUses", cMark)))
            item.itemMeta = meta
        }
    }

    override fun register() {
        listener.register()
    }

    private fun addCraftingRecipe(config: FileConfiguration) {
        val shapeList = config.getStringList("tool-recipe")
        if (shapeList.size != 3) {
            consoleAudience?.sendMessage(invalidConfig("tool-recipe"))
            return
        }
        val recipe = ShapedRecipe(NamespacedKey("de.miraculixx.api", "crafting.headifier"), item)
        recipe.shape(shapeList[0], shapeList[1], shapeList[2])
        val section = config.getConfigurationSection("tool-recipe-items")
        if (section == null) {
            consoleAudience?.sendMessage(invalidConfig("tool-recipe-items"))
            return
        }
        section.getKeys(false).forEach { key ->
            val m = enumOf<Material>(section.getString(key))
            if (m == null) {
                consoleAudience?.sendMessage(invalidConfig("tool-recipe-items ($key)"))
                return@forEach
            }
            recipe.setIngredient(key[0], m)
        }

        if (!Bukkit.addRecipe(recipe)) {
            consoleAudience?.sendMessage(prefix + cmp("Failed to add crafting recipe for 'Headifier-Tool'! Please check your recipe pattern"))
        }
    }

    init {
        if (config.getBoolean("tool-craftable")) addCraftingRecipe(config)
    }
}