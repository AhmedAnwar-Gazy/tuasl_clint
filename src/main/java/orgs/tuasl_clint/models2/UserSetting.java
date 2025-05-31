package orgs.clint_pages.models2;

import java.sql.Timestamp;

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

    public Long getUserSettingId() {
        return userSettingId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPrivacyPhoneNumber() {
        return privacyPhoneNumber;
    }

    public String getPrivacyLastSeen() {
        return privacyLastSeen;
    }

    public String getPrivacyProfilePhoto() {
        return privacyProfilePhoto;
    }

    public String getPrivacyCalls() {
        return privacyCalls;
    }

    public String getPrivacyGroupsAndChannels() {
        return privacyGroupsAndChannels;
    }

    public String getPrivacyForwardedMessages() {
        return privacyForwardedMessages;
    }

    public Boolean getNotificationsPrivateChats() {
        return notificationsPrivateChats;
    }

    public Boolean getNotificationsGroupChats() {
        return notificationsGroupChats;
    }

    public Boolean getNotificationsChannels() {
        return notificationsChannels;
    }

    public String getNotificationSound() {
        return notificationSound;
    }

    public String getChatTheme() {
        return chatTheme;
    }

    public Integer getChatTextSize() {
        return chatTextSize;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUserSettingId(Long userSettingId) {
        this.userSettingId = userSettingId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPrivacyPhoneNumber(String privacyPhoneNumber) {
        this.privacyPhoneNumber = privacyPhoneNumber;
    }

    public void setPrivacyLastSeen(String privacyLastSeen) {
        this.privacyLastSeen = privacyLastSeen;
    }

    public void setPrivacyProfilePhoto(String privacyProfilePhoto) {
        this.privacyProfilePhoto = privacyProfilePhoto;
    }

    public void setPrivacyCalls(String privacyCalls) {
        this.privacyCalls = privacyCalls;
    }

    public void setPrivacyGroupsAndChannels(String privacyGroupsAndChannels) {
        this.privacyGroupsAndChannels = privacyGroupsAndChannels;
    }

    public void setPrivacyForwardedMessages(String privacyForwardedMessages) {
        this.privacyForwardedMessages = privacyForwardedMessages;
    }

    public void setNotificationsPrivateChats(Boolean notificationsPrivateChats) {
        this.notificationsPrivateChats = notificationsPrivateChats;
    }

    public void setNotificationsGroupChats(Boolean notificationsGroupChats) {
        this.notificationsGroupChats = notificationsGroupChats;
    }

    public void setNotificationsChannels(Boolean notificationsChannels) {
        this.notificationsChannels = notificationsChannels;
    }

    public void setNotificationSound(String notificationSound) {
        this.notificationSound = notificationSound;
    }

    public void setChatTheme(String chatTheme) {
        this.chatTheme = chatTheme;
    }

    public void setChatTextSize(Integer chatTextSize) {
        this.chatTextSize = chatTextSize;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
