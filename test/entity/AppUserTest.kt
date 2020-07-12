package net.formula97.webapps.entity

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import java.sql.Date
import java.time.Instant
import kotlin.test.Test

class AppUserTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getsBindMap1() {
        val curTime = Date.from(Instant.now())
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
        assertEquals("testsan", map["username"])
        assertEquals("password", map["password"])
        assertEquals("てすとさん", map["displayName"])
        assertEquals("testsan@example.com", map["email"])
        assertEquals(curTime, map["passwdLastModified"])
        assertNull(map["joinedOrgId"])
        assertNull(map["roleId"])
    }

    @Test
    fun getsBindMap2() {
        val curTime = Date.from(Instant.now())
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
        assertEquals("testsan", map["username"])
        assertEquals("password", map["password"])
        assertEquals("てすとさん", map["displayName"])
        assertEquals("testsan@example.com", map["email"])
        assertEquals(curTime, map["passwdLastModified"])
        assertNull(map["joinedOrgId"])
        assertNull(map["roleId"])
    }

    @Test
    fun getsBindMap3() {
        val curTime = Date.from(Instant.now())
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
        assertNull(map["userId"])
    }

    @Test
    fun getDefaultFieldMapper1() {
        val curTime = Date.from(Instant.now())
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

        assertEquals("userId", map["user_id"])
        assertEquals("username", map["username"])
        assertEquals("password", map["password"])
        assertEquals("displayName", map["display_name"])
        assertEquals("email", map["email"])
        assertEquals("passwdLastModified", map["passwd_last_modified"])
        assertEquals("joinedOrgId", map["joined_org_id"])
        assertEquals("roleId", map["role_id"])
    }

    @Test
    fun getDefaultFieldMapper2() {
        val curTime = Date.from(Instant.now())
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
        assertEquals("displayName", map["display_name"])
        assertEquals("email", map["email"])
        assertEquals("passwdLastModified", map["passwd_last_modified"])
        assertEquals("joinedOrgId", map["joined_org_id"])
        assertEquals("roleId", map["role_id"])
    }
}