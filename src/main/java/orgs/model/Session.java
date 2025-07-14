package orgs.model;

import java.time.LocalDateTime;

public class Session {
    private int id;
    private int userId;
    private String deviceToken;
    private boolean isActive;
    private LocalDateTime lastActiveAt;
    private LocalDateTime createdAt;

    // Constructors
    public Session() {
    }

    public Session(int id, int userId, String deviceToken, boolean isActive, LocalDateTime lastActiveAt, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.isActive = isActive;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
