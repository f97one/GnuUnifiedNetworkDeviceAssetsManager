package net.formula97.webapps.entity.annotation

/**
 * Annotation for definition of table name.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TableNameDefinition(
    /**
     * table name value
     */
    val value: String
)