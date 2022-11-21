package de.miraculixx.headifier.utils.entities

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.unregister
import org.bukkit.event.Event

interface Listener<T : Event> {
    val listener: SingleListener<T>

    fun unregister() {
        listener.unregister()
    }

    fun register()
}