package com.coffeeshop.service;

import com.coffeeshop.model.SaleItem;
import java.util.List;

public abstract class AbstractTransactionService {
    public abstract double calculateSubtotal(List<SaleItem> items);

    public double calculateTotal(List<SaleItem> items, double discount, double taxPercent) {
        double subtotal = calculateSubtotal(items);
        double afterDiscount = subtotal - discount;
        return afterDiscount + (afterDiscount * taxPercent / 100.0);
    }
}
