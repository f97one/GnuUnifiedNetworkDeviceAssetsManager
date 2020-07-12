package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

abstract class BaseEntity {

    /**
     * creates default column mapping strategy from Annotation FieldMapDefinition.
     *
     * @param buildForInsertion true if the AUTO_INCREMENT column should be ignored when INSERT statement is built, false otherwise
     * @return default column mapping strategy of this class
     */
    fun getDefaultMapper(buildForInsertion: Boolean = false): Map<String, String> {
        val fieldMap: MutableMap<String, String> = mutableMapOf()
        val fields = this.javaClass.declaredFields
        for (f in fields) {
            f.isAccessible = true
            val mapDef: FieldMapDefinition = f.getAnnotation(FieldMapDefinition::class.java)
            if (mapDef.columnName.trim().isEmpty()) {
                throw IllegalArgumentException("attribute columnName must not be empty : ${f.name}")
            }
            if (buildForInsertion && mapDef.ignoreWhenInsertion) {
                // skip when both flag is raised
                continue
            }
            fieldMap[f.name] = mapDef.columnName
        }

        return fieldMap
    }

    /**
     * gets bind values pair as Dictionary.
     *
     * @param bindPrefix prefix of bind field, default is `:`
     * @param extractPrimaryKeyOnly true if limit to primary key values only, false otherwise, default is false
     * @return pair of bind field and its value
     */
    fun getBindValues(bindPrefix: Char = ':', extractPrimaryKeyOnly: Boolean = false): Map<String, KClass<*>?> {
        val fieldMap = mutableMapOf<String, KClass<*>?>()
        this::class.memberProperties.filter { p ->
            p.isAccessible = true
            val mapDef: FieldMapDefinition? = p.findAnnotation()
            return@filter mapDef != null
        }.filter { p ->
            p.isAccessible = true
            val mapDef: FieldMapDefinition? = p.findAnnotation()
            return@filter !(extractPrimaryKeyOnly && !mapDef!!.isPrimaryKey)
        }.forEach { p ->
            p.isAccessible = true
            val mapDef: FieldMapDefinition? = p.findAnnotation()

            fieldMap["${bindPrefix}${p.name}"] = if (p.getter.call(this) == null) null else p.getter.call(this) as KClass<*>
        }

        return fieldMap
    }
}