package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Product;
import com.coffeeshop.repository.ProductRepository;
import com.coffeeshop.repository.impl.ProductRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepositoryImpl();
    }

    public List<Product> getAll() throws SQLException {
        return productRepository.findAll();
    }

    public List<Product> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return productRepository.searchByName(keyword.trim());
    }

    public Product getById(int id) throws SQLException, DataTidakValidException {
        if (id <= 0) {
            throw new DataTidakValidException("ID produk tidak valid.");
        }
        return productRepository.findById(id);
    }

    public void create(Product product) throws SQLException, InputKosongException, DataTidakValidException {
        validate(product);
        productRepository.save(product);
    }

    public void update(Product product) throws SQLException, InputKosongException, DataTidakValidException {
        if (product.getId() <= 0) {
            throw new DataTidakValidException("ID produk tidak valid.");
        }
        validate(product);
        productRepository.update(product);
    }

    public void delete(int id) throws SQLException, DataTidakValidException {
        if (id <= 0) {
            throw new DataTidakValidException("ID produk tidak valid.");
        }
        productRepository.delete(id);
    }

    private void validate(Product product) throws InputKosongException, DataTidakValidException {
        ValidationUtil.requireNotBlank(product.getName(), "Nama produk");
        ValidationUtil.requirePositive(product.getPrice(), "Harga");
        ValidationUtil.requireNonNegative(product.getStock(), "Stok");
        if (product.getCategoryId() <= 0) {
            throw new DataTidakValidException("Kategori harus dipilih.");
        }
    }
}
