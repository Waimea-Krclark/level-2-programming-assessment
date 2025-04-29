plugins {
    kotlin("jvm") version "2.0.0"
    id("application")
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.varabyte.kotter:kotter:1.2.1")
    implementation("org.jline:jline:3.25.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
tasks.jar {
    manifest{
        attributes(mapOf("Main-Class" to "MainKt"))
    }
    configurations["compileClasspath"].forEach {file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}