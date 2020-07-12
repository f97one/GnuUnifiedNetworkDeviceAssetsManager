package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition
import java.util.Date

/**
 * users
 */
@TableNameDefinition("app_user")
data class AppUser(
    /**
     * user id
     */
    @FieldMapDefinition(columnName = "user_id", ignoreWhenInsertion = true, isPrimaryKey = true)
    var userId: Int? = null,
    /**
     * login name
     */
    @FieldMapDefinition(columnName = "username")
    var username: String = "",
    /**
     * password
     */
    @FieldMapDefinition(columnName = "password")
    var password: String = "",
    /**
     * user name used for display
     */
    @FieldMapDefinition(columnName = "display_name")
    var displayName: String? = null,
    /**
     * email address for user
     */
    @FieldMapDefinition(columnName = "email")
    var email: String? = null,
    /**
     * Date and time when the password was last updated
     */
    @FieldMapDefinition(columnName = "passwd_last_modified")
    var passwdLastModified: Date? = null,
    /**
     * ID of the organization to which user belongs
     */
    @FieldMapDefinition(columnName = "joined_org_id")
    var joinedOrgId: Int? = null,
    /**
     * role id of user
     */
    @FieldMapDefinition(columnName = "role_id")
    var roleId: Int? = null

): BaseEntity() {
}