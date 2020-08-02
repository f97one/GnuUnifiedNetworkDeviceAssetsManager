package net.formula97.webapps.controller.form

data class LoginForm(
    var username: String = "",
    var password: String = ""
): ValidatorFunction {
    override fun validate(): ValidationResult {
        if (username.length > 32) {
            return ValidationResult(reason = "ユーザー名は32文字以下でなければなりません。")
        }
        if (password.length > 32) {
            return ValidationResult(reason = "パスワードは32文字以下でなければなりません。")
        }

        return ValidationResult(isValid = true)
    }
}