package orgs.tuasl_clint.models2;

import java.sql.Timestamp;

public class Message {

    private Long messageId;
    private Long chatId;
    private Long senderUserId;
    private String messageType; // 'text', 'image', etc.
    private String content;
    private Long mediaId;
    private Long repliedToMessageId;
    private Long forwardedFromUserId;
    private Long forwardedFromChatId;
    private Timestamp sentAt;
    private Timestamp editedAt;
    private Boolean isDeleted;
    private Integer viewCount;

    public Message(Long messageId) {
        this.messageId = messageId;
    }
    public Message(String content) {
        this.content = content;
    }

    public Message(Long messageId, Long chatId, Long senderUserId, String messageType, String content, Long mediaId, Long repliedToMessageId, Long forwardedFromUserId, Long forwardedFromChatId, Timestamp sentAt, Timestamp editedAt, Boolean isDeleted, Integer viewCount) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderUserId = senderUserId;
        this.messageType = messageType;
        this.content = content;
        this.mediaId = mediaId;
        this.repliedToMessageId = repliedToMessageId;
        this.forwardedFromUserId = forwardedFromUserId;
        this.forwardedFromChatId = forwardedFromChatId;
        this.sentAt = sentAt;
        this.editedAt = editedAt;
        this.isDeleted = isDeleted;
        this.viewCount = viewCount;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public Long getRepliedToMessageId() {
        return repliedToMessageId;
    }

    public Long getForwardedFromUserId() {
        return forwardedFromUserId;
    }

    public Long getForwardedFromChatId() {
        return forwardedFromChatId;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public Timestamp getEditedAt() {
        return editedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Integer getViewCount() {
        return viewCount;
    }


    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public void setRepliedToMessageId(Long repliedToMessageId) {
        this.repliedToMessageId = repliedToMessageId;
    }

    public void setForwardedFromUserId(Long forwardedFromUserId) {
        this.forwardedFromUserId = forwardedFromUserId;
    }

    public void setForwardedFromChatId(Long forwardedFromChatId) {
        this.forwardedFromChatId = forwardedFromChatId;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public void setEditedAt(Timestamp editedAt) {
        this.editedAt = editedAt;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", chatId=" + chatId +
                ", senderUserId=" + senderUserId +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                ", mediaId=" + mediaId +
                ", repliedToMessageId=" + repliedToMessageId +
                ", forwardedFromUserId=" + forwardedFromUserId +
                ", forwardedFromChatId=" + forwardedFromChatId +
                ", sentAt=" + sentAt +
                ", editedAt=" + editedAt +
                ", isDeleted=" + isDeleted +
                ", viewCount=" + viewCount +
                '}';
    }




}
