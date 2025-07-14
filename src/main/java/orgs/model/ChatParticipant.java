package orgs.model;

import java.time.LocalDateTime;

public class ChatParticipant {
    private int id;
    private int chatId;
    private int userId;
    private String role; // ENUM: 'member', 'admin', 'creator', 'subscriber'
    private int unreadCount;
    private Integer lastReadMessageId; // Use Integer for nullable
    private LocalDateTime joinedAt;

    // Constructors
    public ChatParticipant() {
    }

    public ChatParticipant(int id, int chatId, int userId, String role, int unreadCount, Integer lastReadMessageId, LocalDateTime joinedAt) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.role = role;
        this.unreadCount = unreadCount;
        this.lastReadMessageId = lastReadMessageId;
        this.joinedAt = joinedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Integer getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(Integer lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
