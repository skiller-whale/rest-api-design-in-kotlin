plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization").version("1.8.0")
    application
}

group = "com.skillerwhale"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

val ktor_version = "2.2.3"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.5")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("stock_management.MainKt")
}
