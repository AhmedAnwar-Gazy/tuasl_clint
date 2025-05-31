package orgs.clint_pages.models2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MessageFactory {


    public static Message createMessageFromResultSet(ResultSet rs) throws SQLException {
        // Retrieve values, handling potential nulls for nullable columns

        // Non-nullable fields
        Long messageId = rs.getLong("id"); // Maps to messageId
        Long chatId = rs.getLong("chat_id");
        Long senderUserId = rs.getLong("sender_id");
        String messageType = rs.getString("message_type");
        Timestamp sentAt = rs.getTimestamp("sent_at");
        Boolean isDeleted = rs.getBoolean("is_deleted");
        Integer viewCount = rs.getInt("view_count");


        // Nullable fields - Use getObject and check for null or use wrapper types
        String content = rs.getString("content");
        if (rs.wasNull()) content = null; // Important for TEXT columns that can be NULL

        // For Long wrapper types, getLong() returns 0 for NULL, so it's safer to check wasNull()
        Long mediaId = rs.getObject("media_id", Long.class); // Preferred for nullable Longs
        // Long mediaId = rs.getLong("media_id"); if (rs.wasNull()) mediaId = null; // Alternative

        Long repliedToMessageId = rs.getObject("replied_to_message_id", Long.class);
        Long forwardedFromUserId = rs.getObject("forwarded_from_user_id", Long.class);
        Long forwardedFromChatId = rs.getObject("forwarded_from_chat_id", Long.class);

        Timestamp editedAt = rs.getTimestamp("edited_at");
        if (rs.wasNull()) editedAt = null;


        // Construct and return the Message object
        return new Message(
                messageId,
                chatId,
                senderUserId,
                messageType,
                content,
                mediaId,
                repliedToMessageId,
                forwardedFromUserId,
                forwardedFromChatId,
                sentAt,
                editedAt,
                isDeleted,
                viewCount
        );
    }
}
