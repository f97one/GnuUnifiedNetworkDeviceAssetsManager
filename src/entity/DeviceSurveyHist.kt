package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition
import java.time.Instant
import java.util.*

/**
 * デバイス調査履歴
 */
@TableNameDefinition("device_survey_hist")
data class DeviceSurveyHist(
    @FieldMapDefinition(columnName = "survey_id", isPrimaryKey = true, ignoreWhenInsertion = false)
    var surveyId: UUID? = null,
    @FieldMapDefinition(columnName = "surveyed_at")
    var surveyedAt: Date = Date.from(Instant.now()),
    @FieldMapDefinition(columnName = "deviceId")
    var deviceId: UUID? = null,
    @FieldMapDefinition(columnName = "hostname")
    var hostname: String? = null,
    @FieldMapDefinition(columnName = "device_name")
    var deviceName: String? = null,
    @FieldMapDefinition(columnName = "ip_address")
    var ipAddress: String? = null,
    @FieldMapDefinition(columnName = "os_version")
    var osVersion: String? = null,
    @FieldMapDefinition(columnName = "mem_size")
    var memSize: Long? = null,
    @FieldMapDefinition(columnName = "cpu_name")
    var cpuName: String? = null,
    @FieldMapDefinition(columnName = "disk_size")
    var diskSize: Long? = null,
    @FieldMapDefinition(columnName = "diskFreeSize")
    var diskFreeSize: Long? = null,
    @FieldMapDefinition(columnName = "survey_method")
    var surveyMethod: Int? = 1
): BaseEntity() {
    // todo surveyMethod のマジックナンバーを定数に変える
}