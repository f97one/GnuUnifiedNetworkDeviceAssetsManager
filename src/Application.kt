package net.formula97.webapps

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.thymeleaf.Thymeleaf
import io.ktor.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.sessions.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import net.formula97.webapps.controller.loginController
import net.formula97.webapps.dao.AppUserDao
import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.dao.config.DbConnectionConfig
import org.flywaydb.core.Flyway

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
            // todo 認証失敗時のリダイレクト先を決める
            challenge("/login?error")
            // todo DB認証の処理を書く
            validate { cred ->
                val userOpt = AppUserDao().loadByUsername(cred.name)
                if (userOpt.isPresent) {
                    val u = userOpt.get()
                    if (u.isPasswordValid(cred.password)) {
                        return@validate AppUserPrincipal(u)
                    } else {
                        return@validate null
                    }
                } else {
                    return@validate null
                }
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
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/html-thymeleaf") {
            call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
            files("js")
            files("css")
        }

        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
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

data class ThymeleafUser(val id: Int, val name: String)

data class MySession(val count: Int = 0)

