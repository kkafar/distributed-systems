import com.google.protobuf.gradle.*
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protoc
import org.gradle.kotlin.dsl.proto
import org.gradle.kotlin.dsl.protobuf

plugins {
    application
    idea
    id("com.google.protobuf") version "0.8.17"
}

sourceSets {
    create("proto") {
        proto {
            srcDir("src/main/proto")
        }
    }
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/kkafar/result-type")
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
//    maven {
//        name = "JFrogBintray"
//        url = uri("https://kkafara.jfrog.io/artifactory/kkafara-gradle-release-local")
//    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    implementation("com.google.guava:guava:30.1.1-jre")

    runtimeOnly("io.grpc:grpc-netty-shaded:1.45.1")
    implementation("io.grpc:grpc-protobuf:1.45.1")
    implementation("io.grpc:grpc-stub:1.45.1")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")

    implementation("com.google.code.gson:gson:2.9.0")

    implementation("com.kkafara.rt:result-type:1.0.3")
}

protobuf {
    this.protobuf.protoc {
        artifact = "com.google.protobuf:protoc:3.20.1"
    }
    this.protobuf.plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.45.1"
        }
    }
    this.protobuf.generateProtoTasks {
        ofSourceSet("proto").forEach {
            it.plugins {
                id("grpc")
            }
        }
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

application {
    // Define the main class for the application.
    mainClass.set("com.kkafara.server.App")
}
