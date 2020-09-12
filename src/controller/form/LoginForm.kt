package net.formula97.webapps.controller.form

import am.ik.yavi.builder.ValidatorBuilder
import am.ik.yavi.builder.konstraint
import am.ik.yavi.core.Validator
import io.ktor.http.*

data class LoginForm(
    var username: String = "",
    var password: String = ""
): ValidatorFunction<LoginForm>, ParameterImportable<LoginForm> {

    override fun importParams(params: Parameters): LoginForm {
        this.username = params["username"] ?: ""
        this.password = params["password"] ?: ""

        return this
    }

    override fun getValidator(): Validator<LoginForm> {
        return ValidatorBuilder.of<LoginForm>()
            .konstraint(LoginForm::username) {
                notBlank()
                    .greaterThan(0)
                    .lessThanOrEqual(32)
            }
            .konstraint(LoginForm::password) {
                notBlank()
                    .greaterThan(0)
                    .lessThanOrEqual(32)
            }
            .build()
    }

    override fun compileResult(): ValidationResult {
        return ValidationResult.fromViolations(getValidator().validate(this))
    }
}