package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Supplier;
import com.coffeeshop.repository.SupplierRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    @Override
    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT id_supplier, nama_supplier, no_hp, alamat FROM suppliers ORDER BY id_supplier";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(mapRow(rs));
            }
        }
        return suppliers;
    }

    @Override
    public List<Supplier> searchByName(String keyword) throws SQLException {
        String sql = "SELECT id_supplier, nama_supplier, no_hp, alamat FROM suppliers WHERE nama_supplier LIKE ? ORDER BY id_supplier";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(mapRow(rs));
                }
            }
        }
        return suppliers;
    }

    @Override
    public Supplier findById(int id) throws SQLException {
        String sql = "SELECT id_supplier, nama_supplier, no_hp, alamat FROM suppliers WHERE id_supplier = ?";
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
    public void save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (nama_supplier, no_hp, alamat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getAddress());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET nama_supplier = ?, no_hp = ?, alamat = ? WHERE id_supplier = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getAddress());
            stmt.setInt(4, supplier.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id_supplier = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id_supplier"));
        supplier.setName(rs.getString("nama_supplier"));
        supplier.setPhone(rs.getString("no_hp"));
        supplier.setAddress(rs.getString("alamat"));
        return supplier;
    }
}
