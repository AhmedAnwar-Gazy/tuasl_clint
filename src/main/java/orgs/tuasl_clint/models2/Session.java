package orgs.tuasl_clint.models2;

import java.sql.Timestamp;

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

    public byte[] getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Timestamp getLastActiveAt() {
        return lastActiveAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setSessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setLastActiveAt(Timestamp lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
