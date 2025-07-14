package orgs.model;

import java.time.LocalDateTime;

public class Chat {
    private int id;
    private String chatType; // ENUM: 'private', 'group', 'channel'
    private String chatName;
    private String chatPictureUrl;
    private String chatDescription;
    private String publicLink;
    private int creatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Chat() {
    }

    public Chat(int id, String chatType, String chatName, String chatPictureUrl, String chatDescription, String publicLink, int creatorId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.chatType = chatType;
        this.chatName = chatName;
        this.chatPictureUrl = chatPictureUrl;
        this.chatDescription = chatDescription;
        this.publicLink = publicLink;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatPictureUrl() {
        return chatPictureUrl;
    }

    public void setChatPictureUrl(String chatPictureUrl) {
        this.chatPictureUrl = chatPictureUrl;
    }

    public String getChatDescription() {
        return chatDescription;
    }

    public void setChatDescription(String chatDescription) {
        this.chatDescription = chatDescription;
    }

    public String getPublicLink() {
        return publicLink;
    }

    public void setPublicLink(String publicLink) {
        this.publicLink = publicLink;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
