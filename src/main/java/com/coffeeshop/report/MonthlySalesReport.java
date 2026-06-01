package com.coffeeshop.report;

import com.coffeeshop.service.ReportService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MonthlySalesReport implements ReportGenerator {
    private final ReportService reportService = new ReportService();

    @Override
    public String generate() throws SQLException {
        int year = LocalDate.now().getYear();
        List<MonthlySalesRow> rows = reportService.getMonthlySales(year);
        if (rows.isEmpty()) {
            return "Belum ada data penjualan untuk tahun " + year + ".";
        }

        StringBuilder builder = new StringBuilder("Laporan Penjualan Bulanan ").append(year).append("\n");
        for (MonthlySalesRow row : rows) {
            builder.append(row.getMonthLabel())
                .append(" | transaksi: ")
                .append(row.getTransactionCount())
                .append(" | total: ")
                .append(row.getTotalSales())
                .append("\n");
        }
        return builder.toString();
    }
}
