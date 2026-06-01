package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Sale;
import com.coffeeshop.model.SaleItem;
import com.coffeeshop.repository.SalesTransactionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesTransactionRepositoryImpl implements SalesTransactionRepository {
    @Override
    public int save(Sale sale, List<SaleItem> items) throws SQLException {
        String insertSaleSql = "INSERT INTO sales (tanggal, id_user, id_customer, subtotal, diskon, pajak, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO sales_detail (id_sale, id_product, qty, harga, subtotal_item) VALUES (?, ?, ?, ?, ?)";
        String lockStockSql = "SELECT stok FROM products WHERE id_product = ? FOR UPDATE";
        String reduceStockSql = "UPDATE products SET stok = stok - ? WHERE id_product = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                validateStock(conn, items, lockStockSql);

                int saleId;
                try (PreparedStatement stmt = conn.prepareStatement(insertSaleSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setTimestamp(1, Timestamp.valueOf(sale.getTransactionDate()));
                    stmt.setInt(2, sale.getUserId());
                    if (sale.getCustomerId() == null) {
                        stmt.setNull(3, java.sql.Types.INTEGER);
                    } else {
                        stmt.setInt(3, sale.getCustomerId());
                    }
                    stmt.setDouble(4, sale.getSubtotal());
                    stmt.setDouble(5, sale.getDiscount());
                    stmt.setDouble(6, sale.getTax());
                    stmt.setDouble(7, sale.getTotal());
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Gagal mengambil ID penjualan.");
                        }
                        saleId = keys.getInt(1);
                    }
                }

                try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql);
                     PreparedStatement stockStmt = conn.prepareStatement(reduceStockSql)) {
                    for (SaleItem item : items) {
                        double subtotalItem = item.getUnitPrice() * item.getQuantity();

                        detailStmt.setInt(1, saleId);
                        detailStmt.setInt(2, item.getProductId());
                        detailStmt.setInt(3, item.getQuantity());
                        detailStmt.setDouble(4, item.getUnitPrice());
                        detailStmt.setDouble(5, subtotalItem);
                        detailStmt.addBatch();

                        stockStmt.setInt(1, item.getQuantity());
                        stockStmt.setInt(2, item.getProductId());
                        stockStmt.addBatch();
                    }
                    detailStmt.executeBatch();
                    stockStmt.executeBatch();
                }

                conn.commit();
                return saleId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<Sale> findRecent(int limit) throws SQLException {
        String sql = "SELECT id_sale, tanggal, id_user, id_customer, subtotal, diskon, pajak, total FROM sales ORDER BY id_sale DESC LIMIT ?";
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sale sale = new Sale();
                    sale.setId(rs.getInt("id_sale"));
                    sale.setTransactionDate(rs.getTimestamp("tanggal").toLocalDateTime());
                    sale.setUserId(rs.getInt("id_user"));
                    int customerId = rs.getInt("id_customer");
                    sale.setCustomerId(rs.wasNull() ? null : customerId);
                    sale.setSubtotal(rs.getDouble("subtotal"));
                    sale.setDiscount(rs.getDouble("diskon"));
                    sale.setTax(rs.getDouble("pajak"));
                    sale.setTotal(rs.getDouble("total"));
                    sales.add(sale);
                }
            }
        }
        return sales;
    }

    private void validateStock(Connection conn, List<SaleItem> items, String lockStockSql) throws SQLException {
        Map<Integer, Integer> requiredByProduct = new HashMap<>();
        for (SaleItem item : items) {
            requiredByProduct.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        try (PreparedStatement lockStmt = conn.prepareStatement(lockStockSql)) {
            for (Map.Entry<Integer, Integer> entry : requiredByProduct.entrySet()) {
                int productId = entry.getKey();
                int requiredQty = entry.getValue();
                lockStmt.setInt(1, productId);
                try (ResultSet rs = lockStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Produk dengan ID " + productId + " tidak ditemukan.");
                    }
                    int stock = rs.getInt("stok");
                    if (stock < requiredQty) {
                        throw new SQLException("Stok produk ID " + productId + " tidak mencukupi.");
                    }
                }
            }
        }
    }
}
