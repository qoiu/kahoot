package com.example.kahoot.domain.model.statistic

import javafx.scene.chart.XYChart

interface ChartGraphic {
    fun toSeries(): List<XYChart.Series<*, *>>
    interface Add : ChartGraphic {
        fun addGroup(name: String): Add
        fun addObj(name: String, value: Number): Add
    }


    class Base : Add {
        private val groups = mutableListOf<Group>()
        private lateinit var g: Group

        override fun addGroup(name: String): Add {
            g = Group(name)
            groups.add(g)
            return this
        }


        override fun addObj(name: String, value: Number): Add {
            g.objs.add(Obj(name, value))
            return this
        }

        class Group(internal val groupName: String) {
            internal val objs = mutableListOf<Obj>()
        }

        internal class Obj(internal val key: String, internal val value: Number)

        override fun toSeries(): List<XYChart.Series<*, *>> {
            val result = mutableListOf<XYChart.Series<*, *>>()
            groups.forEach { group ->
                val series: XYChart.Series<String, Number> = XYChart.Series<String, Number>()
                series.name = group.groupName
                group.objs.forEach { obj ->
                    series.data.add(XYChart.Data(obj.key, obj.value))
                }
                result.add(series)
            }
            return result.toList()
        }
    }
}