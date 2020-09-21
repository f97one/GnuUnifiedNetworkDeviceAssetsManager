package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.Role

class RoleDao: AbstractDao<Role>(dataSource = DataSourceCreator.getDataSource()) {
}