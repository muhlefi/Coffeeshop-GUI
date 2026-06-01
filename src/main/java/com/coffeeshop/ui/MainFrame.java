package com.coffeeshop.ui;

import com.coffeeshop.ui.panel.CategoryPanel;
import com.coffeeshop.ui.panel.CustomerPanel;
import com.coffeeshop.ui.panel.DashboardPanel;
import com.coffeeshop.ui.panel.PurchaseTransactionPanel;
import com.coffeeshop.ui.panel.ProductPanel;
import com.coffeeshop.ui.panel.ReportPanel;
import com.coffeeshop.ui.panel.SalesTransactionPanel;
import com.coffeeshop.ui.panel.SupplierPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Coffee Shop Management");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Master Produk", new ProductPanel());
        tabs.addTab("Master Kategori", new CategoryPanel());
        tabs.addTab("Master Supplier", new SupplierPanel());
        tabs.addTab("Master Customer", new CustomerPanel());
        tabs.addTab("Transaksi Penjualan", new SalesTransactionPanel());
        tabs.addTab("Transaksi Pembelian", new PurchaseTransactionPanel());
        tabs.addTab("Report", new ReportPanel());

        add(tabs, BorderLayout.CENTER);
    }
}
