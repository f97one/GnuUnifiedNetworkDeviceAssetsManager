package net.formula97.webapps

import net.formula97.webapps.entity.AppUser
import io.ktor.auth.Principal

data class AppUserPrincipal(val currentUser: AppUser?): Principal {
    fun createSession(): CurrentUserSession {
        return CurrentUserSession(currentUser!!.userId!!, currentUser.username, currentUser.displayName)
    }
}