package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.AppUser
import java.lang.StringBuilder
import java.util.*

class AppUserDao(): AbstractDao<AppUser>(DataSourceCreator.getDataSource()) {
    fun loadByUsername(username: String): Optional<AppUser> {
        val b = StringBuilder(basicSelectStatement(AppUser::class.java))
        b.append(" where username = :username")

        val q = sql2o.open().createQuery(b.toString())
        q.columnMappings = AppUser().getDefaultMapper()
        q.addParameter("username", username)

        val ret = q.executeAndFetchFirst(AppUser::class.java) ?: return Optional.empty()
        return Optional.of(ret)
    }

}