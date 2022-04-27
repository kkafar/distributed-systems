plugins {
  java
  application
}

application {
  mainClassName = "kkafara.Main"
}

group = "kkafara"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

println("Before configuring run task")

tasks.getByName<JavaExec>("run") {
  println("setting up run task")
  standardInput = System.`in`
}

dependencies {
  implementation("org.apache.logging.log4j:log4j-api:2.17.2")
  implementation("org.apache.logging.log4j:log4j-core:2.17.2")
  implementation("org.jetbrains:annotations:23.0.0")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
  println("Inside test task")
  useJUnitPlatform()
}
