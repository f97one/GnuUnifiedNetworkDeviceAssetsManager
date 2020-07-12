package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.ExecutionResult
import net.formula97.webapps.dao.converter.LocalDateTimeConverter
import net.formula97.webapps.dao.converter.ZonedDateTimeConverter
import net.formula97.webapps.entity.BaseEntity
import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition
import org.sql2o.Sql2o
import org.sql2o.Sql2oException
import org.sql2o.converters.Converter
import org.sql2o.quirks.PostgresQuirks
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.sql.DataSource

/**
 * base DAO class.
 */
abstract class AbstractDao<T: BaseEntity>(dataSource: DataSource) {
    protected val sql2o: Sql2o

    init {
        // todo コンバータ定義をここに書く
        val converterMap: MutableMap<Class<*>, Converter<*>> = mutableMapOf()
        converterMap[LocalDateTime::class.java] = LocalDateTimeConverter()
        converterMap[ZonedDateTime::class.java] = ZonedDateTimeConverter(ZoneId.systemDefault())

        sql2o = Sql2o(dataSource, PostgresQuirks(converterMap))
    }

    /**
     * creates default SQL select clause.
     *
     * @param clz Entity Class to get default select clause
     * @param withBindChar true if insert bind character at the beginning, false otherwise, default is false
     * @param ignoreForInsertion true if the AUTO_INCREMENT column should be ignored when INSERT statement is built, false otherwise, default is false
     * @return comma-separated column names from Annotation FieldMapDefinition
     */
    fun defaultSelectionColumn(clz: Class<T>, ignoreForInsertion: Boolean = false, binderMode: Boolean = false, withBindChar: Char = ':'): String {
        val entity = clz.getDeclaredConstructor().newInstance()

        val lst = mutableListOf<String>()
        for (f in entity.javaClass.declaredFields) {
            val mapDef: FieldMapDefinition = f.getAnnotation(FieldMapDefinition::class.java)
            if (mapDef.columnName.trim().isEmpty()) {
                throw IllegalArgumentException("attribute columnName must not be empty : ${f.name}")
            }
            if (ignoreForInsertion && mapDef.ignoreWhenInsertion) {
                continue
            }

            val cn: String = if (binderMode) "${withBindChar}${f.name}" else mapDef.columnName
            lst.add(cn)
        }

        return lst.joinToString(separator = ", ")
    }

    /**
     * returns mapped table name.
     *
     * @param clz Entity Class to get declared table name
     * @return value defined at Annotation TableNameDefinition
     */
    fun mappedTableName(clz: Class<T>): String {
        val entity = clz.getDeclaredConstructor().newInstance()

        val tnd: TableNameDefinition = entity.javaClass.getAnnotation(TableNameDefinition::class.java)
            ?: throw IllegalArgumentException("Annotation TableNameDefinition required")
        if (tnd.value.trim().isEmpty()) {
            throw IllegalArgumentException("table name must not be empty")
        }

        return tnd.value
    }

    /**
     * creates default SELECT statement from declared annotations. like this:
     *
     * ```sql
     * select section, pref_name, pref_value, pref_type from preference
     * ```
     */
    fun basicSelectStatement(clz: Class<T>): String {
        val b = StringBuilder()
        b.append("select ").append(defaultSelectionColumn(clz))
            .append(" from ").append(mappedTableName(clz))

        return b.toString()
    }

    /**
     * perform INSERT/UPDATE/DELETE statements.
     *
     * @param stmt SQL statement to perform
     * @param entity to INSERT/UPDATE/DELETE
     * @param ignoreForInsertion true if containing columns (such as AUTO INCREMENT) that want to ignore, false otherwise
     */
    internal fun performUpdate(stmt: String, entity: T, ignoreForInsertion: Boolean): ExecutionResult {
        val tran = sql2o.beginTransaction()
        return try {
            val q = tran.createQuery(stmt)
            q.columnMappings = entity.getDefaultMapper(ignoreForInsertion)
            for ((k, v) in entity.getBindValues(ignoreForInsertion = ignoreForInsertion)) {
                q.addParameter(k, v)
            }
            val rows = q.executeUpdate().result

            tran.commit()

            ExecutionResult(success = true, affectedRows = rows)
        } catch (e: Sql2oException) {
            e.printStackTrace()
            tran.rollback()
            ExecutionResult(success = false, causedException = e)
        }
    }

    internal fun primaryKeySelectionClause(clz: Class<T>): String {
        val entity = clz.getDeclaredConstructor().newInstance()

        val fields = entity.javaClass.declaredFields
        val b = StringBuilder(" where ")
        var hasMany = false

        for (f in fields) {
            val mapDef: FieldMapDefinition = f.getAnnotation(FieldMapDefinition::class.java)

            if (!mapDef.isPrimaryKey) {
                continue
            }

            if (mapDef.columnName.trim().isEmpty()) {
                throw IllegalArgumentException("attribute columnName must not be empty : ${f.name}")
            }

            if (hasMany) {
                b.append(" and ")
            }

            b.append(mapDef.columnName).append(" = :").append(f.name)

            hasMany = true
        }

        return b.toString()
    }

    /**
     * creates new record.
     *
     * @param entity entity to insert
     * @return result object of SQL execution
     */
    fun createOne(entity: T): ExecutionResult {
        val b = StringBuilder()
        b.append("insert into ").append(mappedTableName(entity.javaClass))
            .append(" (").append(defaultSelectionColumn(entity.javaClass, ignoreForInsertion = true)).append(") ")
            .append("values (").append(defaultSelectionColumn(entity.javaClass,
                ignoreForInsertion = true, binderMode = true
            )).append(")")

        return performUpdate(b.toString(), entity, true)
    }

    /**
     * finds one record by primary key search.
     *
     * @param entity entity for finding by primary key
     * @return result entity wrapped in java.util.Optional, if no result, returns Optional.empty()
     */
    fun findByKey(entity: T): Optional<T> {
        val b = StringBuilder(basicSelectStatement(entity.javaClass)).append(primaryKeySelectionClause(entity.javaClass))

        val conn = sql2o.open()
        val q = conn.createQuery(b.toString())
        q.columnMappings = entity.getDefaultMapper()
        for((k, v) in entity.getBindValues(extractPrimaryKeyOnly = true)) {
            q.addParameter(k, v)
        }

        val ret = q.executeAndFetchFirst(entity.javaClass) ?: return Optional.empty()
        return Optional.of(ret)
    }

    /**
     * finds all records.
     *
     * @return list of all records. if no record found, returns empty list.
     */
    fun findAll(clz: Class<T>): List<T> {
        val conn = sql2o.open()

        val q = conn.createQuery(basicSelectStatement(clz))

        val entity = clz.getDeclaredConstructor().newInstance()
        q.columnMappings = entity.getDefaultMapper()

        return q.executeAndFetch(clz)
    }

    /**
     * updates existing record by passed primary key condition.
     *
     * @param entity entity to update
     * @return result object of SQL execution
     */
    fun updateOneByPK(entity: T): ExecutionResult {
        val b1 = StringBuilder()
        b1.append("update ").append(mappedTableName(entity.javaClass))
            .append("set ")
        for ((k, v) in entity.getDefaultMapper()) {
            b1.append(v).append(" = ").append(k).append(",")
        }
        val tmp = b1.toString()
        val b2 = StringBuilder(tmp.replaceAfterLast(",", ""))
        b2.append(primaryKeySelectionClause(entity.javaClass))

        return performUpdate(b2.toString(), entity, false)
    }

    /**
     * deletes existing records by passed primary key condition.
     *
     * @param entity entity to delete
     * @return result object of SQL execution
     */
    fun deleteOneByPK(entity: T): ExecutionResult {
        val b = StringBuilder()
        b.append("delete from ").append(mappedTableName(entity.javaClass))
            .append(primaryKeySelectionClause(entity.javaClass))

        return performUpdate(b.toString(), entity, false)
    }

    /**
     * deletes all existing records UNCONDITIONALLY.
     *
     * @return result object of SQL execution
     */
    fun deleteAll(clz: Class<T>): ExecutionResult {
        val b = StringBuilder()
        b.append("delete from ").append(mappedTableName(clz))

        return performUpdate(b.toString(), clz.getDeclaredConstructor().newInstance(), false)
    }
}