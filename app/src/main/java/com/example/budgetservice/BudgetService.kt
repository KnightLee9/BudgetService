package com.example.budgetservice

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

class BudgetService {
    fun totalAmount(start: GregorianCalendar, end: GregorianCalendar): BigDecimal {
        if (!isCheckDateRange(start, end)) {
            return BigDecimal(0)
        }
        var current = start
        var totalAmt = BigDecimal.ZERO
        current.set(Calendar.DATE, 1)
        while (current.before(end) || isSameDate(current, end)) {
            val dayOfMonth = current.getActualMaximum(Calendar.DAY_OF_MONTH)
            val yearMonth = SimpleDateFormat("yyyyMM").format(current.time)
            when {
                current.get(Calendar.MONTH) == start.get(Calendar.MONTH) -> {
                    if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                        totalAmt = totalAmt.add(
                            getMonthAmt(
                                yearMonth,
                                end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH) + 1,
                                dayOfMonth
                            )
                        )
                    } else {
                        totalAmt += totalAmt.add(
                            getMonthAmt(
                                yearMonth,
                                dayOfMonth - start.get(Calendar.DAY_OF_MONTH) + 1,
                                dayOfMonth
                            )
                        )
                    }
                }

                current.get(Calendar.MONTH) == end.get(Calendar.MONTH) -> {
                    totalAmt = totalAmt.add(
                        getMonthAmt(
                            yearMonth,
                            start.get(Calendar.DAY_OF_MONTH),
                            dayOfMonth
                        )
                    )
                }

                else -> {
                    totalAmt = totalAmt.add(
                        getMonthAmt(
                            SimpleDateFormat("yyyyMM").format(current),
                            dayOfMonth,
                            dayOfMonth
                        )
                    )
                }
            }
            current.add(Calendar.MONTH, 1)
        }
        return totalAmt
    }

    private fun getMonthAmt(ym: String, totalDate: Int, dayOfMonth: Int): BigDecimal {
        val amt = BudgetRepoImpl.getAll().find { it.yearMonth == ym }?.amount ?: BigDecimal.ZERO
        return amt.multiply(BigDecimal(totalDate)).divide(BigDecimal(dayOfMonth))
    }


    private fun isCheckDateRange(start: GregorianCalendar, end: GregorianCalendar): Boolean {
        return end.after(start) || isSameDate(start, end)
    }

    private fun isSameDate(start: GregorianCalendar, end: GregorianCalendar): Boolean {
        return start.get(Calendar.YEAR) == end.get(Calendar.YEAR) && start.get(Calendar.MONTH) == end.get(
            Calendar.MONTH
        )
                && start.get(Calendar.DATE) == end.get(Calendar.DATE)
    }
}
