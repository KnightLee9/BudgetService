package com.example.budgetservice

import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class BudgetServiceTest {
    private val budgetService = BudgetService()

    @Test
    fun `起訖日不合法`() {
        val startCalendar = GregorianCalendar().apply {
            time = Date()
        }
        val endCalendar = GregorianCalendar().apply {
            time = Date()
        }.also {
            it.set(Calendar.DATE, -1)
        }
        Assert.assertEquals(
            BigDecimal.ZERO,
            budgetService.totalAmount(startCalendar, endCalendar)
        )
    }

    @Test
    fun `partial month`() {
        val startCalendar = GregorianCalendar().apply {

            time = Date(2023 - 1900, 7, 1)
        }
        val endCalendar = GregorianCalendar().apply {
            time = Date(2023 - 1900, 7, 2)
        }
        Assert.assertEquals(BigDecimal(2000), budgetService.totalAmount(startCalendar, endCalendar))
    }

    @Test
    fun `data not found`() {
        val startCalendar = GregorianCalendar().apply {

            time = Date(2023 - 1900, 6, 1)
        }
        val endCalendar = GregorianCalendar().apply {
            time = Date(2023 - 1900, 6, 2)
        }
        Assert.assertEquals(BigDecimal(0), budgetService.totalAmount(startCalendar, endCalendar))
    }

}