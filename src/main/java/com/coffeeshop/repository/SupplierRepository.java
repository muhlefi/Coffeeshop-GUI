package com.coffeeshop.repository;

import com.coffeeshop.model.Supplier;
import java.sql.SQLException;
import java.util.List;

public interface SupplierRepository {
    List<Supplier> findAll() throws SQLException;
    List<Supplier> searchByName(String keyword) throws SQLException;
    Supplier findById(int id) throws SQLException;
    void save(Supplier supplier) throws SQLException;
    void update(Supplier supplier) throws SQLException;
    void delete(int id) throws SQLException;
}
