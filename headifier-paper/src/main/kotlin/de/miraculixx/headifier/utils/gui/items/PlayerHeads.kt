package de.miraculixx.headifier.utils.gui.items

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import de.miraculixx.headifier.HeadCache
import de.miraculixx.headifier.utils.messages.cError
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.headifier.utils.messages.plus
import de.miraculixx.headifier.utils.messages.prefix
import net.axay.kspigot.extensions.console
import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*

fun HeadCache.Head.getItem(): ItemStack {
    return itemStack(Material.PLAYER_HEAD) {
        itemMeta = skullTexture(itemMeta as SkullMeta, value, uuid)
    }
}

fun skullTexture(meta: SkullMeta, base64: String, uuid: String): SkullMeta {
    val profile = GameProfile(UUID.fromString(uuid), "")
    profile.properties.put("textures", Property("textures", base64))
    val profileField: Field?
    try {
        profileField = meta.javaClass.getDeclaredField("profile")
        profileField.isAccessible = true
        profileField[meta] = profile
    } catch (e: Exception) {
        e.printStackTrace()
        console.sendMessage(prefix + cmp("Head Builder failed to apply Base64 Code to Skull!", cError))
        console.sendMessage(prefix + cmp("Code: ", cError))
    }
    return meta
}