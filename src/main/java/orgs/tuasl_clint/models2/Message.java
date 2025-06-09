package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;
import java.util.Date;

public class Message {
    private Long messageId;
    private Long chatId;
    private Long senderUserId;
    private String senderName;
    private String messageType;
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
        this.sentAt = new Timestamp(new Date().getTime());
        this.editedAt = sentAt;
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

    public Long getMessageId() { return messageId; }
    public Long getChatId() { return chatId; }
    public Long getSenderUserId() { return senderUserId; }
    public String getSenderName() { return senderName; }
    public String getMessageType() { return messageType; }
    public String getContent() { return content; }
    public Long getMediaId() { return mediaId; }
    public Long getRepliedToMessageId() { return repliedToMessageId; }
    public Long getForwardedFromUserId() { return forwardedFromUserId; }
    public Long getForwardedFromChatId() { return forwardedFromChatId; }
    public Timestamp getSentAt() { return sentAt; }
    public Timestamp getEditedAt() { return editedAt; }
    public Boolean getDeleted() { return isDeleted; }
    public Integer getViewCount() { return viewCount; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setChatId(Long chatId) { this.chatId = chatId; }
    public void setSenderUserId(Long senderUserId) { this.senderUserId = senderUserId; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    public void setContent(String content) { this.content = content; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }
    public void setRepliedToMessageId(Long repliedToMessageId) { this.repliedToMessageId = repliedToMessageId; }
    public void setForwardedFromUserId(Long forwardedFromUserId) { this.forwardedFromUserId = forwardedFromUserId; }
    public void setForwardedFromChatId(Long forwardedFromChatId) { this.forwardedFromChatId = forwardedFromChatId; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }
    public void setEditedAt(Timestamp editedAt) { this.editedAt = editedAt; }
    public void setDeleted(Boolean deleted) { isDeleted = deleted; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO messages (chat_id, sender_user_id, message_type, content, media_id, replied_to_message_id, forwarded_from_user_id, forwarded_from_chat_id, sent_at, edited_at, is_deleted, view_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, chatId);
            statement.setLong(2, senderUserId);
            statement.setString(3, messageType);
            statement.setString(4, content);
            statement.setObject(5, mediaId);
            statement.setObject(6, repliedToMessageId);
            statement.setObject(7, forwardedFromUserId);
            statement.setObject(8, forwardedFromChatId);
            statement.setTimestamp(9, sentAt);
            statement.setTimestamp(10, editedAt);
            statement.setBoolean(11, isDeleted != null ? isDeleted : false);
            statement.setInt(12, viewCount != null ? viewCount : 0);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.messageId = generatedKeys.getLong(1);
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE messages SET content = ?, edited_at = ?, is_deleted = ?, view_count = ? WHERE message_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, content);
            statement.setTimestamp(2, editedAt);
            statement.setBoolean(3, isDeleted != null ? isDeleted : false);
            statement.setInt(4, viewCount != null ? viewCount : 0);
            statement.setLong(5, messageId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM messages WHERE message_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, messageId);
            return statement.executeUpdate() > 0;
        }
    }
}