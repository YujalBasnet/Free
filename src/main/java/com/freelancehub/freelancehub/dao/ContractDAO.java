package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractDAO {
    public int countContracts(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contracts";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public Integer findLatestContractId(Connection connection) throws SQLException {
        String sql = "SELECT id FROM contracts ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt("id") : null;
        }
    }

    public int countContractsByClientAndStatus(Connection connection, int clientId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contracts WHERE client_id = ? AND status = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setString(2, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public int countContractsByFreelancerAndStatus(Connection connection, int freelancerId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contracts WHERE freelancer_id = ? AND status = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            statement.setString(2, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public String findLatestCompletedContractTitle(Connection connection, int freelancerId) throws SQLException {
        String sql = "SELECT p.title FROM contracts c JOIN projects p ON c.project_id = p.id "
                + "WHERE c.freelancer_id = ? AND c.status = 'completed' "
                + "ORDER BY c.created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("title") : null;
            }
        }
    }
}
