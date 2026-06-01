package com.coffeeshop.report;

import java.sql.SQLException;

public interface ReportGenerator {
    String generate() throws SQLException;
}
