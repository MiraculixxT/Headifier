
plugins {
    `kotlin-script`
    `loom-script`
    `adventure-script`
}

repositories {
    mavenCentral()
}

dependencies {

    include(implementation(project(":headifier-core"))!!)
}