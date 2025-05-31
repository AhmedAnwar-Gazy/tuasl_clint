package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;
import java.sql.*;

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

    public boolean save() throws SQLException {
        String sql = "INSERT INTO contacts (owner_user_id, contact_user_id, alias_name, added_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, ownerUserId);
            statement.setLong(2, contactUserId);
            statement.setString(3, aliasName);
            statement.setTimestamp(4, addedAt);

            boolean isInserted = statement.executeUpdate() > 0;
            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.contactId = generatedKeys.getLong(1);
                    }
                }
            }
            return isInserted;
        }
    }

    public boolean update() throws SQLException {
        String sql = "UPDATE contacts SET alias_name = ? WHERE contact_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, aliasName);
            statement.setLong(2, contactId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM contacts WHERE contact_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, contactId);
            return statement.executeUpdate() > 0;
        }
    }
}