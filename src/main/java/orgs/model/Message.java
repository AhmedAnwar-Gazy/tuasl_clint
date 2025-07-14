package orgs.model;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private int chatId;
    private int senderId;
    private String content;
    private String messageType; // ENUM: 'text', 'image', 'video', 'voiceNote', 'file', 'system'
    private LocalDateTime sentAt;
    private Integer mediaId; // Use Integer for nullable
    private Integer repliedToMessageId; // Use Integer for nullable
    private Integer forwardedFromUserId; // Use Integer for nullable
    private Integer forwardedFromChatId; // Use Integer for nullable
    private LocalDateTime editedAt; // Nullable
    private boolean isDeleted;
    private int viewCount;
    private Media media; // New field to hold media details

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    // Constructors
    public Message() {
    }

    public Message(int id, int chatId, int senderId, String content, String messageType, LocalDateTime sentAt, Integer mediaId, Integer repliedToMessageId, Integer forwardedFromUserId, Integer forwardedFromChatId, LocalDateTime editedAt, boolean isDeleted, int viewCount) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = messageType;
        this.sentAt = sentAt;
        this.mediaId = mediaId;
        this.repliedToMessageId = repliedToMessageId;
        this.forwardedFromUserId = forwardedFromUserId;
        this.forwardedFromChatId = forwardedFromChatId;
        this.editedAt = editedAt;
        this.isDeleted = isDeleted;
        this.viewCount = viewCount;
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getRepliedToMessageId() {
        return repliedToMessageId;
    }

    public void setRepliedToMessageId(Integer repliedToMessageId) {
        this.repliedToMessageId = repliedToMessageId;
    }

    public Integer getForwardedFromUserId() {
        return forwardedFromUserId;
    }

    public void setForwardedFromUserId(Integer forwardedFromUserId) {
        this.forwardedFromUserId = forwardedFromUserId;
    }

    public Integer getForwardedFromChatId() {
        return forwardedFromChatId;
    }

    public void setForwardedFromChatId(Integer forwardedFromChatId) {
        this.forwardedFromChatId = forwardedFromChatId;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
