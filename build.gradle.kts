plugins {
    `java-library`
    // https://plugins.gradle.org/plugin/io.papermc.paperweight.userdev
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "imagineBreaker"
version = "1.0.0"
val javaVersion = 16
val filePath = ""

repositories {
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0-SNAPSHOT")
}

tasks {
    compileJava {
        options.release = javaVersion
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        expand(
            "name" to project.name,
            "pluginVersion" to if (version == "") "1.0.0" else version,
            "description" to "우선 그 환상을 부숴주마!!",
            "author" to "카미조 토우마",
            "website" to "https://namu.wiki/w/%EC%B9%B4%EB%AF%B8%EC%A1%B0%20%ED%86%A0%EC%9A%B0%EB%A7%88",
            "apiVersion" to "1.17"
        )
    }
    jar {
        archiveFileName = "${project.name}${if (version == "") version else "-${version}"}.jar"
        if (filePath != "") {
            destinationDirectory.set(File(filePath))
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}