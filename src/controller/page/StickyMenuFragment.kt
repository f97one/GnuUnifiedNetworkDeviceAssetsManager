package net.formula97.webapps.controller.page

import io.ktor.html.*
import kotlinx.html.*
import net.formula97.webapps.CurrentUserSession

class StickyMenuFragment(private val session: CurrentUserSession?): Template<DIV> {
    override fun DIV.apply() {
        header(classes = "sticky-top") {
            nav(classes = "navbar navbar-expand-lg navbar-light bg-light") {
                a(classes = "navbar-brand", href = "#") {
                    +"the Gnu Unified Network Device Assets Manager"
                }
                button(classes = "navbar-toggler", type = ButtonType.button) {
                    attributes["data-toggle"] = "collapse"
                    attributes["data-target"] = "#navbarSupportedContent"
                    attributes["aria-controls"] = "navbarSupportedContent"
                    attributes["aria-expanded"] = "false"
                    attributes["aria-label"] = "Toggle navigation"
                    span(classes = "navbar-toggler-icon") {}
                }

                div(classes = "collapse navbar-collapse") {
                    id = "navbarSupportedContent"

                    ul(classes = "navbar-nav mr-auto") {
                        li(classes = "nav-item active") {
                            a(classes = "nav-link", href = "/dashboards") {
                                +"Home"
                                span(classes = "sr-only") {
                                    +"(Current)"
                                }
                            }
                        }
                        li(classes = "nav-item") {
                            a(classes = "nav-link", href = "#") {
                                +"Link"
                            }
                        }
                        li(classes = "nav-item") {
                            a(classes = "nav-link disabled", href = "#") {
                                +"Disabled"
                            }
                        }
                    }

                    // 画面右へ
                    ul(classes ="navbar-nav") {
                        if (session == null) {
                            li(classes = "nav-iyem") {
                                a(classes = "nav-link", href = "/login") {
                                    +"ログイン"
                                }
                            }
                        } else {
                            val dispName = session.displayName ?: session.username
                            li(classes = "nav-link dropdown-right") {
                                a(classes = "nav-link dropdown-toggle", href = "#") {
                                    id = "navbarDropdown"
                                    attributes["role"] = "button"
                                    attributes["data-toggle"] = "dropdown"
                                    attributes["aria-haspopup"] = "true"
                                    attributes["aria-expanded"] = "false"
                                    +dispName
                                }
                                div(classes = "dropdown-menu dropdown-menu-right") {
                                    attributes["aria-labelledby"] = "navbarDropdown"
                                    a(classes = "dropdown-item", href = "#") {
                                        +"設定"
                                    }
                                    div(classes = "dropdown-divider") {}
                                    a(classes = "dropdown-item", href = "/logout") {
                                        +"ログアウト"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}