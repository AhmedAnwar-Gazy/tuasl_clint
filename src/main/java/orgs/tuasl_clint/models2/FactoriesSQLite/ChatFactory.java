package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.Chat;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ChatFactory {
    public Chat create() {
        return new Chat();
    }

    public static Chat createFromResultSet(ResultSet rs) throws SQLException {
        return new Chat(
                (rs.getLong("chat_id")),
                Chat.ChatType.fromString(rs.getString("chat_type")),
                rs.getString("chat_name"),
                rs.getString("chat_description"),
                rs.getString("chat_picture_url"),
                (rs.getLong("creator_user_id")),
                rs.getString("public_link"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
    public static Chat findByName(String chatName)throws SQLException{
        String sql = "SELECT * FROM chats WHERE chat_name = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, chatName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }
    public static Chat findById(long chatId) throws SQLException {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, chatId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static Chat findByPublicLink(String publicLink) throws SQLException {
        String sql = "SELECT * FROM chats WHERE public_link = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, publicLink);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static List<Chat> findByCreator(long creatorUserId) throws SQLException {
        String sql = "SELECT * FROM chats WHERE creator_user_id = ?";
        List<Chat> chats = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, creatorUserId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    chats.add(createFromResultSet(rs));
                }
            }
        }
        return chats;
    }

    public static List<Chat> findByType(Chat.ChatType chatType) throws SQLException {
        String sql = "SELECT * FROM chats WHERE chat_type = ?";
        List<Chat> chats = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, chatType.name().toLowerCase());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    chats.add(createFromResultSet(rs));
                }
            }
        }
        return chats;
    }
}