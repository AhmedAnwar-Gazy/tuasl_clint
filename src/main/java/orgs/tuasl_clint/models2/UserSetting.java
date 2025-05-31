package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;

public class UserSetting {
    private Long userSettingId;
    private Long userId;
    private String privacyPhoneNumber;
    private String privacyLastSeen;
    private String privacyProfilePhoto;
    private String privacyCalls;
    private String privacyGroupsAndChannels;
    private String privacyForwardedMessages;
    private Boolean notificationsPrivateChats;
    private Boolean notificationsGroupChats;
    private Boolean notificationsChannels;
    private String notificationSound;
    private String chatTheme;
    private Integer chatTextSize;
    private Timestamp updatedAt;

    public UserSetting(Long userSettingId) {
        this.userSettingId = userSettingId;
    }

    public UserSetting(Long userSettingId, Long userId, String privacyPhoneNumber, String privacyLastSeen, String privacyProfilePhoto, String privacyCalls, String privacyGroupsAndChannels, String privacyForwardedMessages, Boolean notificationsPrivateChats, Boolean notificationsGroupChats, Boolean notificationsChannels, String notificationSound, String chatTheme, Integer chatTextSize, Timestamp updatedAt) {
        this.userSettingId = userSettingId;
        this.userId = userId;
        this.privacyPhoneNumber = privacyPhoneNumber;
        this.privacyLastSeen = privacyLastSeen;
        this.privacyProfilePhoto = privacyProfilePhoto;
        this.privacyCalls = privacyCalls;
        this.privacyGroupsAndChannels = privacyGroupsAndChannels;
        this.privacyForwardedMessages = privacyForwardedMessages;
        this.notificationsPrivateChats = notificationsPrivateChats;
        this.notificationsGroupChats = notificationsGroupChats;
        this.notificationsChannels = notificationsChannels;
        this.notificationSound = notificationSound;
        this.chatTheme = chatTheme;
        this.chatTextSize = chatTextSize;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters (keep all existing ones)

    public boolean save() throws SQLException {
        String sql = "INSERT INTO user_settings (user_id, privacy_phone_number, privacy_last_seen, privacy_profile_photo, " +
                "privacy_calls, privacy_groups_and_channels, privacy_forwarded_messages, " +
                "notifications_private_chats, notifications_group_chats, notifications_channels, " +
                "notification_sound, chat_theme, chat_text_size, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, userId);
            statement.setString(2, privacyPhoneNumber);
            statement.setString(3, privacyLastSeen);
            statement.setString(4, privacyProfilePhoto);
            statement.setString(5, privacyCalls);
            statement.setString(6, privacyGroupsAndChannels);
            statement.setString(7, privacyForwardedMessages);
            statement.setBoolean(8, notificationsPrivateChats);
            statement.setBoolean(9, notificationsGroupChats);
            statement.setBoolean(10, notificationsChannels);
            statement.setString(11, notificationSound);
            statement.setString(12, chatTheme);
            statement.setInt(13, chatTextSize);
            statement.setTimestamp(14, updatedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.userSettingId = generatedKeys.getLong(1);
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE user_settings SET " +
                "privacy_phone_number = ?, privacy_last_seen = ?, privacy_profile_photo = ?, " +
                "privacy_calls = ?, privacy_groups_and_channels = ?, privacy_forwarded_messages = ?, " +
                "notifications_private_chats = ?, notifications_group_chats = ?, notifications_channels = ?, " +
                "notification_sound = ?, chat_theme = ?, chat_text_size = ?, updated_at = ? " +
                "WHERE user_setting_id = ?";

        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, privacyPhoneNumber);
            statement.setString(2, privacyLastSeen);
            statement.setString(3, privacyProfilePhoto);
            statement.setString(4, privacyCalls);
            statement.setString(5, privacyGroupsAndChannels);
            statement.setString(6, privacyForwardedMessages);
            statement.setBoolean(7, notificationsPrivateChats);
            statement.setBoolean(8, notificationsGroupChats);
            statement.setBoolean(9, notificationsChannels);
            statement.setString(10, notificationSound);
            statement.setString(11, chatTheme);
            statement.setInt(12, chatTextSize);
            statement.setTimestamp(13, updatedAt);
            statement.setLong(14, userSettingId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM user_settings WHERE user_setting_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userSettingId);
            return statement.executeUpdate() > 0;
        }
    }
}