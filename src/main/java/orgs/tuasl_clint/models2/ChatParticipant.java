package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;
import java.math.BigInteger;

public class ChatParticipant {
    private BigInteger chatParticipantId;
    private BigInteger chatId;
    private BigInteger userId;
    private ChatParticipantRole role;
    private Timestamp joinedAt;
    private Timestamp mutedUntil;
    private boolean isPinned;
    private int unreadCount;
    private BigInteger lastReadMessageId;

    public enum ChatParticipantRole {
        MEMBER, ADMIN, CREATOR, MODERATOR;

        public static ChatParticipantRole fromString(String value) {
            for (ChatParticipantRole role : ChatParticipantRole.values()) {
                if (role.name().equalsIgnoreCase(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid ChatParticipantRole: " + value);
        }
    }

    public ChatParticipant() {}

    public ChatParticipant(BigInteger chatId, BigInteger userId, Timestamp joinedAt) {
        this.chatId = chatId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    public ChatParticipant(BigInteger chatParticipantId, BigInteger chatId, BigInteger userId, ChatParticipantRole role, Timestamp joinedAt, Timestamp mutedUntil, boolean isPinned, int unreadCount, BigInteger lastReadMessageId) {
        this.chatParticipantId = chatParticipantId;
        this.chatId = chatId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
        this.mutedUntil = mutedUntil;
        this.isPinned = isPinned;
        this.unreadCount = unreadCount;
        this.lastReadMessageId = lastReadMessageId;
    }

    public BigInteger getChatParticipantId() { return chatParticipantId; }
    public void setChatParticipantId(BigInteger chatParticipantId) { this.chatParticipantId = chatParticipantId; }
    public BigInteger getChatId() { return chatId; }
    public void setChatId(BigInteger chatId) { this.chatId = chatId; }
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }
    public ChatParticipantRole getRole() { return role; }
    public void setRole(ChatParticipantRole role) { this.role = role; }
    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }
    public Timestamp getMutedUntil() { return mutedUntil; }
    public void setMutedUntil(Timestamp mutedUntil) { this.mutedUntil = mutedUntil; }
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    public BigInteger getLastReadMessageId() { return lastReadMessageId; }
    public void setLastReadMessageId(BigInteger lastReadMessageId) { this.lastReadMessageId = lastReadMessageId; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO chat_participants (chat_id, user_id, role, joined_at, muted_until, is_pinned, unread_count, last_read_message_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, chatId.longValue());
            statement.setLong(2, userId.longValue());
            statement.setString(3, role != null ? role.name().toLowerCase() : "member");
            statement.setTimestamp(4, joinedAt);
            statement.setTimestamp(5, mutedUntil);
            statement.setInt(6, isPinned ? 1 : 0);
            statement.setInt(7, unreadCount);
            statement.setLong(8, lastReadMessageId != null ? lastReadMessageId.longValue() : 0);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.chatParticipantId = BigInteger.valueOf(generatedKeys.getLong(1));
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE chat_participants SET role = ?, muted_until = ?, is_pinned = ?, unread_count = ?, last_read_message_id = ? WHERE chat_participant_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, role != null ? role.name().toLowerCase() : "member");
            statement.setTimestamp(2, mutedUntil);
            statement.setInt(3, isPinned ? 1 : 0);
            statement.setInt(4, unreadCount);
            statement.setLong(5, lastReadMessageId != null ? lastReadMessageId.longValue() : 0);
            statement.setLong(6, chatParticipantId.longValue());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM chat_participants WHERE chat_participant_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, chatParticipantId.longValue());
            return statement.executeUpdate() > 0;
        }
    }
}