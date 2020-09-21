package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition

@TableNameDefinition("operating_system")
data class OperatingSystem(
    @FieldMapDefinition(columnName = "os_id", isPrimaryKey = true, ignoreWhenInsertion = true)
    var osId: Int? = null,
    @FieldMapDefinition(columnName = "os_name")
    var osName: String = ""
): BaseEntity()