package org.example.finsight.stats.dto;

import java.math.BigDecimal;

public interface CategorySummaryProjection {
    String getCategory();
    BigDecimal getTotal();
}