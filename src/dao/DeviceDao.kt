package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.Device

class DeviceDao(): AbstractDao<Device>(dataSource = DataSourceCreator.getDataSource()) {
}