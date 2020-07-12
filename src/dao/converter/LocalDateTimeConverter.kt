package net.formula97.webapps.dao.converter

import org.sql2o.converters.Converter
import java.sql.Timestamp
import java.time.LocalDateTime

class LocalDateTimeConverter: Converter<LocalDateTime?> {
    override fun toDatabaseParam(value: LocalDateTime?): Any? {
        return if (value == null) null else Timestamp.valueOf(value)
    }

    override fun convert(value: Any?): LocalDateTime? {
        return if (value == null) null else (value as Timestamp).toLocalDateTime()
    }
}