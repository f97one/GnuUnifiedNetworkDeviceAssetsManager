package net.formula97.webapps.controller.form

import io.ktor.http.Parameters

data class LoginForm(
    var username: String = "",
    var password: String = ""
): ValidatorFunction, ParameterImportable<LoginForm> {
    override fun validate(): ValidationResult {
        if (username.length > 32 || username.isEmpty()) {
            return ValidationResult(reason = "ユーザー名は1文字以上32文字以下でなければなりません。")
        }
        if (password.length > 32 || password.isEmpty()) {
            return ValidationResult(reason = "パスワードは1文字以上32文字以下でなければなりません。")
        }

        return ValidationResult(isValid = true)
    }

    override fun importParams(params: Parameters): LoginForm {
        this.username = params["username"] ?: ""
        this.password = params["password"] ?: ""

        return this
    }
}