package net.formula97.webapps.dao.config

import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI

/**
 * Database connection configuration object.
 */
data class DbConnectionConfig(
    /**
     * JDBC driver class name using connection
     */
    val driverClassName: String = org.postgresql.Driver::javaClass.name,
    /**
     * JDBC connection URL
     */
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/g-u-n-d-a-m-db",
    /**
     * JDBC connection user's name
     */
    val username: String = "g-u-n-d-a-m-user",
    /**
     * JDBC connection user's password
     */
    val password: String = "g-u-n-d-a-m-user"
) {
    companion object {
        /**
         * creates instance from Application config
         *
         * @param conf ApplicationConfig from static configuration file
         * @param withTestEnv if true, force database connection setting using with H2 (for testing)
         * @return configured instance
         */
        @KtorExperimentalAPI
        fun fromConfig(conf: ApplicationConfig, withTestEnv: Boolean = false): DbConnectionConfig {
            val driverClassName: String
            val jdbcUrl: String
            val dbUser: String
            val dbPasswd: String

            if (withTestEnv) {
                driverClassName = org.h2.Driver::class.java.name
                jdbcUrl = "jdbc:h2:mem:g-u-n-d-a-m-db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
                dbUser = "SA"
                dbPasswd = ""
            } else {
                driverClassName = conf.propertyOrNull("settings.db.driverClassName")?.getString() ?: org.postgresql.Driver::class.java.name
                jdbcUrl = conf.propertyOrNull("settings.db.jdbcUrl")?.getString() ?: "jdbc:postgresql://localhost:5432/g-u-n-d-a-m-db"
                dbUser = conf.propertyOrNull("settings.db.dbUser")?.getString() ?: "g-u-n-d-a-m-user"
                dbPasswd = conf.propertyOrNull("settings.db.dbPasswd")?.getString() ?: "g-u-n-d-a-m-user"
            }

            return DbConnectionConfig(driverClassName, jdbcUrl, dbUser, dbPasswd)
        }
    }
}