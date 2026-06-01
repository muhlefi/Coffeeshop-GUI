package com.coffeeshop.ui.panel;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;
import com.coffeeshop.model.Category;
import com.coffeeshop.service.CategoryService;
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

public class CategoryPanel extends JPanel {
    private final CategoryService categoryService = new CategoryService();

    private final JTextField txtSearch = new JTextField(20);
    private final JTextField txtId = new JTextField(6);
    private final JTextField txtName = new JTextField(25);
    private final JTextField txtDescription = new JTextField(30);

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {"ID", "Nama Kategori", "Deskripsi"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public CategoryPanel() {
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

        topPanel.add(new JLabel("Search Nama Kategori:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.add(new JLabel("ID Kategori"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nama Kategori"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Deskripsi"));
        formPanel.add(txtDescription);

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
                txtDescription.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<Category> categories = categoryService.getAll();
            fillTable(categories);
        } catch (SQLException e) {
            showError("Gagal memuat data kategori: " + e.getMessage());
        }
    }

    private void searchData() {
        try {
            List<Category> categories = categoryService.search(txtSearch.getText());
            fillTable(categories);
        } catch (SQLException e) {
            showError("Gagal mencari data kategori: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            Category category = buildCategoryFromForm(false);
            categoryService.create(category);
            JOptionPane.showMessageDialog(this, "Data kategori berhasil disimpan.");
            clearForm();
            loadData();
        } catch (InputKosongException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateData() {
        try {
            Category category = buildCategoryFromForm(true);
            categoryService.update(category);
            JOptionPane.showMessageDialog(this, "Data kategori berhasil diubah.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID kategori tidak valid.");
        } catch (InputKosongException | DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private void deleteData() {
        try {
            if (txtId.getText().isBlank()) {
                throw new DataTidakValidException("Pilih data yang ingin dihapus.");
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus kategori ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            categoryService.delete(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Data kategori berhasil dihapus.");
            clearForm();
            loadData();
        } catch (NumberFormatException e) {
            showError("ID tidak valid.");
        } catch (DataTidakValidException | SQLException e) {
            showError(e.getMessage());
        }
    }

    private Category buildCategoryFromForm(boolean includeId) {
        Category category = new Category();
        if (includeId) {
            category.setId(Integer.parseInt(txtId.getText()));
        }
        category.setName(txtName.getText());
        category.setDescription(txtDescription.getText());
        return category;
    }

    private void fillTable(List<Category> categories) {
        tableModel.setRowCount(0);
        for (Category c : categories) {
            tableModel.addRow(new Object[] {
                c.getId(),
                c.getName(),
                c.getDescription()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
