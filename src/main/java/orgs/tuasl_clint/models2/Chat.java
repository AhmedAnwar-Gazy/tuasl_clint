package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;


public class Chat {
    private long chatId;
    private ChatType chatType;
    private String chatName;
    private String chatDescription;
    private String chatPictureUrl;
    private long creatorUserId;
    private String publicLink;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum ChatType {
        PRIVATE, GROUP, CHANNEL, UNKNOWN;

        public static ChatType fromString(String value) {
            for (ChatType type : ChatType.values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            //throw new IllegalArgumentException("Invalid ChatType: " + value);
            return UNKNOWN;
        }
    }

    public Chat() {}

    public Chat(ChatType chatType, Timestamp createdAt) {
        this.chatType = chatType;
        this.createdAt = createdAt;
    }

    public Chat(long chatId, ChatType chatType, String chatName, String chatDescription, String chatPictureUrl, long creatorUserId, String publicLink, Timestamp createdAt, Timestamp updatedAt) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.chatName = chatName;
        this.chatDescription = chatDescription;
        this.chatPictureUrl = chatPictureUrl;
        this.creatorUserId = creatorUserId;
        this.publicLink = publicLink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getChatId() { return chatId; }
    public void setChatId(long chatId) { this.chatId = chatId; }
    public ChatType getChatType() { return chatType; }
    public void setChatType(ChatType chatType) { this.chatType = chatType; }
    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }
    public String getChatDescription() { return chatDescription; }
    public void setChatDescription(String chatDescription) { this.chatDescription = chatDescription; }
    public String getChatPictureUrl() { return chatPictureUrl; }
    public void setChatPictureUrl(String chatPictureUrl) { this.chatPictureUrl = chatPictureUrl; }
    public long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(long creatorUserId) { this.creatorUserId = creatorUserId; }
    public String getPublicLink() { return publicLink; }
    public void setPublicLink(String publicLink) { this.publicLink = publicLink; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO chats (chat_type, chat_name, chat_description, chat_picture_url, creator_user_id, public_link, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, chatType.name().toLowerCase());
            statement.setString(2, chatName);
            statement.setString(3, chatDescription);
            statement.setString(4, chatPictureUrl);
            statement.setLong(5, creatorUserId);
            statement.setString(6, publicLink);
            statement.setTimestamp(7, createdAt);
            statement.setTimestamp(8, updatedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.chatId = (generatedKeys.getLong(1));
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE chats SET chat_type = ?, chat_name = ?, chat_description = ?, chat_picture_url = ?, creator_user_id = ?, public_link = ?, updated_at = ? WHERE chat_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, chatType.name().toLowerCase());
            statement.setString(2, chatName);
            statement.setString(3, chatDescription);
            statement.setString(4, chatPictureUrl);
            statement.setLong(5, creatorUserId);
            statement.setString(6, publicLink);
            statement.setTimestamp(7, updatedAt);
            statement.setLong(8, chatId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, chatId);
            return statement.executeUpdate() > 0;
        }
    }
}