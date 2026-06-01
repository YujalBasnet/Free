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

    public boolean hasBidForProject(Connection connection, int projectId, int freelancerId) throws SQLException {
        String sql = "SELECT 1 FROM bids WHERE project_id = ? AND freelancer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public int createBid(Connection connection, int projectId, int freelancerId, String proposal, double bidAmount) throws SQLException {
        String sql = "INSERT INTO bids (project_id, freelancer_id, proposal, bid_amount, status) VALUES (?, ?, ?, ?, 'pending')";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, projectId);
            statement.setInt(2, freelancerId);
            statement.setString(3, proposal);
            statement.setDouble(4, bidAmount);
            int updated = statement.executeUpdate();
            if (updated == 0) {
                return -1;
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public java.util.List<com.freelancehub.freelancehub.model.Bid> listBidsForClient(Connection connection, int clientId) throws SQLException {
        String sql = "SELECT b.id, b.project_id, b.freelancer_id, b.proposal, b.bid_amount, b.status, b.created_at, "
                + "p.title AS project_title, u.name AS freelancer_name "
                + "FROM bids b "
                + "JOIN projects p ON b.project_id = p.id "
                + "JOIN users u ON b.freelancer_id = u.id "
                + "WHERE p.client_id = ? "
                + "ORDER BY b.created_at DESC";
        java.util.List<com.freelancehub.freelancehub.model.Bid> bids = new java.util.ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    com.freelancehub.freelancehub.model.Bid bid = new com.freelancehub.freelancehub.model.Bid();
                    bid.setId(resultSet.getInt("id"));
                    bid.setProjectId(resultSet.getInt("project_id"));
                    bid.setFreelancerId(resultSet.getInt("freelancer_id"));
                    bid.setProposal(resultSet.getString("proposal"));
                    bid.setBidAmount(resultSet.getDouble("bid_amount"));
                    bid.setStatus(resultSet.getString("status"));
                    java.sql.Timestamp createdAt = resultSet.getTimestamp("created_at");
                    if (createdAt != null) {
                        bid.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    bid.setProjectTitle(resultSet.getString("project_title"));
                    bid.setFreelancerName(resultSet.getString("freelancer_name"));
                    bids.add(bid);
                }
            }
        }
        return bids;
    }

    public boolean updateBidStatusForClient(Connection connection, int bidId, int clientId, String status) throws SQLException {
        String sql = "UPDATE bids b JOIN projects p ON b.project_id = p.id "
                + "SET b.status = ? "
                + "WHERE b.id = ? AND p.client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, bidId);
            statement.setInt(3, clientId);
            return statement.executeUpdate() > 0;
        }
    }
}
