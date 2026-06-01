package com.coffeeshop.report;

public class TopProductRow {
    private final int productId;
    private final String productName;
    private final int totalQty;
    private final double totalRevenue;

    public TopProductRow(int productId, String productName, int totalQty, double totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.totalQty = totalQty;
        this.totalRevenue = totalRevenue;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
