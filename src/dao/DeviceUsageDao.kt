package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.DeviceUsage

class DeviceUsageDao: AbstractDao<DeviceUsage>(dataSource = DataSourceCreator.getDataSource()) {
}