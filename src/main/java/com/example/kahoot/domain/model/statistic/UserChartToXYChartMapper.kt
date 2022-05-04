package com.example.kahoot.domain.model.statistic

import com.example.kahoot.Mapper
import javafx.scene.chart.XYChart
import java.util.function.Consumer

class UserChartToXYChartMapper : Mapper.Base<UsersTimeChart, List<XYChart.Series<*, *>>> {
    override fun map(data: UsersTimeChart): List<XYChart.Series<*, *>> {
        val result = mutableListOf<XYChart.Series<*, *>>()
        for (i in 0 until data.qAmount) {
            data.list[i].let { series ->
                val s: XYChart.Series<String, Long> = XYChart.Series<String, Long>()
                s.name = i.toString()
                series.list.forEach(Consumer { (user, value): UsersTimeChart.Row ->
                    s.data.add(XYChart.Data(user.currentNick, value))
                })
                result.add(s)
//                    series.list.add(s)
            }
        }
        data.totalTime().let { row ->
            val s: XYChart.Series<String, Long> = XYChart.Series<String, Long>()
            s.name = "total"
            row.forEach(Consumer { (user, value): UsersTimeChart.Row ->
                s.data.add(XYChart.Data(user.currentNick, value))
            })
            result.add(s)
        }
        return result
    }
}