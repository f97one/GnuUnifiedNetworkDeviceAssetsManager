package net.formula97.webapps.controller.form

data class ValidationResult(val isValid: Boolean = false, val reason: String? = null) {
}