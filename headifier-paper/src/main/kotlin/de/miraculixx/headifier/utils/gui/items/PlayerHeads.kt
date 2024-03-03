package de.miraculixx.headifier.utils.gui.items

import de.miraculixx.headifier.HeadCache
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.kpaper.items.itemStack
import de.miraculixx.kpaper.items.meta
import de.miraculixx.kpaper.items.name
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.net.URL
import java.util.*


fun HeadCache.Head.getItem(): ItemStack {
    return itemStack(Material.PLAYER_HEAD) {
        meta<SkullMeta> {
            this.name = cmp(this@getItem.name, NamedTextColor.YELLOW)
            this.playerProfile = Bukkit.createProfile(UUID.fromString(uuid)).apply {
                setTextures(textures.apply { skin = URL(url) })
            }
        }
    }
}
