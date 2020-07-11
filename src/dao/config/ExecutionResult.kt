package net.formula97.webapps.dao.config

/**
 * data class to hold SQL execution results.
 */
data class ExecutionResult(
    /**
     * true if statement has ended successfully, false otherwise
     */
    val success: Boolean = false,
    /**
     * number of affected rows
     */
    val affectedRows: Int = 0,
    /**
     * exceptions object when occurred any problem
     */
    val causedException: RuntimeException? = null
) {
}