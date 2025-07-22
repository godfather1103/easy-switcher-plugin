import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "io.github.godfather1103"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    implementation("com.squareup.okhttp3:okhttp:3.12.3")
    implementation("io.vavr:vavr:0.10.4")
    testImplementation("junit:junit:4.13.2")
    compileOnly("org.projectlombok:lombok:${property("lombok.version")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombok.version")}")
    intellijPlatform {
        create("IC", "2024.3")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
    }
}

intellijPlatform {
    sandboxContainer.set(file("idea-sandbox"))
    signing {
        cliPath = file("${project.projectDir.absolutePath}/tools/marketplace-zip-signer-cli.jar")
        project.findProperty("signing.certificateChainFile")?.let {
            certificateChainFile.set(file(it as String))
        }
        project.findProperty("signing.privateKeyFile")?.let {
            privateKeyFile.set(file(it as String))
        }
        project.findProperty("signing.password")?.let {
            password.set(it as String)
        }
    }
    publishing {
        project.findProperty("ORG_GRADLE_PROJECT_intellijPublishToken")?.let {
            token.set(it as String)
        }
    }
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "221"
        }
        description = """
            <h1>English Readme：</h1>
            <p>Easy Switcher</p>
        
            <h1>中文说明：</h1>
            <p>代理快速切换</a></p>
    """.trimIndent()
        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
