package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.dao.config.DbConnectionConfig
import net.formula97.webapps.entity.AppUser
import org.flywaydb.core.Flyway
import org.junit.After
import org.junit.Before
import java.sql.Date
import java.time.Instant
import javax.sql.DataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppUserDaoTest {

    private lateinit var ds: DataSource

    @Before
    fun setUp() {
        val connConf = DbConnectionConfig(
            driverClassName = org.h2.Driver::class.java.name,
            jdbcUrl = "jdbc:h2:mem:g-u-n-d-a-m-db;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
            username = "SA",
            password = ""
        )
        ds = DataSourceCreator.create(connConf)
        val flyway = Flyway.configure()
            .dataSource(ds)
            .baselineOnMigrate(false)
            .load()
        flyway.migrate()
    }

    @After
    fun tearDown() {
        val exec = GenericSqlExecutor(ds)

        val delAll = "delete from app_user"
        exec.executeRawSql(delAll)
        val resetSeq = "select setval ('app_user_user_id_seq', 1, false)"
        exec.executeRawSql(resetSeq)
    }

    @Test
    fun createsDefaultSelectionColumns1() {
        val actual = AppUserDao().defaultSelectionColumn(AppUser::class.java)
        assertEquals("user_id, username, password, display_name, email, passwd_last_modified, joined_org_id, role_id", actual)
    }

    @Test
    fun createsDefaultSelectionColumns2() {
        val actual = AppUserDao().defaultSelectionColumn(clz = AppUser::class.java, ignoreForInsertion = true)
        assertEquals("username, password, display_name, email, passwd_last_modified, joined_org_id, role_id", actual)
    }

    @Test
    fun createsDefaultSelectionColumns3() {
        val actual = AppUserDao().defaultSelectionColumn(AppUser::class.java, binderMode = true)
        assertEquals(":userId, :username, :password, :displayName, :email, :passwdLastModified, :joinedOrgId, :roleId", actual)
    }

    @Test
    fun createsDefaultSelectionColumns4() {
        val actual = AppUserDao().defaultSelectionColumn(AppUser::class.java, binderMode = true, withBindChar = '#')
        assertEquals("#userId, #username, #password, #displayName, #email, #passwdLastModified, #joinedOrgId, #roleId", actual)
    }

    @Test
    fun createsDefaultSelectionColumns5() {
        val actual = AppUserDao().defaultSelectionColumn(AppUser::class.java, ignoreForInsertion = true, binderMode = true, withBindChar = '#')
        assertEquals("#username, #password, #displayName, #email, #passwdLastModified, #joinedOrgId, #roleId", actual)
    }

    @Test
    fun createsBasicSelectStatement() {
        val actual = AppUserDao().basicSelectStatement(AppUser::class.java)
        assertEquals("select user_id, username, password, display_name, email, passwd_last_modified, joined_org_id, role_id from app_user", actual)
    }

    @Test
    fun addsNewUser() {
        val curTime = Date.from(Instant.now())
        val appUser = AppUser(username = "testsan", password = "password", displayName = "てすとさん", email = "testsan@example.com", passwdLastModified = curTime, joinedOrgId = null, roleId = null)

        val result = AppUserDao().createOne(appUser)
        assertTrue(result.success)
        assertEquals(1, result.affectedRows)
        assertNull(result.causedException)

        val users = AppUserDao().findAll(AppUser::class.java)
        assertEquals(1, users.size)
        val u = users[0]

        assertEquals(1, u.userId)
        assertEquals("testsan", u.username)
        assertEquals("password", u.password)
        assertEquals("てすとさん", u.displayName)
        assertEquals("testsan@example.com", u.email)
        assertEquals(curTime, u.passwdLastModified)
        assertNull(u.joinedOrgId)
        assertNull(u.roleId)
    }
}