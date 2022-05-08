package com.example.kahoot.domain.clean

sealed class Conditions {
    open fun toSql(): String = ""

    object Empty : Conditions()

    class Base(field: String = "", value: Any = "") : Conditions() {
        private val set = if (field != "") mutableSetOf(Condition(field, value)) else mutableSetOf()
        fun addCondition(field: String, value: Any): Base {
            set.add(Condition(field, value))
            return this
        }

        fun get(key: String): Any? = set.find { it.checkEquals(key) }?.getValue()

        override fun toSql(): String {
            if (set.isEmpty()) return ""
            var where = "WHERE "
            set.forEachIndexed { index, condition ->
                where += condition.toString()
                if (index != set.size - 1) {
                    where += " AND "
                }
            }
            return where
        }

        private inner class Condition(private val field: String, private val value: Any) {
            fun checkEquals(other: String): Boolean = field == other
            fun getValue() = value
            override fun toString(): String = "$field = ${toSql(value)}"
        }

        private fun toSql(any: Any): String = when (any) {
            is String -> "'$any'"
            else -> "$any"
        }
    }
}