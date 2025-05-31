package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class BlockedUser {
    private Long blockId;
    private Long blockerUserId;
    private Long blockedUserId;
    private Timestamp blockedAt;

    public BlockedUser(Long blockId) {
        this.blockId = blockId;
    }

    public BlockedUser(Long blockId, Long blockerUserId, Long blockedUserId, Timestamp blockedAt) {
        this.blockId = blockId;
        this.blockerUserId = blockerUserId;
        this.blockedUserId = blockedUserId;
        this.blockedAt = blockedAt;
    }

    public Long getBlockId() {
        return blockId;
    }

    public Long getBlockerUserId() {
        return blockerUserId;
    }

    public Long getBlockedUserId() {
        return blockedUserId;
    }

    public Timestamp getBlockedAt() {
        return blockedAt;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public void setBlockerUserId(Long blockerUserId) {
        this.blockerUserId = blockerUserId;
    }

    public void setBlockedUserId(Long blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public void setBlockedAt(Timestamp blockedAt) {
        this.blockedAt = blockedAt;
    }

    public boolean save() throws SQLException {
        String sql = "INSERT INTO blocked_users (blocker_user_id, blocked_user_id, blocked_at) VALUES (?, ?, ?)";
        boolean isInserted;
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, this.blockerUserId);
            statement.setLong(2, this.blockedUserId);
            statement.setTimestamp(3, this.blockedAt != null ? this.blockedAt : new Timestamp(new Date().getTime()));

            isInserted = statement.executeUpdate() > 0;

            if (isInserted) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.blockId = generatedKeys.getLong(1);
                    }
                }
            }
        }

        return isInserted;
    }
    public boolean update() throws SQLException {
        String sql = "UPDATE blocked_users SET blocker_user_id = ?, blocked_user_id = ?, blocked_at = ? WHERE block_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {

            statement.setLong(1, this.blockerUserId);
            statement.setLong(2, this.blockedUserId);
            statement.setTimestamp(3, this.blockedAt);
            statement.setLong(4, this.blockId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String sql = "DELETE FROM blocked_users WHERE block_id = ?";
        try (PreparedStatement statement = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement(sql)) {
            statement.setLong(1, this.blockId);
            return statement.executeUpdate() > 0;
        }
    }
}
