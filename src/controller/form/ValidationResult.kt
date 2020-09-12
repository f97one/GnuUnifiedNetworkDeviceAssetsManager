package net.formula97.webapps.controller.form

import am.ik.yavi.core.ConstraintViolations

data class ValidationResult(
    val isValid: Boolean = false,
    val violationMessages: MutableMap<String, MutableList<String>> = mutableMapOf()
) {
    companion object {
        fun fromViolations(violations: ConstraintViolations): ValidationResult {
            val msgMap = mutableMapOf<String, MutableList<String>>()

            violations.forEach { r ->
                if (!msgMap.containsKey(r.name())) {
                    msgMap[r.name()] = ArrayList()
                }
                msgMap[r.name()]?.add(r.message())
            }

            return ValidationResult(isValid = violations.isValid, violationMessages = msgMap)
        }
    }
}