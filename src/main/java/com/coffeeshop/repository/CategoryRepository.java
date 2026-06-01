package com.coffeeshop.repository;

import com.coffeeshop.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll() throws SQLException;
    List<Category> searchByName(String keyword) throws SQLException;
    Category findById(int id) throws SQLException;
    void save(Category category) throws SQLException;
    void update(Category category) throws SQLException;
    void delete(int id) throws SQLException;
}
