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
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.mina:mina-core:2.1.5")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}