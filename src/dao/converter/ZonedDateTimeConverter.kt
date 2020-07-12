package net.formula97.webapps.dao.converter

import org.sql2o.converters.Converter
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime

class ZonedDateTimeConverter(private val zoneId: ZoneId) : Converter<ZonedDateTime> {
    override fun toDatabaseParam(value: ZonedDateTime?): Any? {
        return if (value == null) null else Timestamp(value.toInstant().toEpochMilli())
    }

    override fun convert(value: Any?): ZonedDateTime? {
        return if (value == null) null else ZonedDateTime.ofInstant((value as Timestamp).toInstant(), zoneId)
    }

}