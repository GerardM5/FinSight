package org.example.finsight.stats.dto;

import java.math.BigDecimal;

public interface MonthlySummaryProjection {
    String getMonth();
    BigDecimal getIncome();
    BigDecimal getExpense();
    BigDecimal getNet();
}