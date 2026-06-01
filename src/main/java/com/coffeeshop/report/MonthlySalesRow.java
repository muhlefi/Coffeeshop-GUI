package com.coffeeshop.report;

public class MonthlySalesRow {
    private final String monthLabel;
    private final int transactionCount;
    private final double totalSales;

    public MonthlySalesRow(String monthLabel, int transactionCount, double totalSales) {
        this.monthLabel = monthLabel;
        this.transactionCount = transactionCount;
        this.totalSales = totalSales;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public double getTotalSales() {
        return totalSales;
    }
}
