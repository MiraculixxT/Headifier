import BuildConstants.minecraftVersion

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("${minecraftVersion}-R0.1-SNAPSHOT")
    implementation("net.axay","kspigot","1.19.0")
    implementation(project(":headifier-core"))
}

tasks {
    cleanCache
    shadowJar {
        //minimize()
        dependencies {
            include(dependency(":headifier-core:"))
        }
    }
    assemble {
        //dependsOn(shadowJar)
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

tasks {
    runServer {
        minecraftVersion("1.19.2")
    }
}
