package com.coffeeshop.repository;

import com.coffeeshop.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll() throws SQLException;
    List<Product> searchByName(String keyword) throws SQLException;
    Product findById(int id) throws SQLException;
    void save(Product product) throws SQLException;
    void update(Product product) throws SQLException;
    void delete(int id) throws SQLException;
}
