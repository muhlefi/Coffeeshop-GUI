package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Category;
import com.coffeeshop.repository.CategoryRepository;
import com.coffeeshop.repository.impl.CategoryRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepositoryImpl();
    }

    public List<Category> getAll() throws SQLException {
        return categoryRepository.findAll();
    }

    public List<Category> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return categoryRepository.searchByName(keyword.trim());
    }

    public void create(Category category) throws SQLException, InputKosongException {
        validate(category);
        categoryRepository.save(category);
    }

    public void update(Category category) throws SQLException, InputKosongException, DataTidakValidException {
        if (category.getId() <= 0) {
            throw new DataTidakValidException("ID kategori tidak valid.");
        }
        validate(category);
        categoryRepository.update(category);
    }

    public void delete(int id) throws SQLException, DataTidakValidException {
        if (id <= 0) {
            throw new DataTidakValidException("ID kategori tidak valid.");
        }
        categoryRepository.delete(id);
    }

    private void validate(Category category) throws InputKosongException {
        ValidationUtil.requireNotBlank(category.getName(), "Nama kategori");
    }
}
