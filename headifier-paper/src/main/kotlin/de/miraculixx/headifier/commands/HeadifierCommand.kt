package de.miraculixx.headifier.commands

import com.mojang.brigadier.arguments.StringArgumentType
import de.miraculixx.headifier.utils.entities.LateInitLoading
import de.miraculixx.headifier.utils.gui.data.InventoryManager
import de.miraculixx.headifier.utils.gui.item.itemStack
import de.miraculixx.headifier.utils.gui.items.getItem
import de.miraculixx.headifier.utils.headCache
import de.miraculixx.headifier.utils.messages.*
import net.axay.kspigot.commands.*
import net.axay.kspigot.event.listen
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.event.player.PlayerToggleSneakEvent

class HeadifierCommand : LateInitLoading {
    private var validMaterials: List<String> = listOf("<error>")

    val command = command("headifier") {
        requires { ctx -> ctx.isPlayer }

        literal("get") {
            requires { ctx -> ctx.bukkitSender.hasPermission("headifier.command.get") }
            argument<String>("material", StringArgumentType.word()) {
                suggestList { ctx -> filterTabComplete(ctx.input) }
                runs {
                    val input = getArgument<String>("material")
                    val head = headCache.getActivatedHeads(input).randomOrNull()
                    if (head == null) {
                        player.sendMessage(prefix + cmp("No activated heads were found for this block!", cError))
                        return@runs
                    }
                    player.inventory.addItem(head.getItem())
                }
            }
        }

        literal("edit") {
            requires { ctx -> ctx.bukkitSender.hasPermission("headifier.command.edit") }
            argument<String>("material", StringArgumentType.word()) {
                suggestList { ctx -> filterTabComplete(ctx.input) }
                runs {
                    val input = getArgument<String>("material")
                    val heads = headCache.getHeads(input)

                        InventoryManager.inventoryBuilder("TEST") {
                            this.player = sender.player
                            size = 1
                            title = cmp("Activate/Deactivate Heads", cHighlight)
                            content = mapOf(itemStack(Items.STONE, 1) {} to 4)
                        }
                }
            }
        }
    }

    private fun filterTabComplete(input: String): List<String> {
        val args = input.split(' ')
        return validMaterials.filter {
            it.startsWith(args.getOrNull(2) ?: "", true)
        }
    }

    override fun loadData() {
        validMaterials = Material.values().filter {
            !it.isLegacy && it.isBlock && headCache.getHeads(it.name).isNotEmpty()
        }.map { it.name }
    }
}