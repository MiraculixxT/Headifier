package de.miraculixx.headifier.utils.gui

import de.miraculixx.headifier.utils.entities.Listener
import de.miraculixx.headifier.utils.messages.plainSerializer
import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickEvent: Listener<InventoryClickEvent> {
    override val listener: SingleListener<InventoryClickEvent> = listen(register = false) {
        val view = it.view
        val title = plainSerializer.serialize(view.title())
        if (title == "Player Statistics" || title == "Crafting Rezept") it.isCancelled = true
    }

    override fun register() {
        listener.register()
    }
}