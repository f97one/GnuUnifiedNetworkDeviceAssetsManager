package net.formula97.webapps.controller

import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.thymeleaf.ThymeleafContent
import net.formula97.webapps.controller.form.LoginForm
import net.formula97.webapps.dao.AppUserDao

@Suppress("UNCHECKED_CAST")
fun Route.loginController() {
    route("/login") {
        get {
            call.respond(ThymeleafContent("login", mapOf("loginForm" to LoginForm())))
        }
        post {
            val loginForm = LoginForm().importParams(call.receiveParameters())
            val validatorResult = loginForm.validate()

            if (validatorResult.isValid) {
                val userOpt = AppUserDao().loadByUsername(loginForm.username)
                if (userOpt.isEmpty) {
                    call.respond(ThymeleafContent("login",
                        mapOf("loginForm" to loginForm, "msg" to "ユーザー名、またはパスワードが違います。")
                    ))
                } else {
                    val appUser = userOpt.get()
                    if (appUser.isPasswordValid(loginForm.password)) {
                        // todo ダッシュボード画面へリダイレクトさせる処理を書く
                    } else {
                        call.respond(ThymeleafContent("login",
                            mapOf("loginForm" to loginForm, "msg" to "ユーザー名、またはパスワードが違います。")
                        ))
                    }
                }
            } else {
                call.respond(ThymeleafContent("login",
                    mapOf("loginForm" to loginForm, "msg" to validatorResult.reason) as Map<String, Any>
                ))
            }
        }
    }
}