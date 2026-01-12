plugins {
    /* Kotlin */
    kotlin("jvm") version "2.2.10"
    /* Typewriter */
    id("com.typewritermc.module-plugin") version "2.1.0"
    /* Shadow */
    id("com.gradleup.shadow") version "9.3.1"
    /* Maven Publish */
    `maven-publish`
}

group = "fr.legendsofxania"
version = "0.0.1"

repositories {
    /* Blockify */
    maven("https://jitpack.io")
}

dependencies {
    /* Blockify */
    implementation("com.github.Kooperlol:Blockify:1.0.0")
}

typewriter {
    namespace = "legendsofxania"

    extension {
        name = "Structure"
        shortDescription = "Display schematics in Typewriter."
        description = """
            Display schematics in your interactions and create
            beautiful places directly in Typewriter.
            Created by the Legends of Xania.
            """.trimMargin()
        engineVersion = "0.9.0-beta-170"
        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA

        paper()
    }
}

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        relocate("dev.kooper.blockify", "fr.legendsofxania.structure.lib.blockify")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String

            artifact(tasks.shadowJar)
        }
    }
}