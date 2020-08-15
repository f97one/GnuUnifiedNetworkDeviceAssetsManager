package net.formula97.webapps.controller.page

import io.ktor.html.Template
import kotlinx.html.*

class FooterFragment: Template<DIV> {
    override fun DIV.apply() {
        footer(classes = "footter") {
            id = "mainFooter"
            div(classes = "row") {
                div(classes = "col-sm-4 col-lg-4") {
                    h4 { +"the Gnu Unified Network Device Assets Manager" }
                    p {
                        +"utilities for lifecycle management of owned devices"
                    }
                    ul(classes = "social-icon") {
                        // todo 各ソーシャルアイコンのリンク先を埋める
                        // Facebook
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-facebook") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                        // Twitter
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-twitter") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                        // Instagram
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-instagram") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                        // YouTube
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-youtube-play") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                        // Google
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-google") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                        // dribble
                        a(classes = "social") {
                            href = "#"
                            i(classes = "fa fa-dribble") {
                                attributes["aria-hidden"] = "true"
                            }
                        }
                    }
                }
                div(classes = "col-sm-4 col-lg-4") {
                    ul(classes = "list-group list-group-flush") {
                        li(classes = "list-group-item") {
                            a {
                                href = "#"
                                target = "_blank"
                                +"プライバシーポリシー"
                            }
                        }
                        li(classes = "list-group-item") {
                            a {
                                href = "#"
                                target = "_blank"
                                +"お問い合わせ"
                            }
                        }
                    }
                }
                div(classes = "col-sm-4 col-lg-4") {
                    // todo サイトマップを張る処理を書く
                    p {
                        +"サイトマップ"
                    }
                }
            }
            hr {}
            div(classes = "row") {
                p(classes = "text-center mx-auto") {
                    +"© 2021. HAJIME Fukuna (a.k.a. f97one)"
                }
            }
        }
    }
}