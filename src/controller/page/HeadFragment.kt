package net.formula97.webapps.controller.page

import io.ktor.html.Template
import kotlinx.html.*

class HeaderFragment(val pageTitle: String): Template<HEAD> {
    override fun HEAD.aplly() {
        title { +"$pageTitle | the Gnu Unified Network Device Assets Manager"}
        meta {
            charset = "UTF-8"
        }
        meta {
            name = "viewport"
            content = "width-device-width, initial-scale=1, shrink-to-fit=no"
        }
        // Bootstrap 4
        link {
            rel = "stylesheet"
            href = "/static/css/bootstrap.min.css"
        }
        script {
            type = "text/javascript"
            src = "/static/js/jquery-3.5.1.min.js"
        }
        // jQuery 3.5
        script {
            type = "text/javascript"
            src = "/static/js/bootstrap.bundle.min.js"
        }
        // Font Awesome
        // todo Font Awesome のソースを更新する
        // script {
        //     type = "text/javascript"
        //     src = "https://kit.fontawesome.com/3abfbb1566.js"
        //     attributes["crossorigin"] = "anonymous"
        // }
        // site local css
        link {
            rel = "stylesheet"
            href = "/static/css/main.css"
        }
    }
}
