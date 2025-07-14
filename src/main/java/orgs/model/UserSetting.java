package orgs.model;

public class UserSetting {
    private int userId;
    private String privacyPhoneNumber; // ENUM: 'everyone', 'my_contacts', 'nobody'
    private String privacyLastSeen;    // ENUM: 'everyone', 'my_contacts', 'nobody'
    private String privacyProfilePhoto; // ENUM: 'everyone', 'my_contacts', 'nobody'
    private String privacyGroupsAndChannels; // ENUM: 'everyone', 'my_contacts', 'nobody'
    private boolean notificationsPrivateChats;
    private boolean notificationsGroupChats;
    private boolean notificationsChannels;

    // Constructors
    public UserSetting() {
    }

    public UserSetting(int userId, String privacyPhoneNumber, String privacyLastSeen, String privacyProfilePhoto, String privacyGroupsAndChannels, boolean notificationsPrivateChats, boolean notificationsGroupChats, boolean notificationsChannels) {
        this.userId = userId;
        this.privacyPhoneNumber = privacyPhoneNumber;
        this.privacyLastSeen = privacyLastSeen;
        this.privacyProfilePhoto = privacyProfilePhoto;
        this.privacyGroupsAndChannels = privacyGroupsAndChannels;
        this.notificationsPrivateChats = notificationsPrivateChats;
        this.notificationsGroupChats = notificationsGroupChats;
        this.notificationsChannels = notificationsChannels;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPrivacyPhoneNumber() {
        return privacyPhoneNumber;
    }

    public void setPrivacyPhoneNumber(String privacyPhoneNumber) {
        this.privacyPhoneNumber = privacyPhoneNumber;
    }

    public String getPrivacyLastSeen() {
        return privacyLastSeen;
    }

    public void setPrivacyLastSeen(String privacyLastSeen) {
        this.privacyLastSeen = privacyLastSeen;
    }

    public String getPrivacyProfilePhoto() {
        return privacyProfilePhoto;
    }

    public void setPrivacyProfilePhoto(String privacyProfilePhoto) {
        this.privacyProfilePhoto = privacyProfilePhoto;
    }

    public String getPrivacyGroupsAndChannels() {
        return privacyGroupsAndChannels;
    }

    public void setPrivacyGroupsAndChannels(String privacyGroupsAndChannels) {
        this.privacyGroupsAndChannels = privacyGroupsAndChannels;
    }

    public boolean isNotificationsPrivateChats() {
        return notificationsPrivateChats;
    }

    public void setNotificationsPrivateChats(boolean notificationsPrivateChats) {
        this.notificationsPrivateChats = notificationsPrivateChats;
    }

    public boolean isNotificationsGroupChats() {
        return notificationsGroupChats;
    }

    public void setNotificationsGroupChats(boolean notificationsGroupChats) {
        this.notificationsGroupChats = notificationsGroupChats;
    }

    public boolean isNotificationsChannels() {
        return notificationsChannels;
    }

    public void setNotificationsChannels(boolean notificationsChannels) {
        this.notificationsChannels = notificationsChannels;
    }
}
