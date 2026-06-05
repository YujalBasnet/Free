package com.freelancehub.freelancehub.dao;

import com.freelancehub.freelancehub.model.Contract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public Contract findContractForFreelancer(Connection connection, int contractId, int freelancerId) throws SQLException {
        String sql = "SELECT c.id, c.project_id, c.client_id, c.freelancer_id, c.start_date, c.end_date, "
                + "c.status, c.created_at, p.title AS project_title, u.name AS client_name "
                + "FROM contracts c "
                + "JOIN projects p ON c.project_id = p.id "
                + "JOIN users u ON c.client_id = u.id "
                + "WHERE c.id = ? AND c.freelancer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, contractId);
            statement.setInt(2, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapContract(resultSet) : null;
            }
        }
    }

    public boolean createContract(Connection connection, int projectId, int clientId, int freelancerId) throws SQLException {
        String sql = "INSERT INTO contracts (project_id, client_id, freelancer_id, start_date, status, created_at) "
                + "VALUES (?, ?, ?, CURDATE(), 'active', NOW())";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, clientId);
            statement.setInt(3, freelancerId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean hasContractForProjectAndFreelancer(Connection connection, int projectId, int freelancerId) throws SQLException {
        String sql = "SELECT 1 FROM contracts WHERE project_id = ? AND freelancer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean markContractCompleted(Connection connection, int contractId, int freelancerId) throws SQLException {
        String sql = "UPDATE contracts SET status = 'completed', end_date = CURDATE() "
                + "WHERE id = ? AND freelancer_id = ? AND status = 'active'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, contractId);
            statement.setInt(2, freelancerId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Contract> listContractsForFreelancer(Connection connection, int freelancerId) throws SQLException {
        String sql = "SELECT c.id, c.project_id, c.client_id, c.freelancer_id, c.start_date, c.end_date, "
                + "c.status, c.created_at, p.title AS project_title, u.name AS client_name "
                + "FROM contracts c "
                + "JOIN projects p ON c.project_id = p.id "
                + "JOIN users u ON c.client_id = u.id "
                + "WHERE c.freelancer_id = ? "
                + "ORDER BY c.created_at DESC";
        List<Contract> contracts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contracts.add(mapContract(resultSet));
                }
            }
        }
        return contracts;
    }

    public List<Contract> listContractsForClient(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT c.id, c.project_id, c.client_id, c.freelancer_id, c.start_date, c.end_date, "
                + "c.status, c.created_at, p.title AS project_title, u.name AS freelancer_name "
                + "FROM contracts c "
                + "JOIN projects p ON c.project_id = p.id "
                + "JOIN users u ON c.freelancer_id = u.id "
                + "WHERE c.client_id = ? "
                + "ORDER BY c.created_at DESC";
        List<Contract> contracts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contracts.add(mapContract(resultSet));
                }
            }
        }
        return contracts;
    }

    private Contract mapContract(ResultSet resultSet) throws SQLException {
        Contract contract = new Contract();
        contract.setId(resultSet.getInt("id"));
        contract.setProjectId(resultSet.getInt("project_id"));
        contract.setClientId(resultSet.getInt("client_id"));
        contract.setFreelancerId(resultSet.getInt("freelancer_id"));
        java.sql.Date startDate = resultSet.getDate("start_date");
        if (startDate != null) {
            contract.setStartDate(startDate.toLocalDate());
        }
        java.sql.Date endDate = resultSet.getDate("end_date");
        if (endDate != null) {
            contract.setEndDate(endDate.toLocalDate());
        }
        contract.setStatus(resultSet.getString("status"));
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            contract.setCreatedAt(createdAt.toLocalDateTime());
        }
        String projectTitle = resultSet.getString("project_title");
        if (projectTitle != null) {
            contract.setProjectTitle(projectTitle);
        }
        String clientName = resultSet.getString("client_name");
        if (clientName != null) {
            contract.setClientName(clientName);
        }
        String freelancerName = resultSet.getString("freelancer_name");
        if (freelancerName != null) {
            contract.setFreelancerName(freelancerName);
        }
        return contract;
    }
}
