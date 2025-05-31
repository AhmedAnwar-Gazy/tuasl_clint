package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.math.BigInteger;

public class UserFactory {
    public User create() {
        return new User();
    }

    public User createFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                BigInteger.valueOf(rs.getLong("user_id")),
                rs.getString("phone_number"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("bio"),
                rs.getString("profile_picture_url"),
                rs.getString("hashed_password"),
                rs.getString("two_factor_secret"),
                rs.getTimestamp("last_seen_at"),
                rs.getInt("is_online") == 1,
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    public User findByPhoneNumber(String phoneNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE phone_number = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, phoneNumber);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public User findById(BigInteger userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId.longValue());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }
}