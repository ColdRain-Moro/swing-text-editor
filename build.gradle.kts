plugins {
    id("java")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("team.redrock.texteditor.Main")
}

dependencies {
    implementation("com.formdev:flatlaf-fonts-jetbrains-mono:2.242")
    implementation("com.formdev:flatlaf:3.1.1")
    implementation("org.apache.tika:tika-core:2.7.0")
    implementation("com.fifesoft:rsyntaxtextarea:3.3.3")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.test {
    useJUnitPlatform()
}