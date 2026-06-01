package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Supplier;
import com.coffeeshop.service.SupplierService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class SupplierPanel extends JPanel {
    private final SupplierService supplierService = new SupplierService();

    private final JTextField txtSearch = new JTextField(20);
    private final JTextField txtId = new JTextField(6);
    private final JTextField txtName = new JTextField(25);
    private final JTextField txtPhone = new JTextField(15);
    private final JTextField txtAddress = new JTextField(30);

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {"ID", "Nama Supplier", "No HP", "Alamat"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public SupplierPanel() {
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

        topPanel.add(new JLabel("Search Nama Supplier:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.add(new JLabel("ID Supplier"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nama Supplier"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("No HP"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Alamat"));
        formPanel.add(txtAddress);

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
                txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtPhone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtAddress.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<Supplier> suppliers = supplierService.getAll();
            fillTable(suppliers);
        } catch (SQLException e) {
            showError("Gagal memuat data supplier: " + e.getMessage());
        }
    }

    private void searchData() {
        try {
            List<Supplier> suppliers = supplierService.search(txtSearch.getText());
            fillTable(suppliers);
        } catch (SQLException e) {
            showError("Gagal mencari data supplier: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            Supplier supplier = buildSupplierFromForm(false);
            supplierService.create(supplier);
            JOptionPane.showMessageDialog(this, "Data supplier berhasil disimpan.");
            clearForm();
            loadData();
        } catch (InputKosongException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateData() {
        try {
            Supplier supplier = buildSupplierFromForm(true);
            supplierService.update(supplier);
            JOptionPane.showMessageDialog(this, "Data supplier berhasil diubah.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID supplier tidak valid.");
        } catch (InputKosongException | DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void deleteData() {
        try {
            if (txtId.getText().isBlank()) {
                throw new DataTidakValidException("Pilih data yang ingin dihapus.");
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus supplier ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            supplierService.delete(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Data supplier berhasil dihapus.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID tidak valid.");
        } catch (DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private Supplier buildSupplierFromForm(boolean includeId) {
        Supplier supplier = new Supplier();
        if (includeId) {
            supplier.setId(Integer.parseInt(txtId.getText()));
        }
        supplier.setName(txtName.getText());
        supplier.setPhone(txtPhone.getText());
        supplier.setAddress(txtAddress.getText());
        return supplier;
    }

    private void fillTable(List<Supplier> suppliers) {
        tableModel.setRowCount(0);
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[] {
                s.getId(),
                s.getName(),
                s.getPhone(),
                s.getAddress()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
