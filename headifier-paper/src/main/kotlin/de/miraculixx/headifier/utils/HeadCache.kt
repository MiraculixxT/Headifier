package de.miraculixx.headifier.utils

import de.miraculixx.headifier.HeadCache
import de.miraculixx.headifier.PluginManager
import java.io.File

val headCache = HeadCache(File("${PluginManager.dataFolder.path}/head-status.json"))