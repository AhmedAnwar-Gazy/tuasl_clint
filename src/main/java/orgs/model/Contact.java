package orgs.model;

import java.time.LocalDateTime;

public class Contact {
    private int id;
    private int userId;
    private int contactUserId;
    private String aliasName;
    private LocalDateTime createdAt;

    // Constructors
    public Contact() {
    }

    public Contact(int id, int userId, int contactUserId, String aliasName, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.contactUserId = contactUserId;
        this.aliasName = aliasName;
        this.createdAt = createdAt;
    }
    public Contact(int id, int userId, int contactUserId) {
        this.id = id;
        this.userId = userId;
        this.contactUserId = contactUserId;
    }
    public Contact( int userId, int contactUserId) {
        this.userId = userId;
        this.contactUserId = contactUserId;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(int contactUserId) {
        this.contactUserId = contactUserId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
