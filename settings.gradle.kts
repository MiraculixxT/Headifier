rootProject.name = "headifier"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

include("headifier-core")
include("headifier-fabric")
include("headifier-paper")