package com.coffeeshop.service;

import com.coffeeshop.model.SaleItem;
import java.util.List;

public class SalesTransactionService extends AbstractTransactionService {
    @Override
    public double calculateSubtotal(List<SaleItem> items) {
        return items.stream()
            .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
            .sum();
    }
}
