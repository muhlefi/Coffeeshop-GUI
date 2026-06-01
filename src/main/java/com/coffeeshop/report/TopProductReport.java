package com.coffeeshop.report;

import com.coffeeshop.service.ReportService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TopProductReport implements ReportGenerator {
    private final ReportService reportService = new ReportService();

    @Override
    public String generate() throws SQLException {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(30);
        List<TopProductRow> rows = reportService.getTopProducts(start, end, 10);
        if (rows.isEmpty()) {
            return "Belum ada data produk terlaris pada periode " + start + " s.d. " + end + ".";
        }

        StringBuilder builder = new StringBuilder("Top Produk ").append(start).append(" s.d. ").append(end).append("\n");
        for (TopProductRow row : rows) {
            builder.append(row.getProductId())
                .append(" - ")
                .append(row.getProductName())
                .append(" | qty: ")
                .append(row.getTotalQty())
                .append(" | revenue: ")
                .append(row.getTotalRevenue())
                .append("\n");
        }
        return builder.toString();
    }
}
