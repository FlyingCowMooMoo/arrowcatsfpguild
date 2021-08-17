
plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("kapt") version "1.5.21"
}
val arrowVersion = "0.13.2"

group = "io.github.flyingcowmoomoo.arrowcatsfpguild.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":java-stuff"))
    implementation(kotlin("stdlib"))
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}
