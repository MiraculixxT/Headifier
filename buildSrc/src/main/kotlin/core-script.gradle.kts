import gradle.kotlin.dsl.accessors._716a08d120ec896a570c442c28d24466.loom
import gradle.kotlin.dsl.accessors._716a08d120ec896a570c442c28d24466.mappings
import gradle.kotlin.dsl.accessors._716a08d120ec896a570c442c28d24466.minecraft
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

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