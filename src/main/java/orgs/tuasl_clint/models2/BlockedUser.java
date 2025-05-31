package orgs.tuasl_clint.models2;

import java.sql.Timestamp;

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
}
