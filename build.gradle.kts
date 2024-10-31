plugins {
    id("java")
}

group = "com.rina"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.lavalink.dev/releases")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.1.2")
    implementation("dev.arbjerg:lavaplayer:2.2.2")
    implementation("com.neovisionaries:nv-websocket-client:2.14")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.slf4j:slf4j-api:2.0.7")

    implementation("io.github.classgraph:classgraph:4.8.175")
}

tasks.test {
    useJUnitPlatform()
}