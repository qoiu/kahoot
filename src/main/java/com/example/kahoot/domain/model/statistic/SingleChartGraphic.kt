package com.example.kahoot.domain.model.statistic

import javafx.scene.chart.XYChart

interface SingleChartGraphic {
    fun toSeries(): List<XYChart.Series<*, *>>
    interface Add : SingleChartGraphic {
        fun addObj(name: String, value: Number): Add
    }


    class Base : Add {
        private val objs = mutableListOf<Obj>()

        override fun addObj(name: String, value: Number): Add {
            objs.add(Obj(name, value))
            return this
        }

        internal class Obj(internal val key: String, internal val value: Number)

        override fun toSeries(): List<XYChart.Series<*, *>> {
            val result = mutableListOf<XYChart.Series<*, *>>()
            objs.forEach { obj ->
                val series: XYChart.Series<String, Number> = XYChart.Series<String, Number>()
                series.name = obj.key
                series.data.add(XYChart.Data(obj.key, obj.value))
                result.add(series)
            }
            return result.toList()
        }
    }
}