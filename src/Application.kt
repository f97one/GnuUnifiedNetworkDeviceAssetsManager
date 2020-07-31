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
import net.formula97.webapps.dao.AppUserDao
import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.dao.config.DbConnectionConfig
import org.flywaydb.core.Flyway

fun main(args: Array<String>): Unit = EngineMain.main(args)

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
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Authentication) {
        form("login") {
            skipWhen {call -> call.sessions.get<AuthSession>() != null}

            userParamName = "username"
            passwordParamName = "password"
            // todo 認証失敗時のリダイレクト先を決める
            challenge("/autherror")
            // todo DB認証の処理を書く
            validate { cred ->
                val userOpt = AppUserDao().loadByUsername(cred.name)
                if (userOpt.isPresent) {
                    val u = userOpt.get()
                    if (u.isPasswordValid(cred.password)) {
                        return@validate UserIdPrincipal(u.username)
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

