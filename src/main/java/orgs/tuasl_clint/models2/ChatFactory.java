package orgs.clint_pages.models2;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChatFactory {

    public static Chat createChatFromResultSet(ResultSet rs) throws SQLException {
        BigInteger chatId = BigInteger.valueOf(rs.getInt("id"));
        Chat.ChatType chatType = Chat.ChatType.valueOf(rs.getString("chat_type").toUpperCase()); // Convert string to enum
        String chatName = rs.getString("chat_name");
        String chatDescription = rs.getString("chat_description");
        String chatPictureUrl = rs.getString("chat_picture_url");
        BigInteger creatorUserId = BigInteger.valueOf(rs.getInt("creator_id"));
        String publicLink = rs.getString("public_link");
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new Chat(
                chatId,
                chatType,
                chatName,
                chatDescription,
                chatPictureUrl,
                creatorUserId,
                publicLink,
                createdAt,
                updatedAt
        );
    }

}
