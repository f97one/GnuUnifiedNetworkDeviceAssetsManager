package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition

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
            fieldMap[mapDef.columnName] = f.name
        }

        return fieldMap
    }

    /**
     * gets bind values pair as Dictionary.
     *
     * @param extractPrimaryKeyOnly true if limit to primary key values only, false otherwise, default is false
     * @param ignoreForInsertion true if the AUTO_INCREMENT column should be ignored when INSERT statement is built, false otherwise
     * @return pair of bind field and its value
     */
    fun getBindValues(extractPrimaryKeyOnly: Boolean = false, ignoreForInsertion: Boolean = false): Map<String, Any?> {
        val fieldMap = mutableMapOf<String, Any?>()

        for (f in this::class.java.declaredFields) {
            f.isAccessible = true
            val mapDef = f.getAnnotation(FieldMapDefinition::class.java) as FieldMapDefinition ?: continue
            if (extractPrimaryKeyOnly && !mapDef.isPrimaryKey) {
                continue
            }
            if (ignoreForInsertion && mapDef.ignoreWhenInsertion) {
                continue
            }

            fieldMap[f.name] = f.get(this)
        }

        return fieldMap
    }
}