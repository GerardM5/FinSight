package org.example.finsight.stats.dto;

import java.math.BigDecimal;

public class MonthlySummaryDTO {
    private String month;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal net;

    public MonthlySummaryDTO(String month, BigDecimal income, BigDecimal expense) {
        this.month = month;
        this.income = income != null ? income : BigDecimal.ZERO;
        this.expense = expense != null ? expense : BigDecimal.ZERO;
        this.net = this.income.subtract(this.expense);
    }



    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }
}