package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition

@TableNameDefinition("user_authority")
data class UserAuthority(
    @FieldMapDefinition(columnName = "authority_id", isPrimaryKey = true)
    var authorityId: Int? = null,
    @FieldMapDefinition(columnName = "user_id", isPrimaryKey = true)
    var userId: Int? = null
): BaseEntity() {
}