package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Category;
import com.coffeeshop.repository.CategoryRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    @Override
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT id_category, nama_category, deskripsi FROM categories ORDER BY id_category";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        }
        return categories;
    }

    @Override
    public List<Category> searchByName(String keyword) throws SQLException {
        String sql = "SELECT id_category, nama_category, deskripsi FROM categories WHERE nama_category LIKE ? ORDER BY id_category";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapRow(rs));
                }
            }
        }
        return categories;
    }

    @Override
    public Category findById(int id) throws SQLException {
        String sql = "SELECT id_category, nama_category, deskripsi FROM categories WHERE id_category = ?";
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
    public void save(Category category) throws SQLException {
        String sql = "INSERT INTO categories (nama_category, deskripsi) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET nama_category = ?, deskripsi = ? WHERE id_category = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id_category = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id_category"));
        category.setName(rs.getString("nama_category"));
        category.setDescription(rs.getString("deskripsi"));
        return category;
    }
}
