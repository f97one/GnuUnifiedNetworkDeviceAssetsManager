package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.AppUser

class AppUserDao(): AbstractDao<AppUser>(DataSourceCreator.getDataSource()) {

}