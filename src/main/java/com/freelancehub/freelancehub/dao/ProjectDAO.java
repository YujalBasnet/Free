package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDAO {
    public int countProjects(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projects";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public String findLatestProjectTitle(Connection connection) throws SQLException {
        String sql = "SELECT title FROM projects ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getString("title") : null;
        }
    }

    public int countProjectsByClient(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projects WHERE client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public int countProjectsByClientAndStatus(Connection connection, int clientId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projects WHERE client_id = ? AND status = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setString(2, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public String findLatestCompletedProjectTitle(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT title FROM projects WHERE client_id = ? AND status = 'completed' ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("title") : null;
            }
        }
    }

    public String findLatestOpenProjectTitle(Connection connection) throws SQLException {
        String sql = "SELECT title FROM projects WHERE status = 'open' ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getString("title") : null;
        }
    }
}
