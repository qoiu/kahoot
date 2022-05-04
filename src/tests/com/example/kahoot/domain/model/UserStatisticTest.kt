package com.example.kahoot.domain.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.example.kahoot.domain.model.UserStatistic.Base.QuestionStatistic.*

internal class UserStatisticTest{

    @Test
    fun addAnswer(){
        val stat = UserStatistic.Test()
        assertEquals(stat.get().size,0)
        assertEquals(NoAnswer,stat.getStatistic(0))
        stat.addAnswer(true,1,10L)
        assertEquals(Base(true,1,10L),stat.getStatistic(0))
        assertEquals(NoAnswer,stat.getStatistic(1))
        assertEquals(NoAnswer,stat.getStatistic(2))
        assertEquals(stat.get().size,1)
        stat.addAnswer(true,1,10L)
        assertEquals(stat.get().size,2)
        stat.addAnswer(true,1,10L)
        assertEquals(stat.get().size,3)
        assertEquals(Base(true,1,10L),stat.getStatistic(0))
        assertEquals(Base(true,1,10L),stat.getStatistic(1))
        assertEquals(Base(true,1,10L),stat.getStatistic(2))
    }

}