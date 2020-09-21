package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.Organization

class OrganizationDao: AbstractDao<Organization>(dataSource = DataSourceCreator.getDataSource()) {
}