
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("fabric-loom")
}

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.1.3"
    minecraft("com.mojang:minecraft:1.19.2")
    mappings(loom.officialMojangMappings())
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("net.kyori:adventure-api:4.11.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("net.kyori:adventure-text-serializer-plain:4.11.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.11.0")
}