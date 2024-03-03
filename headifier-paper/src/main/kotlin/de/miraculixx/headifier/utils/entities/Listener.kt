package de.miraculixx.headifier.utils.entities

import de.miraculixx.kpaper.event.SingleListener
import de.miraculixx.kpaper.event.unregister
import org.bukkit.event.Event

interface Listener<T : Event> {
    val listener: SingleListener<T>

    fun unregister() {
        listener.unregister()
    }

    fun register()
}