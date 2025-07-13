// src/orgs/protocol/Command.java
package orgs.tuasl_clint.protocol;

public enum Command {
    // Authentication & User Management
    REGISTER,
    LOGIN,
    LOGOUT,
    GET_USER_PROFILE,
    UPDATE_USER_PROFILE,
    DELETE_USER,
    GET_ALL_USERS,

    // Chat Management
    CREATE_CHAT,
    GET_USER_CHATS,
    GET_CHAT_DETAILS,
    UPDATE_CHAT,
    DELETE_CHAT,

    // Message Management
    SEND_TEXT_MESSAGE, // Specific for plain text
    SEND_IMAGE,        // For images (will involve file transfer)
    SEND_VIDEO,        // For videos (will involve file transfer)
    SEND_VOICE_NOTE,   // For voice notes (will involve file transfer)
    SEND_FILE,         // For general files (will involve file transfer)
    GET_CHAT_MESSAGES,
    UPDATE_MESSAGE,
    DELETE_MESSAGE,
    MARK_MESSAGE_AS_READ,

    // Chat Participant Management
    ADD_CHAT_PARTICIPANT,
    GET_CHAT_PARTICIPANTS,
    UPDATE_CHAT_PARTICIPANT_ROLE, // Corrected from general UPDATE_CHAT_PARTICIPANT
    REMOVE_CHAT_PARTICIPANT,

    // Contact Management
    ADD_CONTACT,
    GET_CONTACTS,
    REMOVE_CONTACT,
    BLOCK_UNBLOCK_USER, // Client uses a single command for both block/unblock

    // Notification Management
    MY_NOTIFICATIONS, // Client uses MY_NOTIFICATIONS, server uses GET_USER_NOTIFICATIONS
    MARK_NOTIFICATION_AS_READ,
    DELETE_NOTIFICATION,

    // Other
    UNKNOWN_COMMAND
}