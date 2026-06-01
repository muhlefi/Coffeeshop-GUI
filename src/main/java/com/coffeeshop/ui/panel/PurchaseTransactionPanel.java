package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.LookupItem;
import com.coffeeshop.model.Purchase;
import com.coffeeshop.model.PurchaseItem;
import com.coffeeshop.model.Product;
import com.coffeeshop.service.LookupService;
import com.coffeeshop.service.ProductService;
import com.coffeeshop.service.PurchaseOrderService;
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

public class PurchaseTransactionPanel extends JPanel {
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
    private final ProductService productService = new ProductService();
    private final LookupService lookupService = new LookupService();
    private final List<PurchaseItem> currentItems = new ArrayList<>();

    private final JComboBox<LookupItem> cmbUser = new JComboBox<>();
    private final JComboBox<LookupItem> cmbSupplier = new JComboBox<>();
    private final JComboBox<LookupItem> cmbProduct = new JComboBox<>();
    private final JTextField txtQty = new JTextField(6);
    private final JTextField txtUnitCost = new JTextField(10);

    private final JLabel lblGrandTotal = new JLabel("0");

    private final DefaultTableModel itemTableModel = new DefaultTableModel(
        new Object[] {"Produk ID", "Nama Produk", "Qty", "Harga Beli", "Subtotal"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable itemTable = new JTable(itemTableModel);

    private final DefaultTableModel recentTableModel = new DefaultTableModel(
        new Object[] {"ID Purchase", "Tanggal", "User", "Supplier", "Total"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable recentTable = new JTable(recentTableModel);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PurchaseTransactionPanel() {
        setLayout(new BorderLayout(10, 10));
        buildLayout();
        loadLookups();
        loadRecentPurchases();
    }

    private void buildLayout() {
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.add(buildHeaderForm(), BorderLayout.NORTH);
        leftPanel.add(buildItemSection(), BorderLayout.CENTER);
        leftPanel.add(buildSummarySection(), BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Riwayat Pembelian Terbaru"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(recentTable), BorderLayout.CENTER);
        recentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(780);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildHeaderForm() {
        JPanel formPanel = new JPanel(new GridLayout(1, 4, 8, 8));
        formPanel.add(new JLabel("User"));
        formPanel.add(cmbUser);
        formPanel.add(new JLabel("Supplier"));
        formPanel.add(cmbSupplier);
        return formPanel;
    }

    private JPanel buildItemSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));

        JPanel addItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAddItem = new JButton("Tambah Item");
        JButton btnRemoveItem = new JButton("Hapus Item");
        JButton btnClear = new JButton("Reset Form");
        JButton btnSave = new JButton("Simpan Pembelian");

        btnAddItem.addActionListener(e -> addItem());
        btnRemoveItem.addActionListener(e -> removeSelectedItem());
        btnClear.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveTransaction());

        addItemPanel.add(new JLabel("Produk"));
        addItemPanel.add(cmbProduct);
        addItemPanel.add(new JLabel("Qty"));
        addItemPanel.add(txtQty);
        addItemPanel.add(new JLabel("Harga Beli"));
        addItemPanel.add(txtUnitCost);
        addItemPanel.add(btnAddItem);
        addItemPanel.add(btnRemoveItem);
        addItemPanel.add(btnSave);
        addItemPanel.add(btnClear);

        section.add(addItemPanel, BorderLayout.NORTH);
        section.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        return section;
    }

    private JPanel buildSummarySection() {
        JPanel summary = new JPanel(new GridLayout(1, 2, 8, 8));
        summary.add(new JLabel("Grand Total Pembelian"));
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
            double unitCost = Double.parseDouble(txtUnitCost.getText().trim());

            PurchaseItem item = purchaseOrderService.buildItem(productId, qty, unitCost);
            Product product = productService.getById(productId);
            if (product == null) {
                throw new DataTidakValidException("Produk tidak ditemukan.");
            }

            currentItems.add(item);
            itemTableModel.addRow(new Object[] {
                productId,
                product.getName(),
                qty,
                CurrencyUtil.format(unitCost),
                CurrencyUtil.format(qty * unitCost)
            });
            txtQty.setText("");
            txtUnitCost.setText("");
            refreshSummary();
        } catch (NumberFormatException e) {
            showError("Qty dan Harga Beli harus berupa angka.");
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
            LookupItem selectedSupplier = (LookupItem) cmbSupplier.getSelectedItem();
            if (selectedUser == null || selectedUser.getId() == null) {
                throw new DataTidakValidException("Pilih user terlebih dahulu.");
            }
            if (selectedSupplier == null || selectedSupplier.getId() == null) {
                throw new DataTidakValidException("Pilih supplier terlebih dahulu.");
            }

            int userId = selectedUser.getId();
            int supplierId = selectedSupplier.getId();
            int purchaseId = purchaseOrderService.createPurchase(userId, supplierId, currentItems);
            JOptionPane.showMessageDialog(this, "Transaksi pembelian berhasil disimpan. ID Purchase: " + purchaseId);
            clearForm();
            loadRecentPurchases();
        } catch (SQLException | DataTidakValidException | InputKosongException e) {
            showError(e.getMessage());
        }
    }

    private void loadRecentPurchases() {
        try {
            List<Purchase> purchases = purchaseOrderService.getRecentPurchases(20);
            recentTableModel.setRowCount(0);
            for (Purchase purchase : purchases) {
                recentTableModel.addRow(new Object[] {
                    purchase.getId(),
                    purchase.getTransactionDate().format(dateFormatter),
                    purchase.getUserId(),
                    purchase.getSupplierId(),
                    purchase.getTotal()
                });
            }
        } catch (SQLException e) {
            showError("Gagal memuat riwayat pembelian: " + e.getMessage());
        }
    }

    private void refreshSummary() {
        double total = currentItems.stream()
            .mapToDouble(item -> item.getUnitCost() * item.getQuantity())
            .sum();
        lblGrandTotal.setText(CurrencyUtil.format(total));
    }

    private void clearForm() {
        if (cmbUser.getItemCount() > 0) {
            cmbUser.setSelectedIndex(0);
        }
        if (cmbSupplier.getItemCount() > 0) {
            cmbSupplier.setSelectedIndex(0);
        }
        if (cmbProduct.getItemCount() > 0) {
            cmbProduct.setSelectedIndex(0);
        }
        txtQty.setText("");
        txtUnitCost.setText("");
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

            cmbSupplier.removeAllItems();
            for (LookupItem item : lookupService.getSuppliers()) {
                cmbSupplier.addItem(item);
            }

            cmbProduct.removeAllItems();
            List<LookupItem> products = lookupService.getProducts();
            for (LookupItem item : products) {
                cmbProduct.addItem(item);
            }

            cmbProduct.addActionListener(e -> prefillDefaultCost());
            prefillDefaultCost();
        } catch (SQLException e) {
            showError("Gagal memuat data lookup transaksi: " + e.getMessage());
        }
    }

    private void prefillDefaultCost() {
        LookupItem selectedProduct = (LookupItem) cmbProduct.getSelectedItem();
        if (selectedProduct == null || selectedProduct.getId() == null) {
            return;
        }
        try {
            Product product = productService.getById(selectedProduct.getId());
            if (product != null && txtUnitCost.getText().isBlank()) {
                txtUnitCost.setText(String.valueOf(product.getPrice()));
            }
        } catch (SQLException | DataTidakValidException ignored) {
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
