package com.example.kahoot.presentation.model

import javafx.scene.chart.XYChart
import java.lang.Double.max

interface ChartGraphic<T,Y> {
    fun toSeries(): List<XYChart.Series<T, Y>>
    fun getMaxValue(): Double
    interface Add<T,Y> : ChartGraphic<T, Y> {
        fun addGroup(name: String): Add<T, Y>
        fun addObj(name: T, value: Y): Add<T, Y>
    }


    class Base<Out: Number> : Add<String, Out> {
        private val groups = mutableListOf<Group>()
        private lateinit var g: Group

        override fun addGroup(name: String): Add<String, Out> {
            g = Group(name)
            groups.add(g)
            return this
        }


        override fun addObj(name: String, value: Out): Add<String, Out> {
            g.objs.add(Obj(name, value))
            return this
        }

        class Group(internal val groupName: String) {
            internal val objs = mutableListOf<Obj>()
        }

        internal class Obj(internal val key: String, internal val value: Number)

        override fun toSeries(): List<XYChart.Series<String,Out>> {
            val result = mutableListOf<XYChart.Series<String,Out>>()
            groups.forEach { group ->
                val series: XYChart.Series<String,Out> = XYChart.Series<String,Out>()
                series.name = group.groupName
                group.objs.forEach { obj ->
                    series.data.add(XYChart.Data(obj.key, obj.value as Out))
                }
                result.add(series)
            }
            return result.toList()
        }

        override fun getMaxValue(): Double{
            var g = 0.0
            groups.forEach { group ->
                group.objs.forEach { obj->
                    g=max(g,obj.value.toDouble())
                }
            }
            return g
        }
    }
}