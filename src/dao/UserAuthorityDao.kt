package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.UserAuthority

class UserAuthorityDao: AbstractDao<UserAuthority>(dataSource = DataSourceCreator.getDataSource()) {
}