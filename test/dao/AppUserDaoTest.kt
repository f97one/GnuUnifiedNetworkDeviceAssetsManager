package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.dao.config.DbConnectionConfig
import net.formula97.webapps.entity.AppUser
import org.flywaydb.core.Flyway
import org.junit.After
import org.junit.Before
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import javax.sql.DataSource
import kotlin.test.*

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
        val result1 = exec.executeRawSql(delAll)
        println("delete successful : ${result1.success}")
        println("stack trace : ${result1.causedException}")
        val resetSeq = "alter sequence app_user_user_id_seq restart with 1"
        val result2 = exec.executeRawSql(resetSeq)
        println("reset sequence successful : ${result2.success}")
        println("stack trace : ${result2.causedException}")
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

    @Test
    fun updatesExistingUser() {
        // データを追加
        val stmt = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan2', 'P@ssw0rd', 'てすとさん２', 'testsan2@example.com', '2020-07-12 19:37:00', null, null)
        """.trimIndent()
        GenericSqlExecutor(ds).executeRawSql(stmt)
        val appUser = AppUser(userId = 1)
        val dao = AppUserDao()
        val userOpt1 = dao.findByKey(appUser)
        assertTrue(userOpt1.isPresent)

        val user = userOpt1.get()
        user.displayName = "てすとさん２を変更"
        val result = dao.updateOneByPK(user)
        assertTrue(result.success)
        assertEquals(1, result.affectedRows)

        val userOpt2 = dao.findByKey(appUser)
        assertTrue(userOpt2.isPresent)
        val actUser = userOpt2.get()

        assertEquals(1, actUser.userId)
        assertEquals("testsan2", actUser.username)
        assertEquals("P@ssw0rd", actUser.password)
        assertEquals("てすとさん２を変更", actUser.displayName)
        assertEquals("testsan2@example.com", actUser.email)
        assertEquals(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-07-12 19:37:00"), actUser.passwdLastModified)
        assertNull(actUser.joinedOrgId)
        assertNull(actUser.roleId)
    }

    @Test
    fun deletesExistingUserById() {
        // データを追加
        val s1 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan2', 'P@ssw0rd', 'てすとさん２', 'testsan2@example.com', '2020-07-12 19:37:00', null, null)
        """.trimIndent()
        val s2 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan', 'P@ssw0rd', 'てすとさん', 'testsan@example.com', '2020-07-12 21:35:00', null, null)
        """.trimIndent()
        val fix = listOf(s1, s2)
        GenericSqlExecutor(ds).executeRawSql(fix)
        val appUser = AppUser(userId = 2)
        val dao = AppUserDao()
        val result = dao.deleteOneByPK(appUser)
        assertTrue(result.success)

        val lst = dao.findAll(AppUser::class.java)
        assertEquals(1, lst.size)
        val actUser = lst[0]

        assertEquals(1, actUser.userId)
        assertEquals("testsan2", actUser.username)
        assertEquals("P@ssw0rd", actUser.password)
        assertEquals("てすとさん２", actUser.displayName)
        assertEquals("testsan2@example.com", actUser.email)
        assertEquals(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-07-12 19:37:00"), actUser.passwdLastModified)
        assertNull(actUser.joinedOrgId)
        assertNull(actUser.roleId)
    }

    @Test
    fun deletesAllRecords() {
        // データを追加
        val s1 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan2', 'P@ssw0rd', 'てすとさん２', 'testsan2@example.com', '2020-07-12 19:37:00', null, null)
        """.trimIndent()
        val s2 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan', 'P@ssw0rd', 'てすとさん', 'testsan@example.com', '2020-07-12 21:35:00', null, null)
        """.trimIndent()
        val fix = listOf(s1, s2)
        GenericSqlExecutor(ds).executeRawSql(fix)

        val result = AppUserDao().deleteAll(AppUser::class.java)
        assertTrue(result.success)
        assertEquals(2, result.affectedRows)

        val lst = AppUserDao().findAll(AppUser::class.java)
        assertEquals(0, lst.size)
    }

    @Test
    fun findsByProvidedUsername() {
        // データを追加
        val s1 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan2', 'P@ssw0rd', 'てすとさん２', 'testsan2@example.com', '2020-07-12 19:37:00', null, null)
        """.trimIndent()
        val s2 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan', 'P@ssw0rd', 'てすとさん', 'testsan@example.com', '2020-07-12 21:35:00', null, null)
        """.trimIndent()
        val fix = listOf(s1, s2)
        GenericSqlExecutor(ds).executeRawSql(fix)

        val userOpt = AppUserDao().loadByUsername("testsan")
        assertTrue(userOpt.isPresent)
        val user = userOpt.get()
        assertEquals("P@ssw0rd", user.password)
        assertEquals("てすとさん", user.displayName)
        assertEquals("testsan@example.com", user.email)
        assertEquals(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-07-12 21:35:00"), user.passwdLastModified)
    }

    @Test
    fun returnsEmptyIfUsernameNotFound() {
        // データを追加
        val s1 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan2', 'P@ssw0rd', 'てすとさん２', 'testsan2@example.com', '2020-07-12 19:37:00', null, null)
        """.trimIndent()
        val s2 = """
            insert into app_user (username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
            values ('testsan', 'P@ssw0rd', 'てすとさん', 'testsan@example.com', '2020-07-12 21:35:00', null, null)
        """.trimIndent()
        val fix = listOf(s1, s2)
        GenericSqlExecutor(ds).executeRawSql(fix)

        val userOpt = AppUserDao().loadByUsername("testsan3")
        assertFalse(userOpt.isPresent)
    }
}