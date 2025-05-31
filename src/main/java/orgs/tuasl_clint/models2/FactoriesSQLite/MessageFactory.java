package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.Message;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    public Message create() {
        return new Message(null);
    }

    public Message createFromResultSet(ResultSet rs) throws SQLException {
        Message message = new Message(
                rs.getLong("message_id"),
                rs.getLong("chat_id"),
                rs.getLong("sender_user_id"),
                rs.getString("message_type"),
                rs.getString("content"),
                rs.getObject("media_id") != null ? rs.getLong("media_id") : null,
                rs.getObject("replied_to_message_id") != null ? rs.getLong("replied_to_message_id") : null,
                rs.getObject("forwarded_from_user_id") != null ? rs.getLong("forwarded_from_user_id") : null,
                rs.getObject("forwarded_from_chat_id") != null ? rs.getLong("forwarded_from_chat_id") : null,
                rs.getTimestamp("sent_at"),
                rs.getTimestamp("edited_at"),
                rs.getBoolean("is_deleted"),
                rs.getInt("view_count")
        );

        // Set sender name if column exists in result set (from JOIN)
        try {
            message.setSenderName(rs.getString("sender_name"));
        } catch (SQLException e) {
            // Column not present in result set
        }

        return message;
    }

    public Message findById(Long messageId) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "WHERE m.message_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, messageId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Message> findByChatId(Long chatId) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "WHERE m.chat_id = ? ORDER BY m.sent_at DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, chatId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(createFromResultSet(rs));
                }
            }
        }
        return messages;
    }

    public List<Message> findBySender(Long senderUserId) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "WHERE m.sender_user_id = ? ORDER BY m.sent_at DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, senderUserId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(createFromResultSet(rs));
                }
            }
        }
        return messages;
    }

    public List<Message> findByMessageType(String messageType) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "WHERE m.message_type = ? ORDER BY m.sent_at DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, messageType);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(createFromResultSet(rs));
                }
            }
        }
        return messages;
    }

    public List<Message> findRecent(int limit) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "ORDER BY m.sent_at DESC LIMIT ?";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(createFromResultSet(rs));
                }
            }
        }
        return messages;
    }

    public List<Message> findReplies(Long originalMessageId) throws SQLException {
        String sql = "SELECT m.*, u.first_name AS sender_name FROM messages m " +
                "JOIN users u ON m.sender_user_id = u.user_id " +
                "WHERE m.replied_to_message_id = ? ORDER BY m.sent_at DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, originalMessageId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(createFromResultSet(rs));
                }
            }
        }
        return messages;
    }
}