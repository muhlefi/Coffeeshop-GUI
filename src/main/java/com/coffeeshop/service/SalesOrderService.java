package com.coffeeshop.service;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Product;
import com.coffeeshop.model.Sale;
import com.coffeeshop.model.SaleItem;
import com.coffeeshop.repository.SalesTransactionRepository;
import com.coffeeshop.repository.impl.SalesTransactionRepositoryImpl;
import com.coffeeshop.util.ValidationUtil;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SalesOrderService {
    private final SalesTransactionRepository salesTransactionRepository;
    private final ProductService productService;
    private final SalesTransactionService salesCalculator;

    public SalesOrderService() {
        this.salesTransactionRepository = new SalesTransactionRepositoryImpl();
        this.productService = new ProductService();
        this.salesCalculator = new SalesTransactionService();
    }

    public SaleItem buildItem(int productId, int quantity) throws SQLException, DataTidakValidException {
        if (quantity <= 0) {
            throw new DataTidakValidException("Qty harus lebih dari 0.");
        }
        Product product = productService.getById(productId);
        if (product == null) {
            throw new DataTidakValidException("Produk tidak ditemukan.");
        }
        if (product.getStock() < quantity) {
            throw new DataTidakValidException("Stok produk tidak mencukupi.");
        }
        return new SaleItem(product.getId(), quantity, product.getPrice());
    }

    public int createSale(
        int userId,
        Integer customerId,
        double discount,
        double taxPercent,
        List<SaleItem> items
    ) throws SQLException, DataTidakValidException, InputKosongException {
        if (userId <= 0) {
            throw new DataTidakValidException("User ID harus valid.");
        }
        if (customerId != null && customerId <= 0) {
            throw new DataTidakValidException("Customer ID tidak valid.");
        }
        if (items == null || items.isEmpty()) {
            throw new InputKosongException("Minimal ada 1 item dalam transaksi.");
        }
        if (discount < 0 || taxPercent < 0) {
            throw new DataTidakValidException("Diskon dan pajak tidak boleh negatif.");
        }

        for (SaleItem item : items) {
            ValidationUtil.requirePositive(item.getUnitPrice(), "Harga item");
            ValidationUtil.requireNonNegative(item.getQuantity(), "Qty item");
            if (item.getQuantity() == 0) {
                throw new DataTidakValidException("Qty item harus lebih dari 0.");
            }
        }

        double subtotal = salesCalculator.calculateSubtotal(items);
        if (discount > subtotal) {
            throw new DataTidakValidException("Diskon tidak boleh melebihi subtotal.");
        }
        double afterDiscount = subtotal - discount;
        double taxValue = afterDiscount * taxPercent / 100.0;
        double total = afterDiscount + taxValue;

        Sale sale = new Sale();
        sale.setTransactionDate(LocalDateTime.now());
        sale.setUserId(userId);
        sale.setCustomerId(customerId);
        sale.setSubtotal(subtotal);
        sale.setDiscount(discount);
        sale.setTax(taxValue);
        sale.setTotal(total);

        return salesTransactionRepository.save(sale, items);
    }

    public List<Sale> getRecentSales(int limit) throws SQLException {
        if (limit <= 0) {
            limit = 20;
        }
        return salesTransactionRepository.findRecent(limit);
    }
}
