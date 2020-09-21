package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.OperatingSystem

class OperatingSystemDao: AbstractDao<OperatingSystem>(dataSource = DataSourceCreator.getDataSource()) {
}