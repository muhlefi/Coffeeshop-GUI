package com.coffeeshop.repository;

import com.coffeeshop.model.Purchase;
import com.coffeeshop.model.PurchaseItem;
import java.sql.SQLException;
import java.util.List;

public interface PurchaseTransactionRepository {
    int save(Purchase purchase, List<PurchaseItem> items) throws SQLException;
    List<Purchase> findRecent(int limit) throws SQLException;
}
