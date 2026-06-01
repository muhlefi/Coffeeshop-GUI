package com.coffeeshop.repository;

import com.coffeeshop.model.Customer;
import java.sql.SQLException;
import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll() throws SQLException;
    List<Customer> searchByName(String keyword) throws SQLException;
    Customer findById(int id) throws SQLException;
    void save(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;
    void delete(int id) throws SQLException;
}
