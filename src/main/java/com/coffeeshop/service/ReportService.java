package com.coffeeshop.service;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.report.MonthlySalesRow;
import com.coffeeshop.report.StockMovementRow;
import com.coffeeshop.report.TopProductRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    public List<MonthlySalesRow> getMonthlySales(int year) throws SQLException {
        String sql = """
            SELECT DATE_FORMAT(tanggal, '%Y-%m') AS bulan,
                   COUNT(*) AS jumlah_transaksi,
                   COALESCE(SUM(total), 0) AS total_penjualan
            FROM sales
            WHERE YEAR(tanggal) = ?
            GROUP BY DATE_FORMAT(tanggal, '%Y-%m')
            ORDER BY bulan
            """;

        List<MonthlySalesRow> rows = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new MonthlySalesRow(
                        rs.getString("bulan"),
                        rs.getInt("jumlah_transaksi"),
                        rs.getDouble("total_penjualan")
                    ));
                }
            }
        }
        return rows;
    }

    public List<TopProductRow> getTopProducts(LocalDate startDate, LocalDate endDate, int limit) throws SQLException {
        String sql = """
            SELECT p.id_product,
                   p.nama_produk,
                   COALESCE(SUM(sd.qty), 0) AS total_qty,
                   COALESCE(SUM(sd.subtotal_item), 0) AS total_revenue
            FROM sales_detail sd
            JOIN sales s ON s.id_sale = sd.id_sale
            JOIN products p ON p.id_product = sd.id_product
            WHERE DATE(s.tanggal) BETWEEN ? AND ?
            GROUP BY p.id_product, p.nama_produk
            ORDER BY total_qty DESC, total_revenue DESC
            LIMIT ?
            """;

        List<TopProductRow> rows = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            stmt.setInt(3, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new TopProductRow(
                        rs.getInt("id_product"),
                        rs.getString("nama_produk"),
                        rs.getInt("total_qty"),
                        rs.getDouble("total_revenue")
                    ));
                }
            }
        }
        return rows;
    }

    public List<StockMovementRow> getStockMovement() throws SQLException {
        String sql = """
            SELECT p.id_product,
                   p.nama_produk,
                   COALESCE(pin.total_in, 0) AS stock_in,
                   COALESCE(sout.total_out, 0) AS stock_out,
                   p.stok AS current_stock
            FROM products p
            LEFT JOIN (
                SELECT id_product, SUM(qty) AS total_in
                FROM purchase_detail
                GROUP BY id_product
            ) pin ON pin.id_product = p.id_product
            LEFT JOIN (
                SELECT id_product, SUM(qty) AS total_out
                FROM sales_detail
                GROUP BY id_product
            ) sout ON sout.id_product = p.id_product
            ORDER BY p.nama_produk
            """;

        List<StockMovementRow> rows = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add(new StockMovementRow(
                    rs.getInt("id_product"),
                    rs.getString("nama_produk"),
                    rs.getInt("stock_in"),
                    rs.getInt("stock_out"),
                    rs.getInt("current_stock")
                ));
            }
        }
        return rows;
    }
}
