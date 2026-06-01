package com.coffeeshop.repository;

import com.coffeeshop.model.Sale;
import com.coffeeshop.model.SaleItem;
import java.sql.SQLException;
import java.util.List;

public interface SalesTransactionRepository {
    int save(Sale sale, List<SaleItem> items) throws SQLException;
    List<Sale> findRecent(int limit) throws SQLException;
}
