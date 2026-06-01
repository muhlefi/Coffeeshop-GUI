package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Product;
import com.coffeeshop.repository.ProductRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT p.id_product, p.id_category, p.nama_produk, p.harga, p.stok, p.status, c.nama_category FROM products p LEFT JOIN categories c ON p.id_category = c.id_category ORDER BY p.id_product";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    @Override
    public List<Product> searchByName(String keyword) throws SQLException {
        String sql = "SELECT p.id_product, p.id_category, p.nama_produk, p.harga, p.stok, p.status, c.nama_category FROM products p LEFT JOIN categories c ON p.id_category = c.id_category WHERE p.nama_produk LIKE ? ORDER BY p.id_product";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }
        }
        return products;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String sql = "SELECT p.id_product, p.id_category, p.nama_produk, p.harga, p.stok, p.status, c.nama_category FROM products p LEFT JOIN categories c ON p.id_category = c.id_category WHERE p.id_product = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Product product) throws SQLException {
        String sql = "INSERT INTO products (id_category, nama_produk, harga, stok, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setBoolean(5, product.isActive());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET id_category = ?, nama_produk = ?, harga = ?, stok = ?, status = ? WHERE id_product = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setBoolean(5, product.isActive());
            stmt.setInt(6, product.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id_product = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id_product"));
        product.setCategoryId(rs.getInt("id_category"));
        product.setCategoryName(rs.getString("nama_category"));
        product.setName(rs.getString("nama_produk"));
        product.setPrice(rs.getDouble("harga"));
        product.setStock(rs.getInt("stok"));
        product.setActive(rs.getBoolean("status"));
        return product;
    }
}
