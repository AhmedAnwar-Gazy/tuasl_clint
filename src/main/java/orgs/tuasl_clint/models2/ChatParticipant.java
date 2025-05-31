package orgs.clint_pages.models2;


import java.sql.Timestamp;
import java.math.BigInteger; // For BIGINT


public class ChatParticipant {

    private BigInteger chatParticipantId;
    private BigInteger chatId;
    private BigInteger userId;
    private ChatParticipantRole role; // Enum for role
    private Timestamp joinedAt;
    private Timestamp mutedUntil;
    private boolean isPinned;
    private int unreadCount;
    private BigInteger lastReadMessageId;

    public enum ChatParticipantRole {
        MEMBER, ADMIN, CREATOR, MODERATOR;

        public static ChatParticipantRole fromString(String value) {
            for (ChatParticipantRole role : ChatParticipantRole.values()) {
                if (role.name().equalsIgnoreCase(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid ChatParticipantRole: " + value);
        }
    }

    // Constructors
    public ChatParticipant() {}

    public ChatParticipant(BigInteger chatId, BigInteger userId, Timestamp joinedAt) {
        this.chatId = chatId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    public ChatParticipant(BigInteger chatParticipantId, BigInteger chatId, BigInteger userId, ChatParticipantRole role, Timestamp joinedAt, Timestamp mutedUntil, boolean isPinned, int unreadCount, BigInteger lastReadMessageId) {
        this.chatParticipantId = chatParticipantId;
        this.chatId = chatId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
        this.mutedUntil = mutedUntil;
        this.isPinned = isPinned;
        this.unreadCount = unreadCount;
        this.lastReadMessageId = lastReadMessageId;
    }

    // Getters and Setters
    public BigInteger getChatParticipantId() { return chatParticipantId; }
    public void setChatParticipantId(BigInteger chatParticipantId) { this.chatParticipantId = chatParticipantId; }

    public BigInteger getChatId() { return chatId; }
    public void setChatId(BigInteger chatId) { this.chatId = chatId; }

    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

    public ChatParticipantRole getRole() { return role; }
    public void setRole(ChatParticipantRole role) { this.role = role; }

    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }

    public Timestamp getMutedUntil() { return mutedUntil; }
    public void setMutedUntil(Timestamp mutedUntil) { this.mutedUntil = mutedUntil; }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }

    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }

    public BigInteger getLastReadMessageId() { return lastReadMessageId; }
    public void setLastReadMessageId(BigInteger lastReadMessageId) { this.lastReadMessageId = lastReadMessageId; }

    @Override
    public String toString() {
        return "ChatParticipant{" +
                "chatParticipantId=" + chatParticipantId +
                ", chatId=" + chatId +
                ", userId=" + userId +
                ", role=" + role +
                ", joinedAt=" + joinedAt +
                ", mutedUntil=" + mutedUntil +
                ", isPinned=" + isPinned +
                ", unreadCount=" + unreadCount +
                ", lastReadMessageId=" + lastReadMessageId +
                '}';
    }
}
