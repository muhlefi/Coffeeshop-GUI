package com.coffeeshop.ui.panel;

import com.coffeeshop.report.MonthlySalesRow;
import com.coffeeshop.report.StockMovementRow;
import com.coffeeshop.report.TopProductRow;
import com.coffeeshop.service.ReportService;
import com.coffeeshop.util.CurrencyUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ReportPanel extends JPanel {
    private final ReportService reportService = new ReportService();

    private final JTextField txtYear = new JTextField(String.valueOf(LocalDate.now().getYear()), 6);
    private final JTextField txtStartDate = new JTextField(LocalDate.now().minusDays(30).toString(), 10);
    private final JTextField txtEndDate = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField txtTopLimit = new JTextField("10", 4);

    private final DefaultTableModel monthlyModel = new DefaultTableModel(
        new Object[]{"Bulan", "Jumlah Transaksi", "Total Penjualan"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel topProductModel = new DefaultTableModel(
        new Object[]{"Produk ID", "Nama Produk", "Total Qty", "Total Revenue"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel stockMovementModel = new DefaultTableModel(
        new Object[]{"Produk ID", "Nama Produk", "Stok Masuk", "Stok Keluar", "Stok Saat Ini"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        add(buildFilterPanel(), BorderLayout.NORTH);
        add(buildReportTabs(), BorderLayout.CENTER);
        refreshAllReports();
    }

    private JPanel buildFilterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 8, 8));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLoadMonthly = new JButton("Load Penjualan Bulanan");
        btnLoadMonthly.addActionListener(e -> loadMonthlySales());
        row1.add(new JLabel("Tahun"));
        row1.add(txtYear);
        row1.add(btnLoadMonthly);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLoadTopProduct = new JButton("Load Top Produk");
        JButton btnLoadStock = new JButton("Load Pergerakan Stok");
        JButton btnLoadAll = new JButton("Refresh Semua");

        btnLoadTopProduct.addActionListener(e -> loadTopProducts());
        btnLoadStock.addActionListener(e -> loadStockMovement());
        btnLoadAll.addActionListener(e -> refreshAllReports());

        row2.add(new JLabel("Start (yyyy-MM-dd)"));
        row2.add(txtStartDate);
        row2.add(new JLabel("End"));
        row2.add(txtEndDate);
        row2.add(new JLabel("Top N"));
        row2.add(txtTopLimit);
        row2.add(btnLoadTopProduct);
        row2.add(btnLoadStock);
        row2.add(btnLoadAll);

        panel.add(row1);
        panel.add(row2);
        return panel;
    }

    private JTabbedPane buildReportTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Penjualan Bulanan", new JScrollPane(new JTable(monthlyModel)));
        tabs.addTab("Produk Terlaris", new JScrollPane(new JTable(topProductModel)));
        tabs.addTab("Pergerakan Stok", new JScrollPane(new JTable(stockMovementModel)));
        return tabs;
    }

    private void refreshAllReports() {
        loadMonthlySales();
        loadTopProducts();
        loadStockMovement();
    }

    private void loadMonthlySales() {
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            List<MonthlySalesRow> rows = reportService.getMonthlySales(year);

            monthlyModel.setRowCount(0);
            for (MonthlySalesRow row : rows) {
                monthlyModel.addRow(new Object[]{
                    row.getMonthLabel(),
                    row.getTransactionCount(),
                    CurrencyUtil.format(row.getTotalSales())
                });
            }
        } catch (NumberFormatException e) {
            showError("Tahun harus angka.");
        } catch (SQLException e) {
            showError("Gagal memuat laporan penjualan bulanan: " + e.getMessage());
        }
    }

    private void loadTopProducts() {
        try {
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());
            int limit = Integer.parseInt(txtTopLimit.getText().trim());
            List<TopProductRow> rows = reportService.getTopProducts(start, end, limit);

            topProductModel.setRowCount(0);
            for (TopProductRow row : rows) {
                topProductModel.addRow(new Object[]{
                    row.getProductId(),
                    row.getProductName(),
                    row.getTotalQty(),
                    CurrencyUtil.format(row.getTotalRevenue())
                });
            }
        } catch (Exception e) {
            showError("Gagal memuat laporan top produk: " + e.getMessage());
        }
    }

    private void loadStockMovement() {
        try {
            List<StockMovementRow> rows = reportService.getStockMovement();
            stockMovementModel.setRowCount(0);
            for (StockMovementRow row : rows) {
                stockMovementModel.addRow(new Object[]{
                    row.getProductId(),
                    row.getProductName(),
                    row.getStockIn(),
                    row.getStockOut(),
                    row.getCurrentStock()
                });
            }
        } catch (SQLException e) {
            showError("Gagal memuat laporan pergerakan stok: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
