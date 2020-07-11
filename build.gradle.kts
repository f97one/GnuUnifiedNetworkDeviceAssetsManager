import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val flyway_version: String by project
val postgresql_version: String by project
val commons_cli_version: String by project
val h2_version: String by project
val hikari_version: String by project
val sql2o_version: String by project
val jbcrypt_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.72"
}

group = "net.formula97.webapps"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-thymeleaf:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    implementation("org.mindrot:jbcrypt:$jbcrypt_version")

    // data access
    //   flyway
    implementation("org.flywaydb:flyway-core:$flyway_version")
    //   PostgreSQL JDBC Driver
    implementation("org.postgresql:postgresql:$postgresql_version")
    //   H2 database
    implementation("com.h2database:h2:$h2_version")
    //   HikariCP
    implementation("com.zaxxer:HikariCP:$hikari_version")
    //   Sql2o
    implementation("org.sql2o:sql2o:$sql2o_version")
    implementation("org.sql2o.extensions:sql2o-postgres:$sql2o_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
