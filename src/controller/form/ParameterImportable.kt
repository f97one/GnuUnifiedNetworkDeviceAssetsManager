package net.formula97.webapps.controller.form

import io.ktor.http.Parameters

interface ParameterImportable<T> {
    fun importParams(params: Parameters): T
}