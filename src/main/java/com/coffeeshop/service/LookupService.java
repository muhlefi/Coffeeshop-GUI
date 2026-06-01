package com.coffeeshop.service;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.LookupItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LookupService {
    public List<LookupItem> getUsers() throws SQLException {
        String sql = "SELECT id_user, nama FROM users WHERE status = TRUE ORDER BY nama";
        return executeLookup(sql, "id_user", "nama");
    }

    public List<LookupItem> getCustomers() throws SQLException {
        String sql = "SELECT id_customer, nama FROM customers ORDER BY nama";
        return executeLookup(sql, "id_customer", "nama");
    }

    public List<LookupItem> getSuppliers() throws SQLException {
        String sql = "SELECT id_supplier, nama_supplier FROM suppliers ORDER BY nama_supplier";
        return executeLookup(sql, "id_supplier", "nama_supplier");
    }

    public List<LookupItem> getCategories() throws SQLException {
        String sql = "SELECT id_category, nama_category FROM categories ORDER BY nama_category";
        return executeLookup(sql, "id_category", "nama_category");
    }

    public List<LookupItem> getProducts() throws SQLException {
        String sql = "SELECT id_product, nama_produk FROM products WHERE status = TRUE ORDER BY nama_produk";
        return executeLookup(sql, "id_product", "nama_produk");
    }

    private List<LookupItem> executeLookup(String sql, String idColumn, String labelColumn) throws SQLException {
        List<LookupItem> items = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt(idColumn);
                String label = rs.getString(labelColumn);
                items.add(new LookupItem(id, id + " - " + label));
            }
        }
        return items;
    }
}
