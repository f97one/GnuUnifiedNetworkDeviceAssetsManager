package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition

@TableNameDefinition("role")
data class Role(
    @FieldMapDefinition(columnName = "role_id", isPrimaryKey = true, ignoreWhenInsertion = true)
    var roleId: Int? = null,
    @FieldMapDefinition(columnName = "role_name")
    var roleName: String? = null,
    @FieldMapDefinition(columnName = "manager")
    var manager: Boolean? = false
): BaseEntity()