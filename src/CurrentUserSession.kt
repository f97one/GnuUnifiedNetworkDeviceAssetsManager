package net.formula97.webapps

import net.formula97.webapps.dao.AppUserDao
import net.formula97.webapps.entity.AppUser

data class CurrentUserSession(
    val userId: Int,
    val username: String,
    val displayName: String?
) {
    fun readUser(): AppUser {
        return AppUserDao().findByKey(AppUser(userId = userId)).get()
    }
}