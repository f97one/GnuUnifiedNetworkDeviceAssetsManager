package net.formula97.webapps.controller

import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.thymeleaf.ThymeleafContent
import net.formula97.webapps.AppUserPrincipal
import net.formula97.webapps.controller.form.LoginForm
import net.formula97.webapps.dao.AppUserDao

fun Route.loginController() {
    route {
        get("/login") {
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
                val principal = call.authenticate.principal<AppUserPrincipal>()
                if (principal != null) {
                    call.sessions.set(principal.createSession())
                    // todo リダイレクト先を決める
                    call.respondRedirect("/dashboards")
                }
            }
        }
    }
}