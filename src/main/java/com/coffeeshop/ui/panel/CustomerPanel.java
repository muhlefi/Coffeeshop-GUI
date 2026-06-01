package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Customer;
import com.coffeeshop.service.CustomerService;
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

public class CustomerPanel extends JPanel {
    private final CustomerService customerService = new CustomerService();

    private final JTextField txtSearch = new JTextField(20);
    private final JTextField txtId = new JTextField(6);
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtPhone = new JTextField(15);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtAddress = new JTextField(20);

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {"ID", "Nama", "No HP", "Email", "Alamat"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public CustomerPanel() {
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

        topPanel.add(new JLabel("Search Nama Customer:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        formPanel.add(new JLabel("ID Customer"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nama"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("No HP"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Email"));
        formPanel.add(txtEmail);
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
                txtEmail.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtAddress.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<Customer> customers = customerService.getAll();
            fillTable(customers);
        } catch (SQLException e) {
            showError("Gagal memuat data customer: " + e.getMessage());
        }
    }

    private void searchData() {
        try {
            List<Customer> customers = customerService.search(txtSearch.getText());
            fillTable(customers);
        } catch (SQLException e) {
            showError("Gagal mencari data customer: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            Customer customer = buildCustomerFromForm(false);
            customerService.create(customer);
            JOptionPane.showMessageDialog(this, "Data customer berhasil disimpan.");
            clearForm();
            loadData();
        } catch (InputKosongException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateData() {
        try {
            Customer customer = buildCustomerFromForm(true);
            customerService.update(customer);
            JOptionPane.showMessageDialog(this, "Data customer berhasil diubah.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID customer tidak valid.");
        } catch (InputKosongException | DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void deleteData() {
        try {
            if (txtId.getText().isBlank()) {
                throw new DataTidakValidException("Pilih data yang ingin dihapus.");
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus customer ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            customerService.delete(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Data customer berhasil dihapus.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID customer tidak valid.");
        } catch (DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private Customer buildCustomerFromForm(boolean includeId) {
        Customer customer = new Customer();
        if (includeId) {
            customer.setId(Integer.parseInt(txtId.getText()));
        }
        customer.setName(txtName.getText());
        customer.setPhone(txtPhone.getText());
        customer.setEmail(txtEmail.getText());
        customer.setAddress(txtAddress.getText());
        return customer;
    }

    private void fillTable(List<Customer> customers) {
        tableModel.setRowCount(0);
        for (Customer c : customers) {
            tableModel.addRow(new Object[] {
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getAddress()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
