package orgs.tuasl_clint.models2;

import java.sql.Timestamp;
import java.math.BigInteger; // For BIGINT


public class User {

    private BigInteger userId;
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

    // Constructors
    public User() {} // Default constructor

    public User(String phoneNumber, String firstName, Timestamp createdAt) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.createdAt = createdAt;
    }

    public User(BigInteger userId, String phoneNumber, String username, String firstName, String lastName, String bio, String profilePictureUrl, String hashedPassword, String twoFactorSecret, Timestamp lastSeenAt, boolean isOnline, Timestamp createdAt, Timestamp updatedAt) {
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

    // Getters and Setters
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bio='" + bio + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", twoFactorSecret='" + twoFactorSecret + '\'' +
                ", lastSeenAt=" + lastSeenAt +
                ", isOnline=" + isOnline +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
