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
         * @return configured instance
         */
        @KtorExperimentalAPI
        fun fromConfig(conf: ApplicationConfig): DbConnectionConfig {
            val driverClassName = conf.propertyOrNull("settings.db.driverClassName")?.getString() ?: org.postgresql.Driver::class.java.name
            val jdbcUrl = conf.propertyOrNull("settings.db.jdbcUrl")?.getString() ?: "jdbc:postgresql://localhost:5432/klassify_db"
            val dbUser = conf.propertyOrNull("settings.db.dbUser")?.getString() ?: "klassify_user"
            val dbPasswd = conf.propertyOrNull("setings.db.dbPasswd")?.getString() ?: "klassify"

            return DbConnectionConfig(driverClassName, jdbcUrl, dbUser, dbPasswd)
        }
    }
}