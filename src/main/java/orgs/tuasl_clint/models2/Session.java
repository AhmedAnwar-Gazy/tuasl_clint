package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;

public class Session {
    private byte[] sessionId;
    private Long userId;
    private String deviceInfo;
    private String ipAddress;
    private Timestamp lastActiveAt;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public Session(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    public Session(byte[] sessionId, Long userId, String deviceInfo, String ipAddress, Timestamp lastActiveAt, Timestamp createdAt, Timestamp expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public byte[] getSessionId() { return sessionId; }
    public Long getUserId() { return userId; }
    public String getDeviceInfo() { return deviceInfo; }
    public String getIpAddress() { return ipAddress; }
    public Timestamp getLastActiveAt() { return lastActiveAt; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getExpiresAt() { return expiresAt; }
    public void setSessionId(byte[] sessionId) { this.sessionId = sessionId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setLastActiveAt(Timestamp lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO sessions (session_id, user_id, device_info, ip_address, last_active_at, created_at, expires_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBytes(1, sessionId);
            statement.setLong(2, userId);
            statement.setString(3, deviceInfo);
            statement.setString(4, ipAddress);
            statement.setTimestamp(5, lastActiveAt);
            statement.setTimestamp(6, createdAt);
            statement.setTimestamp(7, expiresAt);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE sessions SET user_id = ?, device_info = ?, ip_address = ?, last_active_at = ?, expires_at = ? WHERE session_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, deviceInfo);
            statement.setString(3, ipAddress);
            statement.setTimestamp(4, lastActiveAt);
            statement.setTimestamp(5, expiresAt);
            statement.setBytes(6, sessionId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM sessions WHERE session_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBytes(1, sessionId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean refreshLastActive() throws SQLException {
        String sql = "UPDATE sessions SET last_active_at = CURRENT_TIMESTAMP WHERE session_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBytes(1, sessionId);
            return statement.executeUpdate() > 0;
        }
    }
}