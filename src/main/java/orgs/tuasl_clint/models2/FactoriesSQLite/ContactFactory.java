package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.Contact;
import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactFactory {
    public Contact create() {
        return new Contact(null);
    }

    public Contact createFromResultSet(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getLong("contact_id"),
                rs.getLong("owner_user_id"),
                rs.getLong("contact_user_id"),
                rs.getString("alias_name"),
                rs.getTimestamp("added_at")
        );
    }

    public Contact findById(Long contactId) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE contact_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, contactId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Contact> findByOwner(Long ownerUserId) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE owner_user_id = ?";
        List<Contact> contacts = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, ownerUserId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    contacts.add(createFromResultSet(rs));
                }
            }
        }
        return contacts;
    }

    public List<Contact> findByContactUser(Long contactUserId) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE contact_user_id = ?";
        List<Contact> contacts = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, contactUserId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    contacts.add(createFromResultSet(rs));
                }
            }
        }
        return contacts;
    }

    public Contact findByUserPair(Long ownerUserId, Long contactUserId) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE owner_user_id = ? AND contact_user_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, ownerUserId);
            statement.setLong(2, contactUserId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        }
        return null;
    }
}