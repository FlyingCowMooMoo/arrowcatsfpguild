
plugins {
    scala
}

group = "io.github.flyingcowmoomoo.arrowcatsfpguild.scala"
version = "1.0-SNAPSHOT"
val arrowVersion = "0.10.4"

repositories {
    mavenCentral()
}

dependencies {
	implementation(project(":java-stuff"))
    implementation("org.scala-lang:scala-library:2.13.6")
    implementation("org.typelevel:cats-effect_2.13:3.2.2")
    implementation("org.typelevel:cats-core_2.13:2.6.1")
    scalaCompilerPlugins("com.olegpy:better-monadic-for_2.13:0.3.1")
}

tasks {
    compileScala {
        scalaCompileOptions.apply {
            additionalParameters = mutableListOf("-language:postfixOps")
        }
    }
}