package com.example.budgetservice

import java.math.BigDecimal
import java.util.Calendar

class BudgetService {
    fun totalAmount(start: Calendar, end: Calendar): BigDecimal {
        if (!isCheckDateRange(start, end)) {
            return BigDecimal(0)
        }
        return BudgetRepoImpl.getAll().map {
            val budgetYearMonth = Calendar.getInstance().apply {
                set(Calendar.YEAR, it.yearMonth.substring(0, 4).toInt())
                set(Calendar.MONTH, it.yearMonth.substring(4, 6).toInt() - 1)
            }
            when {
                inBudgetRange(start, end, budgetYearMonth) -> {
                    it.amount.multiply(
                        BigDecimal(
                            getEndDay(end, budgetYearMonth)
                                    - getStartDay(start, budgetYearMonth)
                                    + 1
                        )
                    ).divide(BigDecimal(budgetYearMonth.getActualMaximum(Calendar.DAY_OF_MONTH)))
                }

                else -> BigDecimal.ZERO
            }
        }.sumOf { it }
    }

    private fun getStartDay(start: Calendar, budgetYearMonth: Calendar): Int {
        return if (start.get(Calendar.YEAR) == budgetYearMonth.get(Calendar.YEAR)
            && start.get(Calendar.MONTH) == budgetYearMonth.get(Calendar.MONTH)
        ) {
            start.get(Calendar.DAY_OF_MONTH)
        } else {
            1
        }
    }

    private fun getEndDay(end: Calendar, budget: Calendar): Int {
        return if (end.get(Calendar.YEAR) == budget.get(Calendar.YEAR)
            && end.get(Calendar.MONTH) == budget.get(Calendar.MONTH)
        ) {
            end.get(Calendar.DAY_OF_MONTH)
        } else {
            budget.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }


    private fun inBudgetRange(
        start: Calendar,
        end: Calendar,
        budget: Calendar,
    ): Boolean {
        return budget.after((start.clone() as Calendar).apply {
            add(Calendar.MONTH, -1)
        }) && budget.before((end.clone() as Calendar).apply {
            add(Calendar.MONTH, 1)
        })
    }

    private fun isCheckDateRange(start: Calendar, end: Calendar): Boolean {
        return end.after(start) || isSameDate(start, end)
    }

    private fun isSameDate(start: Calendar, end: Calendar): Boolean {
        return start.get(Calendar.YEAR) == end.get(Calendar.YEAR) &&
                start.get(Calendar.MONTH) == end.get(Calendar.MONTH) &&
                start.get(Calendar.DATE) == end.get(Calendar.DATE)
    }
}
