package net.formula97.webapps.controller.form

import am.ik.yavi.core.Validator

interface ValidatorFunction<T> {
    /**
     * 構成済みバリデータを得る。
     *
     * @return 構成済みバリデータ
     */
    fun getValidator(): Validator<T>

    /**
     * バリデータによる検証結果を取りまとめる。
     *
     * @return とりまとめ結果
     */
    fun compileResult(): ValidationResult
}