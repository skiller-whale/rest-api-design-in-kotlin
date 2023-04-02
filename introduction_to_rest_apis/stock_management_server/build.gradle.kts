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

val ktorVersion = "2.2.3"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktorVersion") //KTor - the web server framework.
    implementation("io.ktor:ktor-server-netty:$ktorVersion")  //KTor - the web server engine.
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion") //Use Jackson to serialize/deserialize - kotlinx breaks auto-reload (https://youtrack.jetbrains.com/issue/KTOR-5426)
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    testImplementation(kotlin("test")) //I will leave this in, to remind me to write some tests.
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("stock_management.MainKt")
}
