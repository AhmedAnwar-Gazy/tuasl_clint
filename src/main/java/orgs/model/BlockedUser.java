package orgs.model;

import java.time.LocalDateTime;

public class BlockedUser {
    private int blockerId;
    private int blockedId;
    private LocalDateTime blockedAt;

    // Constructors
    public BlockedUser() {
    }

    public BlockedUser(int blockerId, int blockedId, LocalDateTime blockedAt) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = blockedAt;
    }

    // Getters and Setters
    public int getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(int blockerId) {
        this.blockerId = blockerId;
    }

    public int getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(int blockedId) {
        this.blockedId = blockedId;
    }

    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }
}
