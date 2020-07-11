package net.formula97.webapps.dao.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

/**
 * configuration and hold class for data source.
 */
object DataSourceCreator {
    private var ds: DataSource? = null

    /**
     * creates Data source.
     *
     * @param connectionConfig extracted connection configuration
     * @return configured DataSource instance
     */
    fun create(connectionConfig: DbConnectionConfig): DataSource {
        if (ds == null) {
            val config = HikariConfig().apply {
                driverClassName = connectionConfig.driverClassName
                jdbcUrl = connectionConfig.jdbcUrl
                username = connectionConfig.username
                password = connectionConfig.password
            }
            ds = HikariDataSource(config)
        }

        return ds!!
    }

    /**
     * returns pre-configured Data Source instance.
     *
     * @return pre-configured Data Source instance
     * @throws IllegalStateException when called if data source is not initialized
     */
    fun getDataSource(): DataSource {
        if (ds == null) {
            throw IllegalStateException("dataSource is not configured, call #create(DbConnectionConfig) first")
        }

        return ds!!
    }
}