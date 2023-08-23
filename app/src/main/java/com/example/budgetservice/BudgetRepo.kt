package com.example.budgetservice

object BudgetRepoImpl : BudgetRepo {
    override fun getAll(): List<Budget> {
       return arrayListOf()
    }

}

interface BudgetRepo {
    fun getAll() : List<Budget>
}
