package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDAO {
    public int countBids(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bids";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public int countBidsForClientProjects(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bids b JOIN projects p ON b.project_id = p.id WHERE p.client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public List<String> findLatestBidActivities(Connection connection, int clientId, int limit) throws SQLException {
        String sql = "SELECT u.name, p.title FROM bids b "
                + "JOIN projects p ON b.project_id = p.id "
                + "JOIN users u ON b.freelancer_id = u.id "
                + "WHERE p.client_id = ? "
                + "ORDER BY b.created_at DESC LIMIT ?";
        List<String> activities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String title = resultSet.getString("title");
                    activities.add("Freelancer " + name + " bid on your project: " + title);
                }
            }
        }
        return activities;
    }

    public int countBidsByFreelancer(Connection connection, int freelancerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bids WHERE freelancer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public int countBidsByFreelancerAndStatus(Connection connection, int freelancerId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bids WHERE freelancer_id = ? AND status = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            statement.setString(2, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public String findLatestAcceptedBidActivity(Connection connection, int freelancerId) throws SQLException {
        String sql = "SELECT p.title FROM bids b JOIN projects p ON b.project_id = p.id "
                + "WHERE b.freelancer_id = ? AND b.status = 'accepted' "
                + "ORDER BY b.created_at DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return "Your bid was accepted: " + resultSet.getString("title");
                }
            }
        }
        return null;
    }
}
