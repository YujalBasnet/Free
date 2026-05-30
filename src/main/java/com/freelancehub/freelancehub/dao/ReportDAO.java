package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {
    public int countPendingReports(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reports WHERE status = 'pending'";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public Integer findLatestReportId(Connection connection) throws SQLException {
        String sql = "SELECT id FROM reports ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt("id") : null;
        }
    }
}
