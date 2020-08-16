val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val flywayVersion: String by project
val postgresqlVersion: String by project
val commonsCliVersion: String by project
val h2Version: String by project
val hikariVersion: String by project
val sql2oVersion: String by project
val jbcryptVersion: String by project

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-thymeleaf:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    implementation("org.mindrot:jbcrypt:$jbcryptVersion")

    // data access
    //   flyway
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    //   PostgreSQL JDBC Driver
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    //   H2 database
    implementation("com.h2database:h2:$h2Version")
    //   HikariCP
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    //   Sql2o
    implementation("org.sql2o:sql2o:$sql2oVersion")
    implementation("org.sql2o.extensions:sql2o-postgres:$sql2oVersion")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
