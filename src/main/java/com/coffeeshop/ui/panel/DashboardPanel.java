package com.coffeeshop.ui.panel;

import com.coffeeshop.config.DatabaseManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Sistem Manajemen Coffee Shop", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22f));

        JButton testConnectionBtn = new JButton("Test Koneksi Database");
        testConnectionBtn.addActionListener(e -> testConnection());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(testConnectionBtn);

        add(title, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.CENTER);
    }

    private void testConnection() {
        try {
            DatabaseManager.testConnection();
            JOptionPane.showMessageDialog(this, "Koneksi database berhasil.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal: " + e.getMessage());
        }
    }
}
