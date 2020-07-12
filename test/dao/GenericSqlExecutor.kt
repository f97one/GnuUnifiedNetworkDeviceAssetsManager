package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.ExecutionResult
import org.sql2o.Sql2o
import org.sql2o.Sql2oException
import javax.sql.DataSource

class GenericSqlExecutor(dataSource: DataSource) {
    private val sql2o: Sql2o = Sql2o(dataSource)

    fun executeRawSql(rawSql: String): ExecutionResult {
        val tran = sql2o.beginTransaction()

        return try {
            val affected = tran.createQuery(rawSql).executeUpdate().result
            tran.commit()
            ExecutionResult(success = true, affectedRows = affected)
        } catch (e: Sql2oException) {
            tran.rollback()
            ExecutionResult(success = false, causedException = e)
        }
    }

    fun executeRawSql(rawSql: List<String>): ExecutionResult {
        val tran = sql2o.beginTransaction()

        var affected = 0
        return try {
            for (s in rawSql) {
                affected += tran.createQuery(s).executeUpdate().result
            }
            tran.commit()
            ExecutionResult(success = true, affectedRows = affected)
        } catch (e: Sql2oException) {
            tran.rollback()
            ExecutionResult(success = false, causedException = e)
        }
    }
}