package com.coffeeshop.report;

public class StockMovementRow {
    private final int productId;
    private final String productName;
    private final int stockIn;
    private final int stockOut;
    private final int currentStock;

    public StockMovementRow(int productId, String productName, int stockIn, int stockOut, int currentStock) {
        this.productId = productId;
        this.productName = productName;
        this.stockIn = stockIn;
        this.stockOut = stockOut;
        this.currentStock = currentStock;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getStockIn() {
        return stockIn;
    }

    public int getStockOut() {
        return stockOut;
    }

    public int getCurrentStock() {
        return currentStock;
    }
}
