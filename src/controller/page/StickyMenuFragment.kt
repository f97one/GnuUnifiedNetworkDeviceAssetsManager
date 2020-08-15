package net.formula97.webapps.controller.page

import io.ktor.html.Template
import kotlinx.html.*

class StickyMenuFragment: Template<DIV> {
    override fun DIV.apply() {
        header(classes = "sticky-top") {
            nav(classes = "navbar navbar-expand-lg navbar-light bg-light") {
                a(classes = "navbar-brand", href = "#") {
                    +"Navbar"
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
                            a(classes = "nav-link", href = "#") {
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
                        li(classes = "nav-link dropdown") {
                            a(classes = "nav-link dropdown-toggle", href = "#") {
                                id = "navbarDropdown"
                                attributes["role"] = "button"
                                attributes["data-toggle"] = "dropdown"
                                attributes["aria-haspopup"] = "true"
                                attributes["aria-expanded"] = "false"
                                +"Dropdown"
                            }
                            div(classes = "dropdown-menu") {
                                attributes["aria-labelledby"] = "navbarDropdown"
                                a(classes = "dropdown-item", href = "#") {
                                    +"Action"
                                }
                                a(classes = "dropdown-item", href = "#") {
                                    +"Another action"
                                }
                                div(classes = "dropdown-divider") {}
                                a(classes = "dropdown-item", href = "#") {
                                    +"Something else here"
                                }
                            }
                        }
                        li(classes = "nav-item") {
                            a(classes = "nav-link disabled", href = "#") {
                                +"Disabled"
                            }
                        }
                    }

                    form(classes = "form-inline my-2 my-lg-0") {
                        input(type = InputType.search, classes = "form-control mr-sm-2") {
                            attributes["placeholder"] = "Search"
                            attributes["aria-label"] = "Search"
                        }
                        button(type = ButtonType.submit, classes = "btn btn-outline-success my-2 my-sm-0") {
                            +"Search"
                        }
                    }
                }
            }
        }
    }
}