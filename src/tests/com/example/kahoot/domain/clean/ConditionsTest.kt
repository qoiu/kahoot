package com.example.kahoot.domain.clean

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConditionsTest {

    @Test
    fun toSql() {
        val conditions = Conditions.Base()
        conditions.addCondition("id", 2)
        conditions.addCondition("name", "Vasya")
        assertEquals(conditions.toSql(), "WHERE id = 2 AND name = 'Vasya'")
    }

    @Test
    fun emptySql() {
        assertEquals("", Conditions.Base().toSql())
    }

    @Test
    fun get() {
        val conditions = Conditions.Base()
        conditions.addCondition("id", 2)
        conditions.addCondition("name", "Vasya")
        assertEquals(2, conditions.get("id"))
        assertEquals("Vasya", conditions.get("name"))
        assertEquals(null, conditions.get("title"))
    }

    @Test
    fun empty() {
        val conditions = Conditions.Empty
        assertEquals("", conditions.toSql())
    }
}