package com.example.kahoot.presentation.model

import javafx.scene.chart.XYChart

interface SingleChartGraphic {
    fun toSeries(): List<XYChart.Series<String,Int>>
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

        override fun toSeries(): List<XYChart.Series<String,Int>> {
            val result = mutableListOf(XYChart.Series<String,Int>())
            objs.forEach { obj ->
                val series: XYChart.Series<String, Int> = XYChart.Series<String, Int>()
                series.name = obj.key
                series.data.add(XYChart.Data(obj.key, obj.value.toInt()))
                result.add(series)
            }
            return result.toList()
        }
    }
}