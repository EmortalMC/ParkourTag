import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
    // Kotlinx serialization for any data format
    kotlin("plugin.serialization") version "1.4.21"
    // Shade the plugin
    id("com.github.johnrengelman.shadow") version "7.1.0"
    // Allow publishing
    `maven-publish`

    // Apply the application plugin to add support for building a jar
    java
    // Dokka documentation w/ kotlin
    id("org.jetbrains.dokka") version "1.5.0"
}

repositories {
    // Use mavenCentral
    mavenCentral()

    maven(url = "https://jitpack.io")
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://repo.minestom.com/repository/maven-public/")
    maven(url = "https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    // Align versions of all Kotlin components
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    compileOnly(kotlin("stdlib"))

    // Use the Kotlin reflect library.
    compileOnly(kotlin("reflect"))

    compileOnly("com.github.Minestom:Minestom:0bcfc39a9d")
    compileOnly("com.github.EmortalMC:Immortal:c0280c69dd")

    // TODO: get extension instead of compiling separately
    compileOnly("com.github.Project-Cepi:Particable:b552376803")
    // import kotlinx serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")


    implementation(files("libs/Blocky-1.0-SNAPSHOT.jar"))
}
tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

// Take gradle.properties and apply it to resources.
tasks {
    processResources {
        // Apply properties to extension.json
        filesMatching("extension.json") {
            expand(project.properties)
        }
    }

    // Set name, minimize, and merge service files
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        mergeServiceFiles()
        minimize()
    }

    test { useJUnitPlatform() }

    // Make build depend on shadowJar as shading dependencies will most likely be required.
    build {
        dependsOn(shadowJar)
    }

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}
