package net.formula97.webapps.controller.page

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*

class StdPageTemplate(
    private val pageTitle: String,
    private val headFragment: HeadFragment = HeadFragment(pageTitle),
    private val menuFragment: StickyMenuFragment = StickyMenuFragment(),
    private val footer: FooterFragment = FooterFragment()
) : Template<HTML> {

    val body = Placeholder<DIV>()

    override fun HTML.apply() {
        head {
            insert(headFragment) {}
        }
        body {
            div(classes = "container") {
                insert(menuFragment) {}
                insert(body)
                insert(footer) {}
            }
        }
    }
}