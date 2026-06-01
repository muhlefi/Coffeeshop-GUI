package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Purchase;
import com.coffeeshop.model.PurchaseItem;
import com.coffeeshop.repository.PurchaseTransactionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PurchaseTransactionRepositoryImpl implements PurchaseTransactionRepository {
    @Override
    public int save(Purchase purchase, List<PurchaseItem> items) throws SQLException {
        String insertPurchaseSql = "INSERT INTO purchases (tanggal, id_user, id_supplier, total) VALUES (?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO purchase_detail (id_purchase, id_product, qty, harga_beli, subtotal_item) VALUES (?, ?, ?, ?, ?)";
        String addStockSql = "UPDATE products SET stok = stok + ? WHERE id_product = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int purchaseId;
                try (PreparedStatement stmt = conn.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setTimestamp(1, Timestamp.valueOf(purchase.getTransactionDate()));
                    stmt.setInt(2, purchase.getUserId());
                    stmt.setInt(3, purchase.getSupplierId());
                    stmt.setDouble(4, purchase.getTotal());
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Gagal mengambil ID pembelian.");
                        }
                        purchaseId = keys.getInt(1);
                    }
                }

                try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql);
                     PreparedStatement stockStmt = conn.prepareStatement(addStockSql)) {
                    for (PurchaseItem item : items) {
                        double subtotalItem = item.getUnitCost() * item.getQuantity();

                        detailStmt.setInt(1, purchaseId);
                        detailStmt.setInt(2, item.getProductId());
                        detailStmt.setInt(3, item.getQuantity());
                        detailStmt.setDouble(4, item.getUnitCost());
                        detailStmt.setDouble(5, subtotalItem);
                        detailStmt.addBatch();

                        stockStmt.setInt(1, item.getQuantity());
                        stockStmt.setInt(2, item.getProductId());
                        stockStmt.addBatch();
                    }

                    detailStmt.executeBatch();
                    int[] updatedRows = stockStmt.executeBatch();
                    for (int updatedRow : updatedRows) {
                        if (updatedRow == 0) {
                            throw new SQLException("Ada produk pada detail pembelian yang tidak valid.");
                        }
                    }
                }

                conn.commit();
                return purchaseId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<Purchase> findRecent(int limit) throws SQLException {
        String sql = "SELECT id_purchase, tanggal, id_user, id_supplier, total FROM purchases ORDER BY id_purchase DESC LIMIT ?";
        List<Purchase> purchases = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setId(rs.getInt("id_purchase"));
                    purchase.setTransactionDate(rs.getTimestamp("tanggal").toLocalDateTime());
                    purchase.setUserId(rs.getInt("id_user"));
                    purchase.setSupplierId(rs.getInt("id_supplier"));
                    purchase.setTotal(rs.getDouble("total"));
                    purchases.add(purchase);
                }
            }
        }
        return purchases;
    }
}
