package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Purchase;
import com.coffeeshop.model.PurchaseItem;
import com.coffeeshop.repository.PurchaseTransactionRepository;
import com.coffeeshop.repository.impl.PurchaseTransactionRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseOrderService {
    private final PurchaseTransactionRepository purchaseRepository;

    public PurchaseOrderService() {
        this.purchaseRepository = new PurchaseTransactionRepositoryImpl();
    }

    public PurchaseItem buildItem(int productId, int quantity, double unitCost) throws DataTidakValidException {
        if (productId <= 0) {
            throw new DataTidakValidException("Produk ID harus valid.");
        }
        if (quantity <= 0) {
            throw new DataTidakValidException("Qty harus lebih dari 0.");
        }
        if (unitCost <= 0) {
            throw new DataTidakValidException("Harga beli harus lebih dari 0.");
        }
        return new PurchaseItem(productId, quantity, unitCost);
    }

    public int createPurchase(
        int userId,
        int supplierId,
        List<PurchaseItem> items
    ) throws SQLException, DataTidakValidException, InputKosongException {
        if (userId <= 0) {
            throw new DataTidakValidException("User ID harus valid.");
        }
        if (supplierId <= 0) {
            throw new DataTidakValidException("Supplier ID harus valid.");
        }
        if (items == null || items.isEmpty()) {
            throw new InputKosongException("Minimal ada 1 item dalam pembelian.");
        }

        double total = 0;
        for (PurchaseItem item : items) {
            ValidationUtil.requirePositive(item.getUnitCost(), "Harga beli item");
            ValidationUtil.requireNonNegative(item.getQuantity(), "Qty item");
            if (item.getQuantity() == 0) {
                throw new DataTidakValidException("Qty item harus lebih dari 0.");
            }
            total += item.getUnitCost() * item.getQuantity();
        }

        Purchase purchase = new Purchase();
        purchase.setTransactionDate(LocalDateTime.now());
        purchase.setUserId(userId);
        purchase.setSupplierId(supplierId);
        purchase.setTotal(total);

        return purchaseRepository.save(purchase, items);
    }

    public List<Purchase> getRecentPurchases(int limit) throws SQLException {
        if (limit <= 0) {
            limit = 20;
        }
        return purchaseRepository.findRecent(limit);
    }
}
