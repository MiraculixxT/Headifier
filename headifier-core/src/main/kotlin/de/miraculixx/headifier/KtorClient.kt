package de.miraculixx.headifier

import io.ktor.client.*
import io.ktor.client.engine.cio.*

val client = HttpClient(CIO)