package orgs.clint_pages.models2;

import java.sql.Timestamp;

public class Contact {

    private Long contactId;
    private Long ownerUserId;
    private Long contactUserId;
    private String aliasName;
    private Timestamp addedAt;

    public Contact(Long contactId, Long ownerUserId, Long contactUserId, String aliasName, Timestamp addedAt) {
        this.contactId = contactId;
        this.ownerUserId = ownerUserId;
        this.contactUserId = contactUserId;
        this.aliasName = aliasName;
        this.addedAt = addedAt;
    }

    public Contact(Long contactId) {
        this.contactId = contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public void setContactUserId(Long contactUserId) {
        this.contactUserId = contactUserId;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }

    public Long getContactId() {
        return contactId;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public Long getContactUserId() {
        return contactUserId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }
}
