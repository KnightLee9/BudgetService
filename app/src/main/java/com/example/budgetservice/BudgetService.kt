package com.example.budgetservice

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Calendar
import java.util.GregorianCalendar

class BudgetService {
    fun totalAmount(start: GregorianCalendar, end: GregorianCalendar): BigDecimal {
        if (!isCheckDateRange(start, end)) {
            return BigDecimal(0)
        }
        var current = start
        var totalAmt = BigDecimal.ZERO
        while (current.before(end)) {
            val dayOfMonth = current.getActualMaximum(Calendar.DAY_OF_MONTH)

            val s = start.get(Calendar.DAY_OF_MONTH)
            val e = end.get(Calendar.DAY_OF_MONTH)
            if (current.get(Calendar.MONTH) == start.get(Calendar.MONTH)) {

                if(start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                    totalAmt = totalAmt.add(
                        getMonthAmt(
                            SimpleDateFormat("yyyyMM").format(current.time),
                            end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH) + 1,
                            dayOfMonth
                        )
                    )
                } else {
                    totalAmt.add(
                        getMonthAmt(
                            SimpleDateFormat("yyyyMM").format(current.time),
                            dayOfMonth - start.get(Calendar.DAY_OF_MONTH) + 1,
                            dayOfMonth
                        )
                    )
                }
            } else if (current.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                totalAmt = totalAmt.add(
                    getMonthAmt(
                        SimpleDateFormat("yyyyMM").format(current.time),
                        dayOfMonth - start.get(Calendar.DATE) + 1,
                        dayOfMonth
                    )
                )
            } else {
                totalAmt = totalAmt.add(
                    getMonthAmt(
                        SimpleDateFormat("yyyyMM").format(current),
                        dayOfMonth,
                        dayOfMonth
                    )
                )
            }
            current.add(Calendar.MONTH, 1)
        }
        return totalAmt
    }

    private fun getMonthAmt(ym: String, totalDate: Int, dayOfMonth: Int): BigDecimal {
        val amt = BudgetRepoImpl.getAll().find { it.yearMonth == ym }?.amount ?: BigDecimal.ZERO
        return BigDecimal((amt.toDouble() * totalDate.toDouble() / dayOfMonth.toDouble()))
    }


    private fun isCheckDateRange(start: GregorianCalendar, end: GregorianCalendar): Boolean {
        return end.after(start)
    }
}
