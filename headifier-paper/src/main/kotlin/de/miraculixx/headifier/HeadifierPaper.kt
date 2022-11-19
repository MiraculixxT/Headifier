package de.miraculixx.headifier

import net.axay.kspigot.main.KSpigot

class HeadifierPaper: KSpigot() {
    companion object {
        lateinit var INSTANCE: KSpigot
    }

    override fun startup() {
        INSTANCE = this
    }

    override fun shutdown() {

    }
}

val PluginManager by lazy { HeadifierPaper.INSTANCE }