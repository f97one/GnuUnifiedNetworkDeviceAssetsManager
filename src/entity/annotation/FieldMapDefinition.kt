package net.formula97.webapps.entity.annotation

/**
 * Annotation for construction of default mapping from relations.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldMapDefinition(
    /**
     * physical name of column you want to map.
     *
     * must not be empty or white space.
     */
    val columnName: String,
    /**
     * ignores column if insertion.
     *
     * default is false (=not ignore)
     */
    val ignoreWhenInsertion: Boolean = false,
    /**
     * marks as Primary key.
     *
     * default is false (=not marked)
     */
    val isPrimaryKey: Boolean = false
)