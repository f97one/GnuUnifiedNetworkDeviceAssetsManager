package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition

@TableNameDefinition("authority")
data class Authority(
    @FieldMapDefinition(columnName = "authority_id", isPrimaryKey = true, ignoreWhenInsertion = true)
    var authorityId: Int? = null,
    @FieldMapDefinition(columnName = "authority_name")
    var authorityName: String = "",
    @FieldMapDefinition(columnName = "admin_role")
    var adminRole: Boolean? = false
): BaseEntity()