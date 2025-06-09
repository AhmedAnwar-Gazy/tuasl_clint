package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;
import java.util.Date;


public class User {
    private long userId;
    private String phoneNumber;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    private String hashedPassword;
    private String twoFactorSecret;
    private Timestamp lastSeenAt;
    private boolean isOnline;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    public static User user = new User(1,"783429731","Mubarak123","Mubarak","Alqadasi","BIO","","","",new Timestamp(11,2,23,1,22,23,23),true,new Timestamp(11,2,23,1,22,23,23),new Timestamp(11,2,23,1,22,23,23));

    public User() {}

    public User(String phoneNumber, String firstName, Timestamp createdAt) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.createdAt = createdAt;
    }

    public User(long userId, String phoneNumber, String username, String firstName, String lastName, String bio, String profilePictureUrl, String hashedPassword, String twoFactorSecret, Timestamp lastSeenAt, boolean isOnline, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.hashedPassword = hashedPassword;
        this.twoFactorSecret = twoFactorSecret;
        this.lastSeenAt = lastSeenAt;
        this.isOnline = isOnline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String username, String phone, String password) {
        this.username = username;
        this.phoneNumber = phone;
        this.hashedPassword = password;
        this.createdAt = new Timestamp(new Date().getTime());
        this.lastSeenAt = createdAt;
        this.updatedAt = createdAt;
        this.firstName = username;
        this.lastName = username;
        this.bio = username;
    }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
    public String getTwoFactorSecret() { return twoFactorSecret; }
    public void setTwoFactorSecret(String twoFactorSecret) { this.twoFactorSecret = twoFactorSecret; }
    public Timestamp getLastSeenAt() { return lastSeenAt; }
    public void setLastSeenAt(Timestamp lastSeenAt) { this.lastSeenAt = lastSeenAt; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO users (phone_number, username, first_name, last_name, bio, profile_picture_url, hashed_password, two_factor_secret, last_seen_at, is_online, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, phoneNumber);
            statement.setString(2, username);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, bio);
            statement.setString(6, profilePictureUrl);
            statement.setString(7, hashedPassword);
            statement.setString(8, twoFactorSecret);
            statement.setTimestamp(9, lastSeenAt);
            statement.setInt(10, isOnline ? 1 : 0);
            statement.setTimestamp(11, createdAt);
            statement.setTimestamp(12, updatedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.userId = (generatedKeys.getLong(1));
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE users SET phone_number = ?, username = ?, first_name = ?, last_name = ?, bio = ?, profile_picture_url = ?, hashed_password = ?, two_factor_secret = ?, last_seen_at = ?, is_online = ?, updated_at = ? WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, phoneNumber);
            statement.setString(2, username);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, bio);
            statement.setString(6, profilePictureUrl);
            statement.setString(7, hashedPassword);
            statement.setString(8, twoFactorSecret);
            statement.setTimestamp(9, lastSeenAt);
            statement.setInt(10, isOnline ? 1 : 0);
            statement.setTimestamp(11, updatedAt);
            statement.setLong(12, userId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            return statement.executeUpdate() > 0;
        }
    }
}