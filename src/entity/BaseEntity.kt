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
    fun getBindValues(bindPrefix: Char = ':', extractPrimaryKeyOnly: Boolean = false): Map<String, Class<*>> {
        val fieldMap: MutableMap<String, Class<*>> = mutableMapOf()
        val fields = this.javaClass.declaredFields
        for (f in fields) {
            f.isAccessible = true

            val mapDef: FieldMapDefinition = f.getAnnotation(FieldMapDefinition::class.java)
            if (extractPrimaryKeyOnly && !mapDef.isPrimaryKey) {
                continue
            }

            fieldMap["${bindPrefix}${f.name}"] = f.get(this) as Class<*>
        }

        return fieldMap
    }
}