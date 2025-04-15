plugins {
    id("java")
}

group = "core.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    
    implementation("bsf:bsf:2.4.0")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}