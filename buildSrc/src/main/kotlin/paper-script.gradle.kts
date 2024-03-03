
plugins {
    kotlin("jvm")
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
    compileOnly("de.miraculixx","kpaper","1.1.1")
    compileOnly("dev.jorel:commandapi-bukkit-kotlin:9.2.0")
    compileOnly("dev.jorel:commandapi-bukkit-shade:9.2.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
