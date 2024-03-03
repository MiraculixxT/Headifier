
plugins {
    id("fabric-loom")
//    id("io.github.juuxel.loom-quiltflower")
}

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "com.mojang")
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
}

repositories {
    maven {
        name = "JitPack"
        setUrl("https://jitpack.io")
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.2")
    mappings(loom.officialMojangMappings())

    modImplementation("net.silkmc:silk-commands:1.10.2")
    modImplementation("net.silkmc:silk-core:1.10.2")
    modImplementation("net.fabricmc:fabric-loader:0.14.22")
    modImplementation(include("net.kyori:adventure-platform-fabric:5.10.0")!!)
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.89.2+1.20.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.10+kotlin.1.9.10")
    modImplementation(include("me.lucko", "fabric-permissions-api", "0.2-SNAPSHOT"))
    transitiveInclude(implementation("org.yaml:snakeyaml:1.33")!!)

    transitiveInclude.resolvedConfiguration.resolvedArtifacts.forEach {
        include(it.moduleVersion.id.toString())
    }
}