package com.example.budgetservice

import java.math.BigDecimal
import java.time.YearMonth

data class Budget(val yearMonth: String, val amount: BigDecimal)
