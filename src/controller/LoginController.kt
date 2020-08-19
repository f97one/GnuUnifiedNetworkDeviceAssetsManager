package net.formula97.webapps.controller

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.thymeleaf.*
import net.formula97.webapps.AppUserPrincipal
import net.formula97.webapps.CurrentUserSession
import net.formula97.webapps.controller.form.LoginForm

fun Route.loginController() {
    route("") {
        get("/login") {
            if (call.sessions.get<CurrentUserSession>() != null) {
                return@get call.respondRedirect("/dashboards")
            }

            val hasErr = call.parameters["error"] != null
            call.respond(ThymeleafContent("login", mapOf("loginForm" to LoginForm())))
        }
        get("/logout") {
            call.sessions.clear<CurrentUserSession>()
            call.respondRedirect("/")
        }
    }
    authenticate("login") {
        route("/login") {
            post {
                val principal = call.authentication.principal<AppUserPrincipal>()
                if (principal != null) {
                    call.sessions.set(principal.createSession())
                    // ダッシュボード画面へリダイレクト
                    call.respondRedirect("/dashboards")
                }
            }
        }
    }
}