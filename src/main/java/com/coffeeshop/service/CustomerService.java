package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Customer;
import com.coffeeshop.repository.CustomerRepository;
import com.coffeeshop.repository.impl.CustomerRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepositoryImpl();
    }

    public List<Customer> getAll() throws SQLException {
        return customerRepository.findAll();
    }

    public List<Customer> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return customerRepository.searchByName(keyword.trim());
    }

    public void create(Customer customer) throws SQLException, InputKosongException {
        validate(customer);
        customerRepository.save(customer);
    }

    public void update(Customer customer) throws SQLException, InputKosongException, DataTidakValidException {
        if (customer.getId() <= 0) {
            throw new DataTidakValidException("ID customer tidak valid.");
        }
        validate(customer);
        customerRepository.update(customer);
    }

    public void delete(int id) throws SQLException, DataTidakValidException {
        if (id <= 0) {
            throw new DataTidakValidException("ID customer tidak valid.");
        }
        customerRepository.delete(id);
    }

    private void validate(Customer customer) throws InputKosongException {
        ValidationUtil.requireNotBlank(customer.getName(), "Nama customer");
        ValidationUtil.requireNotBlank(customer.getPhone(), "Nomor HP");
    }
}
