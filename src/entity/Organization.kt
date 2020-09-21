package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition

@TableNameDefinition("organization")
data class Organization(
    @FieldMapDefinition(columnName = "org_id", isPrimaryKey = true, ignoreWhenInsertion = true)
    var orgId: Int? = null,
    @FieldMapDefinition(columnName = "ancestor_org_id")
    var ancestorOrgId: Int? = null,
    @FieldMapDefinition(columnName = "org_name")
    var org_name: String? = null
): BaseEntity()