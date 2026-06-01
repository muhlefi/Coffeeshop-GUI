package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.LookupItem;
import com.coffeeshop.model.Product;
import com.coffeeshop.model.Sale;
import com.coffeeshop.model.SaleItem;
import com.coffeeshop.service.LookupService;
import com.coffeeshop.service.ProductService;
import com.coffeeshop.service.SalesOrderService;
import com.coffeeshop.util.CurrencyUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class SalesTransactionPanel extends JPanel {
    private final SalesOrderService salesOrderService = new SalesOrderService();
    private final ProductService productService = new ProductService();
    private final LookupService lookupService = new LookupService();
    private final List<SaleItem> currentItems = new ArrayList<>();

    private final JComboBox<LookupItem> cmbUser = new JComboBox<>();
    private final JComboBox<LookupItem> cmbCustomer = new JComboBox<>();
    private final JComboBox<LookupItem> cmbProduct = new JComboBox<>();
    private final JTextField txtDiscount = new JTextField("0", 8);
    private final JTextField txtTaxPercent = new JTextField("11", 5);
    private final JTextField txtQty = new JTextField(6);

    private final JLabel lblSubtotal = new JLabel("0");
    private final JLabel lblTaxValue = new JLabel("0");
    private final JLabel lblGrandTotal = new JLabel("0");

    private final DefaultTableModel itemTableModel = new DefaultTableModel(
        new Object[] {"Produk ID", "Nama Produk", "Qty", "Harga", "Subtotal"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable itemTable = new JTable(itemTableModel);

    private final DefaultTableModel recentTableModel = new DefaultTableModel(
        new Object[] {"ID Sale", "Tanggal", "User", "Customer", "Total"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable recentTable = new JTable(recentTableModel);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SalesTransactionPanel() {
        setLayout(new BorderLayout(10, 10));
        buildLayout();
        loadLookups();
        loadRecentSales();
    }

    private void buildLayout() {
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.add(buildHeaderForm(), BorderLayout.NORTH);
        leftPanel.add(buildItemSection(), BorderLayout.CENTER);
        leftPanel.add(buildSummarySection(), BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Riwayat Penjualan Terbaru"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(recentTable), BorderLayout.CENTER);
        recentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(780);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildHeaderForm() {
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 8, 8));
        formPanel.add(new JLabel("User"));
        formPanel.add(cmbUser);
        formPanel.add(new JLabel("Customer (opsional)"));
        formPanel.add(cmbCustomer);
        formPanel.add(new JLabel("Diskon (Rp)"));
        formPanel.add(txtDiscount);
        formPanel.add(new JLabel("Pajak (%)"));
        formPanel.add(txtTaxPercent);
        return formPanel;
    }

    private JPanel buildItemSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));

        JPanel addItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAddItem = new JButton("Tambah Item");
        JButton btnRemoveItem = new JButton("Hapus Item");
        JButton btnClear = new JButton("Reset Form");
        JButton btnSave = new JButton("Simpan Transaksi");

        btnAddItem.addActionListener(e -> addItem());
        btnRemoveItem.addActionListener(e -> removeSelectedItem());
        btnClear.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveTransaction());

        addItemPanel.add(new JLabel("Produk"));
        addItemPanel.add(cmbProduct);
        addItemPanel.add(new JLabel("Qty"));
        addItemPanel.add(txtQty);
        addItemPanel.add(btnAddItem);
        addItemPanel.add(btnRemoveItem);
        addItemPanel.add(btnSave);
        addItemPanel.add(btnClear);

        section.add(addItemPanel, BorderLayout.NORTH);
        section.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        return section;
    }

    private JPanel buildSummarySection() {
        JPanel summary = new JPanel(new GridLayout(1, 6, 8, 8));
        summary.add(new JLabel("Subtotal"));
        summary.add(lblSubtotal);
        summary.add(new JLabel("Nilai Pajak"));
        summary.add(lblTaxValue);
        summary.add(new JLabel("Grand Total"));
        summary.add(lblGrandTotal);
        return summary;
    }

    private void addItem() {
        try {
            LookupItem selectedProduct = (LookupItem) cmbProduct.getSelectedItem();
            if (selectedProduct == null || selectedProduct.getId() == null) {
                throw new DataTidakValidException("Pilih produk terlebih dahulu.");
            }
            int productId = selectedProduct.getId();
            int qty = Integer.parseInt(txtQty.getText().trim());
            SaleItem item = salesOrderService.buildItem(productId, qty);
            Product product = productService.getById(productId);
            if (product == null) {
                throw new DataTidakValidException("Produk tidak ditemukan.");
            }

            currentItems.add(item);
            itemTableModel.addRow(new Object[] {
                productId,
                product.getName(),
                qty,
                CurrencyUtil.format(item.getUnitPrice()),
                CurrencyUtil.format(item.getUnitPrice() * qty)
            });
            txtQty.setText("");
            refreshSummary();
        } catch (NumberFormatException e) {
            showError("Qty harus berupa angka.");
        } catch (SQLException | DataTidakValidException e) {
            showError(e.getMessage());
        }
    }

    private void removeSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0) {
            showError("Pilih item yang ingin dihapus.");
            return;
        }
        currentItems.remove(row);
        itemTableModel.removeRow(row);
        refreshSummary();
    }

    private void saveTransaction() {
        try {
            LookupItem selectedUser = (LookupItem) cmbUser.getSelectedItem();
            if (selectedUser == null || selectedUser.getId() == null) {
                throw new DataTidakValidException("Pilih user terlebih dahulu.");
            }
            LookupItem selectedCustomer = (LookupItem) cmbCustomer.getSelectedItem();

            int userId = selectedUser.getId();
            Integer customerId = selectedCustomer == null ? null : selectedCustomer.getId();
            double discount = txtDiscount.getText().isBlank() ? 0 : Double.parseDouble(txtDiscount.getText().trim());
            double taxPercent = txtTaxPercent.getText().isBlank() ? 0 : Double.parseDouble(txtTaxPercent.getText().trim());

            int saleId = salesOrderService.createSale(userId, customerId, discount, taxPercent, currentItems);
            JOptionPane.showMessageDialog(this, "Transaksi penjualan berhasil disimpan. ID Sale: " + saleId);
            clearForm();
            loadRecentSales();
        } catch (NumberFormatException e) {
            showError("Input numerik pada form transaksi tidak valid.");
        } catch (SQLException | DataTidakValidException | InputKosongException e) {
            showError(e.getMessage());
        }
    }

    private void loadRecentSales() {
        try {
            List<Sale> sales = salesOrderService.getRecentSales(20);
            recentTableModel.setRowCount(0);
            for (Sale sale : sales) {
                recentTableModel.addRow(new Object[] {
                    sale.getId(),
                    sale.getTransactionDate().format(dateFormatter),
                    sale.getUserId(),
                    sale.getCustomerId() == null ? "-" : sale.getCustomerId(),
                    sale.getTotal()
                });
            }
        } catch (SQLException e) {
            showError("Gagal memuat riwayat penjualan: " + e.getMessage());
        }
    }

    private void refreshSummary() {
        double subtotal = currentItems.stream()
            .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
            .sum();
        double discount = parseDoubleOrZero(txtDiscount.getText());
        double taxPercent = parseDoubleOrZero(txtTaxPercent.getText());
        double afterDiscount = Math.max(0, subtotal - discount);
        double taxValue = afterDiscount * taxPercent / 100.0;
        double total = afterDiscount + taxValue;

        lblSubtotal.setText(CurrencyUtil.format(subtotal));
        lblTaxValue.setText(CurrencyUtil.format(taxValue));
        lblGrandTotal.setText(CurrencyUtil.format(total));
    }

    private double parseDoubleOrZero(String value) {
        try {
            if (value == null || value.isBlank()) {
                return 0;
            }
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void clearForm() {
        if (cmbUser.getItemCount() > 0) {
            cmbUser.setSelectedIndex(0);
        }
        cmbCustomer.setSelectedIndex(0);
        if (cmbProduct.getItemCount() > 0) {
            cmbProduct.setSelectedIndex(0);
        }
        txtDiscount.setText("0");
        txtTaxPercent.setText("11");
        txtQty.setText("");
        currentItems.clear();
        itemTableModel.setRowCount(0);
        refreshSummary();
    }

    private void loadLookups() {
        try {
            cmbUser.removeAllItems();
            for (LookupItem item : lookupService.getUsers()) {
                cmbUser.addItem(item);
            }

            cmbCustomer.removeAllItems();
            cmbCustomer.addItem(new LookupItem(null, "- Tanpa Customer -"));
            for (LookupItem item : lookupService.getCustomers()) {
                cmbCustomer.addItem(item);
            }

            cmbProduct.removeAllItems();
            for (LookupItem item : lookupService.getProducts()) {
                cmbProduct.addItem(item);
            }
        } catch (SQLException e) {
            showError("Gagal memuat data lookup transaksi: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
