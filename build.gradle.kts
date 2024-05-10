plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.florianraith"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation("com.google.inject:guice:7.0.0")
    implementation("com.google.guava:guava:33.2.0-jre")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {
    archiveClassifier.set("")
}

val copyJarToPlugins by tasks.registering(Copy::class) {
    from("build/libs/")
    include("*.jar")
    into("paper/plugins/")
}

tasks.named("shadowJar") {
    finalizedBy(copyJarToPlugins)
}
