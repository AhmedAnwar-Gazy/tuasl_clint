package orgs.tuasl_clint.models2;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int recipientUserId;
    private String message;
    private String eventType;
    private Integer relatedChatId; // Use Integer for nullable
    private boolean isRead;
    private LocalDateTime timestamp;

    // Constructors
    public Notification() {
    }

    public Notification(int id, int recipientUserId, String message, String eventType, Integer relatedChatId, boolean isRead, LocalDateTime timestamp) {
        this.id = id;
        this.recipientUserId = recipientUserId;
        this.message = message;
        this.eventType = eventType;
        this.relatedChatId = relatedChatId;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(int recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getRelatedChatId() {
        return relatedChatId;
    }

    public void setRelatedChatId(Integer relatedChatId) {
        this.relatedChatId = relatedChatId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
