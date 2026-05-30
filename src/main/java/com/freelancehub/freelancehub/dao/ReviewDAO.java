package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewDAO {
    public Double findAverageRatingForFreelancer(Connection connection, int freelancerId) throws SQLException {
        String sql = "SELECT AVG(rating) FROM reviews WHERE reviewed_user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, freelancerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double avg = resultSet.getDouble(1);
                    return resultSet.wasNull() ? null : avg;
                }
            }
        }
        return null;
    }
}
