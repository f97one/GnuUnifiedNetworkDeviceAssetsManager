package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.Authority

class AuthorityDao(): AbstractDao<Authority>(dataSource = DataSourceCreator.getDataSource()) {
}