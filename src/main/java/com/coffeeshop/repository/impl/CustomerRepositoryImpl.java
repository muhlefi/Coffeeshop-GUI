package com.coffeeshop.repository.impl;

import com.coffeeshop.config.DatabaseManager;
import com.coffeeshop.model.Customer;
import com.coffeeshop.repository.CustomerRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT id_customer, nama, no_hp, email, alamat FROM customers ORDER BY id_customer";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(mapRow(rs));
            }
        }
        return customers;
    }

    @Override
    public List<Customer> searchByName(String keyword) throws SQLException {
        String sql = "SELECT id_customer, nama, no_hp, email, alamat FROM customers WHERE nama LIKE ? ORDER BY id_customer";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapRow(rs));
                }
            }
        }
        return customers;
    }

    @Override
    public Customer findById(int id) throws SQLException {
        String sql = "SELECT id_customer, nama, no_hp, email, alamat FROM customers WHERE id_customer = ?";
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
    public void save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (nama, no_hp, email, alamat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET nama = ?, no_hp = ?, email = ?, alamat = ? WHERE id_customer = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5, customer.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id_customer = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id_customer"));
        customer.setName(rs.getString("nama"));
        customer.setPhone(rs.getString("no_hp"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("alamat"));
        return customer;
    }
}
