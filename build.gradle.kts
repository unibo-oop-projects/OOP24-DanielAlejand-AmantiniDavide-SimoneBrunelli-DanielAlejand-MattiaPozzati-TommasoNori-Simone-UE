import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.Tar
import org.gradle.jvm.application.tasks.CreateStartScripts
plugins {
    java
    application

    // Fat-jar plugin
    id("com.github.johnrengelman.shadow") version "8.1.1"
    // QA plugin (SpotBugs, static checks, config-cache)
    id("org.danilopianini.gradle-java-qa") version "1.96.0"

    id("org.danilopianini.unibo-oop-gradle-plugin") version "1.1.40"
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }


repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.3")
    implementation("com.omertron:API-OMDB:1.5")
    implementation("org.jooq:jool:0.9.15")

    val slf4jVersion = "2.0.17"
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.18")

    val junit = "5.11.4"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
}

application {
    mainClass.set("it.unibo.exam.Main")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
    }
}

// 1) Configure the ShadowJar to produce a single fat-jar
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("university-escape")
    archiveClassifier.set("")   // omit “-all”
    archiveVersion.set("")      // omit version
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

// 2) Make `./gradlew build` also run shadowJar
tasks.named("build") {
    dependsOn("shadowJar")
}

// 3) Ensure distributions & scripts wait for the fat-jar
tasks.named<Zip>("distZip") { dependsOn("shadowJar") }
tasks.named<Tar>("distTar") { dependsOn("shadowJar") }
tasks.named<CreateStartScripts>("startShadowScripts") {
    dependsOn("jar", "shadowJar")
}
tasks.named<CreateStartScripts>("startScripts") {
    dependsOn("jar", "shadowJar")
}
