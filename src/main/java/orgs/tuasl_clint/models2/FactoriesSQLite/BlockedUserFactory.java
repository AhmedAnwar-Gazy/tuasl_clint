package orgs.tuasl_clint.models2.FactoriesSQLite;

import orgs.tuasl_clint.models2.BlockedUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BlockedUserFactory {

    public BlockedUser create() {
        return new BlockedUser(null);
    }

    public BlockedUser createFromResultSet(ResultSet rs) throws SQLException {
        Long blockId = rs.getLong("block_id");
        Long blockerUserId = rs.getLong("blocker_user_id");
        Long blockedUserId = rs.getLong("blocked_user_id");
        Timestamp blockedAt = rs.getTimestamp("blocked_at");

        return new BlockedUser(blockId, blockerUserId, blockedUserId, blockedAt);
    }
}