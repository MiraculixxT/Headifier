import BuildConstants.minecraftVersion

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("${minecraftVersion}-R0.1-SNAPSHOT")
    implementation("net.axay","kspigot","1.19.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}