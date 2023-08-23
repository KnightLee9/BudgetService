package com.example.budgetservice

import java.math.BigDecimal
import java.util.Date

class BudgetService {
    fun totalAmount(start: Date, end: Date): BigDecimal {
        if (!isCheckDateRange(start, end)) {
            return  BigDecimal(0)
        }

        BudgetRepoImpl.getAll()
        return BigDecimal(0)
    }

    private fun isCheckDateRange(start: Date, end: Date): Boolean {
        return end.after(start)
    }
}
