plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.mlunkeit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    implementation("org.jetbrains:annotations:26.0.2")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "de.mlunkeit.Main"
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "de.mlunkeit.Main"
    }
}