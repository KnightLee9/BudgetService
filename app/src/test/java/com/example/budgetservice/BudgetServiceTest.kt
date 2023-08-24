package com.example.budgetservice

import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

class BudgetServiceTest {
    private val budgetService = BudgetService()

    @Test
    fun `起訖日不合法`() {
        val startCalendar = Calendar.getInstance().apply {
            time = Date()
        }
        val endCalendar = Calendar.getInstance().apply {
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
        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 7)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 7)
            set(Calendar.DAY_OF_MONTH, 2)
        }
        Assert.assertEquals(BigDecimal(2000), budgetService.totalAmount(startCalendar, endCalendar))
    }

    @Test
    fun `data not found`() {
        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 6)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 6)
            set(Calendar.DAY_OF_MONTH, 2)
        }
        Assert.assertEquals(BigDecimal(0), budgetService.totalAmount(startCalendar, endCalendar))
    }

    @Test
    fun `multiple month`() {
        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 6)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 20)
        }
        Assert.assertEquals(
            BigDecimal(51000),
            budgetService.totalAmount(startCalendar, endCalendar)
        )
    }

    @Test
    fun `single date`() {
        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 7)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 7)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        Assert.assertEquals(
            BigDecimal(1000),
            budgetService.totalAmount(startCalendar, endCalendar)
        )
    }

}