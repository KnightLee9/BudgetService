package com.example.budgetservice

import java.math.BigDecimal

object BudgetRepoImpl : BudgetRepo {
    override fun getAll(): List<Budget> {
        return arrayListOf(Budget("202308", BigDecimal(31000)),
            Budget("202309", BigDecimal(30000))
            )
    }

}

interface BudgetRepo {
    fun getAll(): List<Budget>
}
