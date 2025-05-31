package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.UserSetting;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserSettingFactory {
    public UserSetting create() {
        return new UserSetting(null);
    }

    public UserSetting createFromResultSet(ResultSet rs) throws SQLException {
        return new UserSetting(
                rs.getLong("user_setting_id"),
                rs.getLong("user_id"),
                rs.getString("privacy_phone_number"),
                rs.getString("privacy_last_seen"),
                rs.getString("privacy_profile_photo"),
                rs.getString("privacy_calls"),
                rs.getString("privacy_groups_and_channels"),
                rs.getString("privacy_forwarded_messages"),
                rs.getBoolean("notifications_private_chats"),
                rs.getBoolean("notifications_group_chats"),
                rs.getBoolean("notifications_channels"),
                rs.getString("notification_sound"),
                rs.getString("chat_theme"),
                rs.getInt("chat_text_size"),
                rs.getTimestamp("updated_at")
        );
    }

    public UserSetting findById(Long userSettingId) throws SQLException {
        String sql = "SELECT * FROM user_settings WHERE user_setting_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userSettingId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public UserSetting findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM user_settings WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<UserSetting> findByPrivacySetting(String settingName, String value) throws SQLException {
        String columnName;
        switch(settingName.toLowerCase()) {
            case "phone": columnName = "privacy_phone_number"; break;
            case "lastseen": columnName = "privacy_last_seen"; break;
            case "profile": columnName = "privacy_profile_photo"; break;
            case "calls": columnName = "privacy_calls"; break;
            case "groups": columnName = "privacy_groups_and_channels"; break;
            case "forwarded": columnName = "privacy_forwarded_messages"; break;
            default: throw new IllegalArgumentException("Invalid privacy setting name");
        }

        String sql = "SELECT * FROM user_settings WHERE " + columnName + " = ?";
        List<UserSetting> settings = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    settings.add(createFromResultSet(rs));
                }
            }
        }
        return settings;
    }

    public List<UserSetting> findByNotificationPreference(boolean privateChats, boolean groupChats, boolean channels) throws SQLException {
        String sql = "SELECT * FROM user_settings WHERE notifications_private_chats = ? AND notifications_group_chats = ? AND notifications_channels = ?";
        List<UserSetting> settings = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setBoolean(1, privateChats);
            statement.setBoolean(2, groupChats);
            statement.setBoolean(3, channels);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    settings.add(createFromResultSet(rs));
                }
            }
        }
        return settings;
    }
}