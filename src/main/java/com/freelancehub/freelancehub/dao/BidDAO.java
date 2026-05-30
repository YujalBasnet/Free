package com.freelancehub.freelancehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BidDAO {
    public int countBids(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bids";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }
}
