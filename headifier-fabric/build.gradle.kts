
plugins {
    `kotlin-script`
    `loom-script`
    `adventure-script`
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:0.14.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.66.0+1.19.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.extra["fabric_language_kotlin_version"] as String}")
    modImplementation(include("net.kyori:adventure-platform-fabric:5.4.0")!!)
    include(implementation(project(":headifier-core"))!!)
}