import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    application
}

group = "me.pacheco"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
dependencies {
    implementation("net.sourceforge.tess4j:tess4j:4.5.5")
}

application {
    mainClass.set("MainKt")
}
