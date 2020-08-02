package net.formula97.webapps.controller

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.thymeleaf.ThymeleafContent
import net.formula97.webapps.controller.form.LoginForm

fun Route.loginController() {
    route("/login") {
        get {
            call.respond(ThymeleafContent("login", mapOf("loginForm" to LoginForm())))
        }
        post {

        }
    }
}