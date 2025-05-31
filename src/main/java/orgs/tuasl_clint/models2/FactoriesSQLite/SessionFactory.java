package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.Session;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionFactory {
    public Session create() {
        return new Session(null);
    }

    public Session createFromResultSet(ResultSet rs) throws SQLException {
        return new Session(
                rs.getBytes("session_id"),
                rs.getLong("user_id"),
                rs.getString("device_info"),
                rs.getString("ip_address"),
                rs.getTimestamp("last_active_at"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("expires_at")
        );
    }

    public Session findById(byte[] sessionId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE session_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBytes(1, sessionId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Session> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user_id = ? ORDER BY last_active_at DESC";
        List<Session> sessions = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    sessions.add(createFromResultSet(rs));
                }
            }
        }
        return sessions;
    }

    public List<Session> findActiveSessions() throws SQLException {
        String sql = "SELECT * FROM sessions WHERE expires_at > CURRENT_TIMESTAMP ORDER BY last_active_at DESC";
        List<Session> sessions = new ArrayList<>();
        try (Statement statement = DatabaseConnectionSQLite.getInstance().getConnection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(createFromResultSet(rs));
            }
        }
        return sessions;
    }

    public List<Session> findExpiredSessions() throws SQLException {
        String sql = "SELECT * FROM sessions WHERE expires_at <= CURRENT_TIMESTAMP";
        List<Session> sessions = new ArrayList<>();
        try (Statement statement = DatabaseConnectionSQLite.getInstance().getConnection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(createFromResultSet(rs));
            }
        }
        return sessions;
    }

    public boolean invalidateSession(byte[] sessionId) throws SQLException {
        String sql = "UPDATE sessions SET expires_at = CURRENT_TIMESTAMP WHERE session_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBytes(1, sessionId);
            return statement.executeUpdate() > 0;
        }
    }

    public int invalidateAllUserSessions(Long userId) throws SQLException {
        String sql = "UPDATE sessions SET expires_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            return statement.executeUpdate();
        }
    }
}