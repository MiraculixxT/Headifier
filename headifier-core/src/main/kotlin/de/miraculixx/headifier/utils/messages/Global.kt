package de.miraculixx.headifier.utils.messages

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.format.NamedTextColor

const val mhAPI = "https://minecraft-heads.com/scripts/api.php?"
var consoleAudience: Audience? = null
var debug = false

val prefix = cmp("Headifier", cHighlight) + cmp(" >> ", NamedTextColor.DARK_GRAY)
val noPlayer = prefix + cmp("You need to be a player to execute this command!", cError)
fun invalidConfig(key: String) = prefix + cmp("Configuration section '$key' is invalid!", cError)