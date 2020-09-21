package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition
import java.util.*

@TableNameDefinition("device")
data class Device(
    @FieldMapDefinition(columnName = "device_id", isPrimaryKey = true)
    var deviceId: UUID? = UUID.randomUUID(),
    @FieldMapDefinition(columnName = "hostname")
    var hostname: String? = null,
    @FieldMapDefinition(columnName = "device_name")
    var deviceName: String = "",
    @FieldMapDefinition(columnName = "ip_address")
    var ipAddress: String? = null,
    @FieldMapDefinition(columnName = "os_id")
    var osId: Int? = null,
    @FieldMapDefinition(columnName = "os_version")
    var osVersion: String? = null,
    @FieldMapDefinition(columnName = "mem_size")
    var memSize: Long? = null,
    @FieldMapDefinition(columnName = "cpu_name")
    var cpuName: String? = null,
    @FieldMapDefinition(columnName = "disk_size")
    var diskSize: Long? = null,
    @FieldMapDefinition(columnName = "remarks")
    var remarks: String? = null,
    @FieldMapDefinition(columnName = "is_provisional")
    var isProvisional: Boolean? = true
): BaseEntity() {
}