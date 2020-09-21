package net.formula97.webapps

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.thymeleaf.*
import io.ktor.util.*
import kotlinx.html.*
import net.formula97.webapps.controller.loginController
import net.formula97.webapps.controller.page.StdPageTemplate
import net.formula97.webapps.controller.dashboardController
import net.formula97.webapps.dao.AppUserDao
import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.dao.config.DbConnectionConfig
import org.flywaydb.core.Flyway
import org.slf4j.event.Level
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun main(args: Array<String>): Unit = EngineMain.main(args)

const val USER_SESSION_KEY = "USER_SESSION_KEY"

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    val conf = environment.config
    initDatabase(DbConnectionConfig.fromConfig(conf, testing))

    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    install(Sessions) {
        val secretHash = hex("998ed5c9957e79ab8d9a4bbd568db3570b900d854360cc7eec8d1e612b816a3a" +
                                "f7041ccb21e09cb2ca4220feb66d251258727efc27c9a31e1acb33389a8d8047" +
                                "e2b56415abc0ba67c25d7dba033479077ea8350413208dfdc8c9ef2a70a5fd6d" +
                                "775664000b0b76dffbcded738594b8238f6a463187c74b850171b7757c86acec")

        cookie<CurrentUserSession>(USER_SESSION_KEY, SessionStorageMemory()) {
            cookie.extensions["SameSite"] = "lax"
            cookie.path = "/"
            transform(SessionTransportTransformerMessageAuthentication(key = secretHash))
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Authentication) {
        form("login") {
            skipWhen {call -> call.sessions.get<CurrentUserSession>() != null}

            userParamName = "username"
            passwordParamName = "password"

            // 認証失敗時のリダイレクト先をURLで振り分ける
            challenge {
                val path = context.request.path()
                if (path.contains("/login")) {
                    context.respondRedirect("/login?error")
                } else {
                    context.respondHtmlTemplate(StdPageTemplate("認証エラー", null), HttpStatusCode.Unauthorized) {
                        body {
                            h2(classes = "h2") {
                                +"認証エラー"
                            }
                            div(classes = "row") {
                                div(classes = "col-lg-12 alert alert-warning") {
                                    +"あなたが開こうとしたページは許可されていません。"
                                }
                            }
                            div(classes = "row") {
                                div(classes = "col-lg-12") {
                                    p(classes = "text-center mx-auto") {
                                        a(href = "/") {
                                            +"トップページへ"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            validate { cred ->
                val userOpt = AppUserDao().loadByUsername(cred.name)
                if (userOpt.isPresent) {
                    val u = userOpt.get()
                    if (u.isPasswordValid(cred.password)) {
                        return@validate AppUserPrincipal(u)
                    }
                }
                return@validate null
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        loginController()
        dashboardController()

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
            files("js")
            files("css")
        }
    }
}

fun initDatabase(connectionConfig: DbConnectionConfig) {
    val ds = DataSourceCreator.create(connectionConfig)

    val flyway = Flyway.configure()
        .dataSource(ds)
        .baselineOnMigrate(true)
        .load()
    flyway.migrate()
}
