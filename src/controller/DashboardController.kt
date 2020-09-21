package net.formula97.webapps.controller

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.div
import kotlinx.html.h2
import net.formula97.webapps.CurrentUserSession
import net.formula97.webapps.controller.page.StdPageTemplate

fun Route.dashboardController() {
    route("") {
        get("/") {
            val session = call.sessions.get<CurrentUserSession>()
            val sendTo =  if (session == null) "/login" else "/dashboards"
            call.respondRedirect(sendTo)
        }
        authenticate("login") {
            get("/dashboards") {
                val session = call.sessions.get<CurrentUserSession>()
                call.respondHtmlTemplate(template = StdPageTemplate("Dashboards", session)) {
                    body {
                        h2(classes = "h2") {
                            +"ダッシュボード"
                        }

                        div(classes = "row") {
                            +"todo ダッシュボードに表示する内容を決める"
                        }
                    }
                }
            }
        }
    }
}