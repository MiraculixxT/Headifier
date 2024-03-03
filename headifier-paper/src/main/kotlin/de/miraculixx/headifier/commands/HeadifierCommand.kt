package de.miraculixx.headifier.commands

import de.miraculixx.headifier.utils.gui.items.getItem
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.cError
import de.miraculixx.headifier.utils.messages.cmp
import de.miraculixx.headifier.utils.messages.plus
import de.miraculixx.headifier.utils.messages.prefix
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.itemStackArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class HeadifierCommand {
    val command = commandTree("headifier") {
        literalArgument("get") {
            withPermission("headifier.command.get")
            itemStackArgument("material") {
                playerExecutor { player, args ->
                    val item = args[0] as ItemStack
                    val type = item.type
                    if (!type.isBlock || type.isLegacy) {
                        player.sendMessage(prefix + cmp("Please provide a valid block!", cError))
                        return@playerExecutor
                    }

                    val head = headCache.getActivatedHeads(type.name).randomOrNull()
                    if (head == null) {
                        player.sendMessage(prefix + cmp("No activated heads were found for this block!", cError))
                        return@playerExecutor
                    }
                    player.inventory.addItem(head.getItem())
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
                }
            }
        }

        literalArgument("edit") {
            withPermission("headifier.command.edit")
            itemStackArgument("material") {
                playerExecutor { player, args ->
                    val item = args[0] as ItemStack
                    val type = item.type
                    if (!type.isBlock || type.isLegacy) {
                        player.sendMessage(prefix + cmp("Please provide a valid block!", cError))
                        return@playerExecutor
                    }

                    val head = headCache.getActivatedHeads(type.name)
                    // Create Inv
                }
            }
        }
    }
}