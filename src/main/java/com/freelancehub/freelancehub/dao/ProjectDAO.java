package com.freelancehub.freelancehub.dao;

import com.freelancehub.freelancehub.model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public int createProject(Connection connection, Project project) throws SQLException {
        String sql = "INSERT INTO projects (client_id, title, description, budget, deadline, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, project.getClientId());
            statement.setString(2, project.getTitle());
            statement.setString(3, project.getDescription());
            statement.setDouble(4, project.getBudget());
            if (project.getDeadline() == null) {
                statement.setNull(5, java.sql.Types.DATE);
            } else {
                statement.setDate(5, java.sql.Date.valueOf(project.getDeadline()));
            }
            statement.setString(6, project.getStatus());
            int updated = statement.executeUpdate();
            if (updated == 0) {
                return -1;
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        }
    }

    public List<Project> listProjectsByClient(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT id, client_id, title, description, budget, deadline, status, created_at " +
                "FROM projects WHERE client_id = ? ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (resultSet.next()) {
                    projects.add(mapProject(resultSet));
                }
                return projects;
            }
        }
    }

    public List<Project> listOpenProjects(Connection connection) throws SQLException {
        String sql = "SELECT id, client_id, title, description, budget, deadline, status, created_at " +
                "FROM projects WHERE status = 'open' ORDER BY created_at DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Project> projects = new ArrayList<>();
            while (resultSet.next()) {
                projects.add(mapProject(resultSet));
            }
            return projects;
        }
    }

    public List<Project> listOpenProjects(Connection connection, int limit) throws SQLException {
        String sql = "SELECT id, client_id, title, description, budget, deadline, status, created_at " +
                "FROM projects WHERE status = 'open' ORDER BY created_at DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (resultSet.next()) {
                    projects.add(mapProject(resultSet));
                }
                return projects;
            }
        }
    }

    public boolean updateProjectStatus(Connection connection, int projectId, String status) throws SQLException {
        String sql = "UPDATE projects SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, projectId);
            return statement.executeUpdate() > 0;
        }
    }

    private Project mapProject(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("id"));
        project.setClientId(resultSet.getInt("client_id"));
        project.setTitle(resultSet.getString("title"));
        project.setDescription(resultSet.getString("description"));
        project.setBudget(resultSet.getDouble("budget"));
        java.sql.Date deadline = resultSet.getDate("deadline");
        if (deadline != null) {
            project.setDeadline(deadline.toLocalDate());
        }
        project.setStatus(resultSet.getString("status"));
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            project.setCreatedAt(createdAt.toLocalDateTime());
        }
        return project;
    }
}
