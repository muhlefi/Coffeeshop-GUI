package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Product;
import com.coffeeshop.service.ProductService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ProductPanel extends JPanel {
    private final ProductService productService = new ProductService();

    private final JTextField txtSearch = new JTextField(20);
    private final JTextField txtId = new JTextField(6);
    private final JTextField txtCategoryId = new JTextField(6);
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtPrice = new JTextField(12);
    private final JTextField txtStock = new JTextField(8);
    private final JCheckBox chkActive = new JCheckBox("Aktif");

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {"ID", "Kategori", "Nama Produk", "Harga", "Stok", "Status"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public ProductPanel() {
        setLayout(new BorderLayout(12, 12));
        txtId.setEditable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initTopPanel();
        initFormPanel();
        initTable();
        loadData();
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSearch = new JButton("Cari");
        JButton btnRefresh = new JButton("Refresh");

        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });

        topPanel.add(new JLabel("Search Nama Produk:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        formPanel.add(new JLabel("ID Produk"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("ID Kategori"));
        formPanel.add(txtCategoryId);
        formPanel.add(new JLabel("Nama Produk"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Harga"));
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Stok"));
        formPanel.add(txtStock);
        formPanel.add(new JLabel("Status"));
        formPanel.add(chkActive);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSave = new JButton("Simpan");
        JButton btnUpdate = new JButton("Ubah");
        JButton btnDelete = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        btnSave.addActionListener(e -> saveData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnReset.addActionListener(e -> clearForm());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReset);

        JPanel container = new JPanel(new BorderLayout());
        container.add(formPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        add(container, BorderLayout.WEST);
    }

    private void initTable() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                txtId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                txtCategoryId.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtName.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtPrice.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtStock.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                chkActive.setSelected((Boolean) tableModel.getValueAt(row, 5));
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<Product> products = productService.getAll();
            fillTable(products);
        } catch (SQLException e) {
            showError("Gagal memuat data produk: " + e.getMessage());
        }
    }

    private void searchData() {
        try {
            List<Product> products = productService.search(txtSearch.getText());
            fillTable(products);
        } catch (SQLException e) {
            showError("Gagal mencari data produk: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            Product product = buildProductFromForm(false);
            productService.create(product);
            JOptionPane.showMessageDialog(this, "Data produk berhasil disimpan.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("Input angka tidak valid.");
        } catch (InputKosongException | DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateData() {
        try {
            Product product = buildProductFromForm(true);
            productService.update(product);
            JOptionPane.showMessageDialog(this, "Data produk berhasil diubah.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("Input angka tidak valid.");
        } catch (InputKosongException | DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void deleteData() {
        try {
            if (txtId.getText().isBlank()) {
                throw new DataTidakValidException("Pilih data yang ingin dihapus.");
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            productService.delete(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Data produk berhasil dihapus.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID tidak valid.");
        } catch (DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private Product buildProductFromForm(boolean includeId) {
        Product product = new Product();
        if (includeId) {
            product.setId(Integer.parseInt(txtId.getText()));
        }
        product.setCategoryId(Integer.parseInt(txtCategoryId.getText()));
        product.setName(txtName.getText());
        product.setPrice(Double.parseDouble(txtPrice.getText()));
        product.setStock(Integer.parseInt(txtStock.getText()));
        product.setActive(chkActive.isSelected());
        return product;
    }

    private void fillTable(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[] {
                p.getId(),
                p.getCategoryId(),
                p.getName(),
                p.getPrice(),
                p.getStock(),
                p.isActive()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtCategoryId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        chkActive.setSelected(false);
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
