package com.example.budgetservice

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
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
        Assert.assertEquals(BigDecimal(200), budgetService.totalAmount(startCalendar, endCalendar))
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
        every { BudgetRepoImpl.getAll() } answers {
            arrayListOf(
                Budget("202308", BigDecimal(3100)),
                Budget("202309", BigDecimal(3000))
            )
        }
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
            BigDecimal(5100),
            budgetService.totalAmount(startCalendar, endCalendar)
        )
    }

    @Test
    fun `single date`() {
        every { BudgetRepoImpl.getAll() } answers {
            arrayListOf(Budget("202305", BigDecimal(3100)))
        }

        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 4)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 4)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        assert(budgetService.totalAmount(startCalendar, endCalendar) == BigDecimal(100))
    }

}