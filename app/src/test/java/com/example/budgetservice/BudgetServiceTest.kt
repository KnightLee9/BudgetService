package com.example.budgetservice

import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

class BudgetServiceTest {
    private val budgetService = BudgetService()


    @Before
    fun setup() {
        mockkObject(BudgetRepoImpl)
    }


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
        every { BudgetRepoImpl.getAll() } answers {
            arrayListOf(Budget("202308", BigDecimal(3100)))
        }
        val startCalendar = getCalendar(2023, 8, 1)
        val endCalendar = getCalendar(2023, 8, 2)
        Assert.assertEquals(BigDecimal(200), budgetService.totalAmount(startCalendar, endCalendar))
    }

    @Test
    fun `data not found`() {
        val startCalendar = getCalendar(2023, 7, 1)
        val endCalendar = getCalendar(2023, 7, 2)
        Assert.assertEquals(BigDecimal(0), budgetService.totalAmount(startCalendar, endCalendar))
    }

    @Test
    fun `multiple month`() {
        every { BudgetRepoImpl.getAll() } answers {
            arrayListOf(
                Budget("202308", BigDecimal(3100)),
                Budget("202309", BigDecimal(3000))
            )
        }
        val startCalendar = getCalendar(2023, 7, 31)
        val endCalendar = getCalendar(2023, 9, 20)
        Assert.assertEquals(
            BigDecimal(5100),
            budgetService.totalAmount(startCalendar, endCalendar)
        )
    }

    private fun getCalendar(year: Int, month: Int, day: Int): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }
    }

    @Test
    fun `single date`() {
        every { BudgetRepoImpl.getAll() } answers {
            arrayListOf(Budget("202305", BigDecimal(3100)))
        }

        val startCalendar = getCalendar(2023, 5, 1)
        val endCalendar = getCalendar(2023, 5, 1)
        assert(budgetService.totalAmount(startCalendar, endCalendar) == BigDecimal(100))
    }

}