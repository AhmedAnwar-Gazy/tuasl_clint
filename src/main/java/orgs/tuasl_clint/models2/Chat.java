package orgs.clint_pages.models2;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Chat {
    private BigInteger chatId;
    private ChatType chatType;  // Using an Enum (see below)
    private String chatName;
    private String chatDescription;
    private String chatPictureUrl;
    private BigInteger creatorUserId;
    private String publicLink;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Enum for chat_type
    public enum ChatType {
        PRIVATE, GROUP, CHANNEL;

        public static ChatType fromString(String value) {
            for (ChatType type : ChatType.values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid ChatType: " + value);
        }
    }

    // Constructors
    public Chat() {}

    public Chat(ChatType chatType, Timestamp createdAt) {
        this.chatType = chatType;
        this.createdAt = createdAt;
    }

    public Chat(BigInteger chatId, ChatType chatType, String chatName, String chatDescription, String chatPictureUrl, BigInteger creatorUserId, String publicLink, Timestamp createdAt, Timestamp updatedAt) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.chatName = chatName;
        this.chatDescription = chatDescription;
        this.chatPictureUrl = chatPictureUrl;
        this.creatorUserId = creatorUserId;
        this.publicLink = publicLink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public BigInteger getChatId() { return chatId; }
    public void setChatId(BigInteger chatId) { this.chatId = chatId; }

    public ChatType getChatType() { return chatType; }
    public void setChatType(ChatType chatType) { this.chatType = chatType; }

    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }

    public String getChatDescription() { return chatDescription; }
    public void setChatDescription(String chatDescription) { this.chatDescription = chatDescription; }

    public String getChatPictureUrl() { return chatPictureUrl; }
    public void setChatPictureUrl(String chatPictureUrl) { this.chatPictureUrl = chatPictureUrl; }

    public BigInteger getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(BigInteger creatorUserId) { this.creatorUserId = creatorUserId; }

    public String getPublicLink() { return publicLink; }
    public void setPublicLink(String publicLink) { this.publicLink = publicLink; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", chatType=" + chatType +
                ", chatName='" + chatName + '\'' +
                ", chatDescription='" + chatDescription + '\'' +
                ", chatPictureUrl='" + chatPictureUrl + '\'' +
                ", creatorUserId=" + creatorUserId +
                ", publicLink='" + publicLink + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}