package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.Authority

class AuthorityDao(): AbstractDao<Authority>(dataSource = DataSourceCreator.getDataSource()) {
    fun getAuthoritiesByUid(userId: Int): List<Authority> {
        val b = StringBuilder(basicSelectStatement(Authority::class.java))
            .append(" where authority_id in (select authority_id from user_authority where user_id = :userId)")

        val q = sql2o.open().createQuery(b.toString())
        q.columnMappings = Authority().getDefaultMapper()
        q.addParameter("userId", userId)

        return q.executeAndFetch(Authority::class.java)
    }
}