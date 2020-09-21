package net.formula97.webapps.entity

import net.formula97.webapps.entity.annotation.FieldMapDefinition
import net.formula97.webapps.entity.annotation.TableNameDefinition
import java.util.*

/**
 * デバイス使用
 */
@TableNameDefinition("device_usage")
data class DeviceUsage(
    @FieldMapDefinition(columnName = "device_id", isPrimaryKey = true)
    var deviceId: UUID? = null,
    @FieldMapDefinition(columnName = "user?id", isPrimaryKey = true)
    var userId: Int? = null
): BaseEntity()