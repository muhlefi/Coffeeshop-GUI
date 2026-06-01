package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Supplier;
import com.coffeeshop.repository.SupplierRepository;
import com.coffeeshop.repository.impl.SupplierRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService() {
        this.supplierRepository = new SupplierRepositoryImpl();
    }

    public List<Supplier> getAll() throws SQLException {
        return supplierRepository.findAll();
    }

    public List<Supplier> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return supplierRepository.searchByName(keyword.trim());
    }

    public void create(Supplier supplier) throws SQLException, InputKosongException {
        validate(supplier);
        supplierRepository.save(supplier);
    }

    public void update(Supplier supplier) throws SQLException, InputKosongException, DataTidakValidException {
        if (supplier.getId() <= 0) {
            throw new DataTidakValidException("ID supplier tidak valid.");
        }
        validate(supplier);
        supplierRepository.update(supplier);
    }

    public void delete(int id) throws SQLException, DataTidakValidException {
        if (id <= 0) {
            throw new DataTidakValidException("ID supplier tidak valid.");
        }
        supplierRepository.delete(id);
    }

    private void validate(Supplier supplier) throws InputKosongException {
        ValidationUtil.requireNotBlank(supplier.getName(), "Nama supplier");
        ValidationUtil.requireNotBlank(supplier.getPhone(), "Nomor HP");
        ValidationUtil.requireNotBlank(supplier.getAddress(), "Alamat");
    }
}
