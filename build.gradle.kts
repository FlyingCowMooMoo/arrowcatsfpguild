plugins {
    java
    idea
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "io.github.flyingcowmoomoo.arrowcatsfpguild"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("java")
        plugin("idea")
    }
    java.sourceCompatibility = JavaVersion.VERSION_11

}