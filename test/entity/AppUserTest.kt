package net.formula97.webapps.entity

import net.formula97.webapps.entity.AppUser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.*

class AppUserTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getsBindMap1() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )

        val map = appUser.getBindValues()
        assertEquals(8, map.size)
        assertNull(map[":userId"])
        assertEquals("testsan", map[":username"])
        assertEquals("password", map[":password"])
        assertEquals("てすとさん", map[":displayName"])
        assertEquals("testsan@example.com", map[":email"])
        assertEquals(curTime, map[":passwdLastModified"])
        assertNull(map[":joinedOrgId"])
        assertNull(map[":roleId"])
    }

    @Test
    fun getsBindMap2() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )

        val map = appUser.getBindValues(ignoreForInsertion = true)
        assertEquals(7, map.size)
//        assertNull(map[":userId"])
        assertEquals("testsan", map[":username"])
        assertEquals("password", map[":password"])
        assertEquals("てすとさん", map[":displayName"])
        assertEquals("testsan@example.com", map[":email"])
        assertEquals(curTime, map[":passwdLastModified"])
        assertNull(map[":joinedOrgId"])
        assertNull(map[":roleId"])
    }

    @Test
    fun getsBindMap3() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )

        val map = appUser.getBindValues(extractPrimaryKeyOnly = true)
        assertEquals(1, map.size)
        assertNull(map[":userId"])
//        assertEquals("testsan", map[":username"])
//        assertEquals("password", map[":password"])
//        assertEquals("てすとさん", map[":displayName"])
//        assertEquals("testsan@example.com", map[":email"])
//        assertEquals(curTime, map[":passwdLastModified"])
//        assertNull(map[":joinedOrgId"])
//        assertNull(map[":roleId"])
    }

    @Test
    fun getsBindMap4() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )

        val map = appUser.getBindValues(bindPrefix = '$')
        assertEquals(8, map.size)
        assertNull(map["\$userId"])
        assertEquals("testsan", map["\$username"])
        assertEquals("password", map["\$password"])
        assertEquals("てすとさん", map["\$displayName"])
        assertEquals("testsan@example.com", map["\$email"])
        assertEquals(curTime, map["\$passwdLastModified"])
        assertNull(map["\$joinedOrgId"])
        assertNull(map["\$roleId"])
    }

    @Test
    fun getDefaultFieldMapper1() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )
        val map = appUser.getDefaultMapper()
        assertEquals(8, map.size)

        assertEquals("user_id", map["userId"])
        assertEquals("username", map["username"])
        assertEquals("password", map["password"])
        assertEquals("display_name", map["displayName"])
        assertEquals("email", map["email"])
        assertEquals("passwd_last_modified", map["passwdLastModified"])
        assertEquals("joined_org_id", map["joinedOrgId"])
        assertEquals("role_id", map["roleId"])
    }

    @Test
    fun getDefaultFieldMapper2() {
        val curTime = LocalDateTime.now()
        val appUser = AppUser(
            username = "testsan",
            password = "password",
            displayName = "てすとさん",
            email = "testsan@example.com",
            passwdLastModified = curTime,
            joinedOrgId = null,
            roleId = null
        )
        val map = appUser.getDefaultMapper(buildForInsertion = true)
        assertEquals(7, map.size)

//        assertEquals("user_id", map["userId"])
        assertEquals("username", map["username"])
        assertEquals("password", map["password"])
        assertEquals("display_name", map["displayName"])
        assertEquals("email", map["email"])
        assertEquals("passwd_last_modified", map["passwdLastModified"])
        assertEquals("joined_org_id", map["joinedOrgId"])
        assertEquals("role_id", map["roleId"])
    }
}