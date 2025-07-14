//package orgs.client;
//
//// src/orgs/client/ChatClient.java
//package orgs.client;
//
//import orgs.model.Message;
//import orgs.model.User;
//import orgs.model.Chat;
//import orgs.model.Media; // Import the new Media class
//import orgs.protocol.Command;
//import orgs.protocol.Request;
//import orgs.protocol.Response;
//import orgs.utils.LocalDateTimeAdapter;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.*;
//import java.lang.reflect.Type;
//import java.net.Socket;
//import java.net.SocketException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//public class ChatClient implements AutoCloseable {
//    private static final String SERVER_IP = "192.168.1.99"; // Localhost
//    private static final int SERVER_PORT = 6373;
//    private static final int FILE_TRANSFER_PORT = 6374;
//
//    private String currentFilePathToSend;
//    private String pendingFileTransferId; // This will now be received from the server
//
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;
//    private Gson gson = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//            .serializeNulls()
//            .create();
//
//    private Scanner scanner;
//    private User currentUser;
//
//    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
//
//    public ChatClient() {
//        this.scanner = new Scanner(System.in);
//        try {
//            socket = new Socket(SERVER_IP, SERVER_PORT);
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            System.out.println("Connected to chat server on main port.");
//
//            new Thread(this::listenForServerMessages, "ServerListener").start();
//
//        } catch (IOException e) {
//            System.err.println("Error connecting to server: " + e.getMessage());
//            System.exit(1);
//        }
//    }
//
//    private void listenForServerMessages() {
//        try {
//            String serverResponseJson;
//            while ((serverResponseJson = in.readLine()) != null) {
//                Response response = gson.fromJson(serverResponseJson, Response.class);
//                System.out.println("[DEBUG - Raw Server Response]: " + serverResponseJson); // Added for debugging
//
//                // Special handling for file transfer initiation
//                if ("READY_TO_RECEIVE_FILE".equals(response.getMessage())) {
//                    System.out.println("Server is ready for file transfer. Initiating file send...");
//                    Type type = new TypeToken<Map<String, String>>() {}.getType();
//                    Map<String, String> data = gson.fromJson(response.getData(), type);
//                    pendingFileTransferId = data.get("transfer_id");
//
//                    if (pendingFileTransferId != null) {
//                        sendFileBytes(currentFilePathToSend, pendingFileTransferId);
//                    } else {
//                        System.err.println("Error: Server responded READY_TO_RECEIVE_FILE but no transfer_id found in data.");
//                    }
//                    continue; // Do not put this into the main response queue
//                }
//
//                // Handle unsolicited new messages (e.g., from other users)
//                if (response.isSuccess() && "New message received".equals(response.getMessage())) {
//                    Message newMessage = gson.fromJson(response.getData(), Message.class);
//                    String senderInfo = (newMessage.getSenderId() == currentUser.getId()) ? "You" : "User " + newMessage.getSenderId();
//                    String contentToDisplay = newMessage.getContent() != null ? newMessage.getContent() : "[No text content]";
//                    String mediaInfo = "";
//                    if (newMessage.getMedia() != null) {
//                        mediaInfo = String.format(" [Media Type: %s, File: %s]",
//                                newMessage.getMedia().getMediaType(), newMessage.getMedia().getFileName());
//                    }
//                    System.out.println(String.format("\n[NEW MESSAGE from %s in Chat ID %d]: %s%s",
//                            senderInfo, newMessage.getChatId(), contentToDisplay, mediaInfo));
//                    System.out.print("> "); // Re-prompt
//                }
//                // Handle general success/failure messages for commands that don't need special parsing
//                else if (response.isSuccess() && (
//                        "Login successful!".equals(response.getMessage()) ||
//                                "Registration successful!".equals(response.getMessage()) ||
//                                "Message sent successfully!".equals(response.getMessage()) ||
//                                "Profile updated successfully!".equals(response.getMessage()) ||
//                                "User account deleted successfully.".equals(response.getMessage()) ||
//                                "Chat created successfully!".equals(response.getMessage()) ||
//                                "Chat deleted successfully!".equals(response.getMessage()) ||
//                                "Participant added successfully!".equals(response.getMessage()) ||
//                                "Participant role updated successfully!".equals(response.getMessage()) ||
//                                "Participant removed successfully!".equals(response.getMessage()) ||
//                                "Contact added successfully!".equals(response.getMessage()) ||
//                                "Contact removed successfully!".equals(response.getMessage()) ||
//                                "User blocked successfully!".equals(response.getMessage()) ||
//                                "User unblocked successfully!".equals(response.getMessage()) ||
//                                "Message updated successfully!".equals(response.getMessage()) ||
//                                "Message deleted successfully!".equals(response.getMessage()) ||
//                                "Message marked as read!".equals(response.getMessage()) ||
//                                "Notification marked as read!".equals(response.getMessage()) ||
//                                "Notification deleted successfully!".equals(response.getMessage()) ||
//                                "Logged out successfully.".equals(response.getMessage())
//                )) {
//                    responseQueue.put(response);
//                }
//                // Handle general failure messages
//                else if (!response.isSuccess() && (
//                        response.getMessage().startsWith("Login failed") ||
//                                response.getMessage().startsWith("Registration failed") ||
//                                response.getMessage().startsWith("Failed to send message") ||
//                                response.getMessage().startsWith("Error sending message") ||
//                                response.getMessage().startsWith("Missing file details") ||
//                                response.getMessage().startsWith("You are not a participant") ||
//                                response.getMessage().startsWith("Failed to get profile") ||
//                                response.getMessage().startsWith("Failed to update profile") ||
//                                response.getMessage().startsWith("Failed to delete user") ||
//                                response.getMessage().startsWith("Failed to create chat") ||
//                                response.getMessage().startsWith("Failed to delete chat") ||
//                                response.getMessage().startsWith("Failed to add participant") ||
//                                response.getMessage().startsWith("Failed to get chat participants") ||
//                                response.getMessage().startsWith("Failed to update participant role") ||
//                                response.getMessage().startsWith("Failed to remove participant") ||
//                                response.getMessage().startsWith("Failed to add contact") ||
//                                response.getMessage().startsWith("Failed to get contacts") ||
//                                response.getMessage().startsWith("Failed to remove contact") ||
//                                response.getMessage().startsWith("Failed to block/unblock user") ||
//                                response.getMessage().startsWith("Failed to get notifications") ||
//                                response.getMessage().startsWith("Failed to mark notification") ||
//                                response.getMessage().startsWith("Failed to delete notification") ||
//                                response.getMessage().startsWith("Failed to update message") ||
//                                response.getMessage().startsWith("Failed to delete message") ||
//                                response.getMessage().startsWith("Failed to mark message as read") ||
//                                response.getMessage().startsWith("Authentication required") ||
//                                response.getMessage().startsWith("Server internal error") ||
//                                response.getMessage().startsWith("Unknown command")
//                )) {
//                    responseQueue.put(response);
//                }
//                // Specific handling for responses that contain lists of objects or single objects
//                else if (response.isSuccess() && (
//                        "Messages retrieved.".equals(response.getMessage()) ||
//                                "All users retrieved.".equals(response.getMessage()) ||
//                                "User profile retrieved.".equals(response.getMessage()) ||
//                                "All chats retrieved for user.".equals(response.getMessage()) ||
//                                "Chat details retrieved.".equals(response.getMessage()) ||
//                                "User contacts retrieved.".equals(response.getMessage()) ||
//                                "User notifications retrieved.".equals(response.getMessage())
//                )) {
//                    responseQueue.put(response);
//                }
//                else {
//                    System.out.println("[DEBUG - Unhandled Response]: " + serverResponseJson);
//                    responseQueue.put(response); // Still put it in case the main loop is waiting
//                }
//            }
//        } catch (SocketException e) {
//            System.out.println("Server connection lost: " + e.getMessage());
//        } catch (IOException e) {
//            System.err.println("Error reading from server: " + e.getMessage());
//        } catch (InterruptedException e) {
//            System.err.println("Listener thread interrupted: " + e.getMessage());
//            Thread.currentThread().interrupt();
//        } finally {
//            closeConnection();
//        }
//    }
//
//
//    public void startClient() {
//        System.out.println("Welcome to the Tuasil Messaging Client!");
//
//        while (currentUser == null) {
//            System.out.println("\n--- Auth Options ---");
//            System.out.println("1. Login");
//            System.out.println("2. Register");
//            System.out.print("Choose an option: ");
//            String authChoice = scanner.nextLine();
//
//            Map<String, Object> authData = new HashMap<>();
//            String phoneNumber, password, firstName, lastName;
//
//            if ("1".equals(authChoice)) {
//                System.out.print("Enter phone number: ");
//                phoneNumber = scanner.nextLine();
//                System.out.print("Enter password: ");
//                password = scanner.nextLine();
//                authData.put("phone_number", phoneNumber);
//                authData.put("password", password);
//
//                Request loginRequest = new Request(Command.LOGIN, authData);
//                Response loginResponse = sendRequestAndAwaitResponse(loginRequest);
//
//                if (loginResponse != null && loginResponse.isSuccess()) {
//                    this.currentUser = gson.fromJson(loginResponse.getData(), User.class);
//                    System.out.println("Logged in as: " + currentUser.getPhoneNumber() + " (" + currentUser.getFirstName() + " " + currentUser.getLastName() + ")");
//                    break;
//                } else if (loginResponse != null) {
//                    System.out.println("Login failed: " + loginResponse.getMessage());
//                }
//            } else if ("2".equals(authChoice)) {
//                System.out.print("Enter phone number: ");
//                phoneNumber = scanner.nextLine();
//                System.out.print("Enter password: ");
//                password = scanner.nextLine();
//                System.out.print("Enter first name: ");
//                firstName = scanner.nextLine();
//                System.out.print("Enter last name: ");
//                lastName = scanner.nextLine();
//
//                // Corrected keys for registration payload
//                authData.put("phone_number", phoneNumber);
//                authData.put("password", password);
//                authData.put("first_name", firstName);
//                authData.put("last_name", lastName);
//
//                Request registerRequest = new Request(Command.REGISTER, authData);
//                Response registerResponse = sendRequestAndAwaitResponse(registerRequest);
//
//                if (registerResponse != null && registerResponse.isSuccess()) {
//                    System.out.println("Registration successful! You can now log in.");
//                } else if (registerResponse != null) {
//                    System.out.println("Registration failed: " + registerResponse.getMessage());
//                }
//            } else {
//                System.out.println("Invalid option.");
//            }
//        }
//
//        while (currentUser != null) {
//            displayCommands();
//            System.out.print("Enter command number: ");
//            String commandInput = scanner.nextLine();
//            handleUserInput(commandInput);
//        }
//    }
//
//    private void displayCommands() {
//        System.out.println("\n--- Commands ---");
//        System.out.println("1. Send Message (Text/Media)"); // Unified command
//        System.out.println("2. Get Chat Messages");
//        System.out.println("3. Create Chat");
//        System.out.println("4. Manage Profile (View/Update/Delete)");
//        System.out.println("5. Get All Users");
//        System.out.println("6. My Chats");
//        System.out.println("7. Manage Chat Participants");
//        System.out.println("8. My Contacts (View/Manage)");
//        System.out.println("9. Block/Unblock User");
//        System.out.println("10. My Notifications");
//        System.out.println("11. Update/Delete Message");
//        System.out.println("12. Delete Chat");
//        System.out.println("13. Logout");
//        System.out.println("14. Get Media File");
//    }
//
//    private void handleUserInput(String commandInput) {
//        Request request = null;
//        Map<String, Object> data = new HashMap<>();
//
//        try {
//            switch (commandInput) {
//                case "1": // Send Message (Unified)
//                    System.out.print("Enter Chat ID: ");
//                    int messageChatId = Integer.parseInt(scanner.nextLine());
//                    System.out.print("Is this a media message? (yes/no): ");
//                    String isMedia = scanner.nextLine().toLowerCase();
//
//                    data.put("chat_id", messageChatId);
//
//                    if ("yes".equals(isMedia)) {
//                        System.out.print("Enter local file path (e.g., C:/images/photo.jpg or /home/user/video.mp4): ");
//                        String filePath = scanner.nextLine();
//
//                        filePath = filePath.trim();
//                        filePath = filePath.replace('\\', '/').replace("\u202A", "").replace("\u202B", "");
//
//                        System.out.println("Processed Path: '" + filePath + "'"); // Print with quotes to see any remaining invisible chars
//                        System.out.println("Processed Path Length: " + filePath.length());
//
//
//                        File file = new File(filePath);
//
//
//                        if (!file.exists()) { // Check for existence first
//                            System.out.println("Error: File does not exist at " + filePath);
//                            System.out.println("Current working directory: " + System.getProperty("user.dir")); // Helpful for relative paths
//                            return;
//                        }
//
//                        if (!file.isFile()) { // Then check if it's a regular file
//                            System.out.println("Error: Path is not a regular file (it might be a directory or special file) at " + filePath);
//                            return;
//                        }
//
//
//                        if (!file.exists() || !file.isFile()) {
//                            System.out.println("Error: File not found or is not a regular file at " + filePath);
//                            return;
//                        }
//                        currentFilePathToSend = filePath;
//                        long fileSize = file.length();
//                        String fileName = file.getName();
//
//                        System.out.print("Enter caption (optional, press Enter to skip): ");
//                        String caption = scanner.nextLine();
//
//                        String mediaType;
//                        // Infer media type from file extension or ask
//                        if (fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
//                            mediaType = "image";
//                        } else if (fileName.matches(".*\\.(mp4|avi|mov|wmv)$")) {
//                            mediaType = "video";
//                        } else if (fileName.matches(".*\\.(mp3|wav|ogg)$")) {
//                            mediaType = "voiceNote";
//                        } else {
//                            System.out.print("Enter media type (image, video, voiceNote, file): ");
//                            mediaType = scanner.nextLine();
//                        }
//
//                        Media media = new Media();
//                        media.setFileName(fileName);
//                        media.setFileSize(fileSize);
//                        media.setMediaType(mediaType);
//                        media.setUploadedByUserId(currentUser.getId());
//                        media.setUploadedAt(LocalDateTime.now());
//                        // mediaId will be set by the server after file transfer
//
//                        data.put("content", caption.isEmpty() ? null : caption); // Caption as content
//                        data.put("media", media); // Embed the Media object directly
//                        request = new Request(Command.SEND_MESSAGE, data);
//
//                    } else { // Text message
//
//                        System.out.print("Enter message content: ");
//                        String textContent = scanner.nextLine();
//                        if (textContent.trim().isEmpty()) {
//                            System.out.println("Text message content cannot be empty.");
//                            return;
//                        }
//                        System.out.println("hello "+ textContent);
//                        data.put("content", textContent);
//                        request = new Request(Command.SEND_MESSAGE, data);
//                    }
//                    break;
//
//                case "2": // Get Chat Messages (was 6)
//                    System.out.print("Enter Chat ID: ");
//                    int getChatId = Integer.parseInt(scanner.nextLine());
//                    System.out.print("Enter limit (number of messages to fetch): ");
//                    int limit = Integer.parseInt(scanner.nextLine());
//                    System.out.print("Enter offset (starting point): ");
//                    int offset = Integer.parseInt(scanner.nextLine());
//                    data.put("chat_id", getChatId);
//                    data.put("limit", limit);
//                    data.put("offset", offset);
//                    request = new Request(Command.GET_CHAT_MESSAGES, data);
//
//                    Response messagesResponse = sendRequestAndAwaitResponse(request);
//                    if (messagesResponse != null && messagesResponse.isSuccess() && "Messages retrieved.".equals(messagesResponse.getMessage())) {
//                        Type messageListType = new TypeToken<List<Message>>() {}.getType();
//                        List<Message> messages = gson.fromJson(messagesResponse.getData(), messageListType);
//                        System.out.println("\n--- Messages in Chat ID: " + getChatId + " ---");
//                        if (messages == null || messages.isEmpty()) {
//                            System.out.println("No messages found in this chat.");
//                        } else {
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                            for (Message msg : messages) {
//                                String senderInfo = (msg.getSenderId() == currentUser.getId()) ? "You" : "User " + msg.getSenderId();
//                                String contentToDisplay = msg.getContent() != null ? msg.getContent() : "[No text content]";
//                                String mediaInfo = "";
//                                if (msg.getMedia() != null) {
//                                    mediaInfo = String.format(" [Media Type: %s, File: %s, ID: %s]",
//                                            msg.getMedia().getMediaType(), msg.getMedia().getFileName(), msg.getMedia().getTransferId());
//                                }
//                                System.out.printf("[%s] %s: %s%s (Views: %d)\n",
//                                        msg.getSentAt().format(formatter), senderInfo, contentToDisplay, mediaInfo, msg.getViewCount());
//                            }
//                        }
//                    } else if (messagesResponse != null) {
//                        System.out.println("Failed to get messages: " + messagesResponse.getMessage());
//                    }
//                    return;
//
//                case "3": // Create Chat (was 7)
//                    System.out.print("Enter chat type (private, group, channel): ");
//                    String chatType = scanner.nextLine();
//                    System.out.print("Enter chat name (optional for private, required for group/channel): ");
//                    String chatName = scanner.nextLine();
//                    System.out.print("Enter chat description (optional): ");
//                    String chatDescription = scanner.nextLine();
//                    System.out.print("Enter public link (optional, for public channels only): ");
//                    String publicLink = scanner.nextLine();
//
//                    data.put("chat_type", chatType);
//                    data.put("chat_name", chatName.isEmpty() ? null : chatName);
//                    data.put("chat_description", chatDescription.isEmpty() ? null : chatDescription);
//                    data.put("public_link", publicLink.isEmpty() ? null : publicLink);
//                    request = new Request(Command.CREATE_CHAT, data);
//                    break;
//
//                case "4": // Manage Profile (was 8)
//                    manageProfile(scanner);
//                    return;
//                case "5": // Get All Users (was 9)
//                    request = new Request(Command.GET_ALL_USERS);
//                    Response allUsersResponse = sendRequestAndAwaitResponse(request);
//                    if (allUsersResponse != null && allUsersResponse.isSuccess() && "All users retrieved.".equals(allUsersResponse.getMessage())) {
//                        Type userListType = new TypeToken<List<User>>() {}.getType();
//                        List<User> users = gson.fromJson(allUsersResponse.getData(), userListType);
//                        System.out.println("\n--- All Registered Users ---");
//                        if (users == null || users.isEmpty()) {
//                            System.out.println("No users found.");
//                        } else {
//                            for (User user : users) {
//                                System.out.printf("ID: %d, Name: %s %s, Phone: %s, Online: %s\n",
//                                        user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.isOnline());
//                            }
//                        }
//                    } else if (allUsersResponse != null) {
//                        System.out.println("Failed to get all users: " + allUsersResponse.getMessage());
//                    }
//                    return;
//
//                case "6": // My Chats (was 10)
//                    getUserChats();
//                    return;
//                case "7": // Manage Chat Participants (was 11)
//                    manageChatParticipants(scanner);
//                    return;
//                case "8": // My Contacts (was 12)
//                    manageContacts(scanner);
//                    return;
//                case "9": // Block/Unblock User (was 13)
//                    System.out.print("Enter User ID to block/unblock: ");
//                    int targetUserId = Integer.parseInt(scanner.nextLine());
//                    System.out.print("Action (block/unblock): ");
//                    String action = scanner.nextLine();
//                    blockUnblockUser(targetUserId, action);
//                    return;
//                case "10": // My Notifications (was 14)
//                    getNotifications();
//                    return;
//                case "11": // Update/Delete Message (was 15)
//                    updateDeleteMessage(scanner);
//                    return;
//                case "12": // Delete Chat (was 16)
//                    System.out.print("Enter Chat ID to delete: ");
//                    int deleteChatId = Integer.parseInt(scanner.nextLine());
//                    deleteChat(deleteChatId);
//                    return;
//                case "13": // Logout (was 17)
//                    request = new Request(Command.LOGOUT);
//                    Response logoutResponse = sendRequestAndAwaitResponse(request);
//                    if (logoutResponse != null && logoutResponse.isSuccess()) {
//                        System.out.println(logoutResponse.getMessage());
//                        currentUser = null;
//                    } else if (logoutResponse != null) {
//                        System.out.println("Logout failed: " + logoutResponse.getMessage());
//                    }
//                    return;
//
//                case "14": // New command to get a media file
//                    //System.out.print("Enter Message ID of the media file to download: ");
//                    //int messageId = Integer.parseInt(scanner.nextLine());
//
//                    // First, get the message details from the server to get the Media object
//                    //Request getMsgRequest = new Request(Command.GET_CHAT_MESSAGES, Map.of("chat_id", 0, "limit", 1, "offset", 0)); // Simplified, need a better way to get a single message
//                    // A better approach would be to have a separate GET_MESSAGE_BY_ID command
//
//                    // For this example, let's assume we already have the Message object from a previous GET_CHAT_MESSAGES call
//                    // and its Media object is available. Let's create a dummy one for demonstration.
//                    System.out.print("Enter Media ID (as shown in messages): ");
//                    String mediaId = scanner.nextLine();
//                    //System.out.print("Enter File Name: ");
//                    //String fileName = scanner.nextLine();
//                    System.out.print("Enter save directory path (e.g., C:/downloads): ");
//                    String saveDir = scanner.nextLine();
//
//                    Media mediaToDownload = new Media();
//                    mediaToDownload.setId(Integer.parseInt(mediaId));
//                    //mediaToDownload.setFileName(fileName);
//
//                    getFileByMedia(mediaToDownload, saveDir);
//                    return;
//
//                default:
//                    System.out.println("Invalid command number.");
//                    return;
//            }
//
//            if (request != null) {
//                Response response = sendRequestAndAwaitResponse(request);
//                if (response != null) {
//                    System.out.println("Server Response: " + response.getMessage());
//                    // Clear file transfer details only after a successful message send (which includes media)
//                    if (response.isSuccess() && "Message sent successfully!".equals(response.getMessage()) && currentFilePathToSend != null) {
//                        System.out.println("File transfer details cleared.");
//                        currentFilePathToSend = null;
//                        pendingFileTransferId = null;
//                    }
//                }
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid number format. Please enter a valid number for IDs/limits.");
//        } catch (Exception e) {
//            System.err.println("Error handling user input: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//
//
//    // -----------------------------------------------------------
//
//
//    // ... (rest of ChatClient methods like login, sendRequestAndAwaitResponse, sendFileBytes, closeConnection)
//    // You will need to add the helper methods like manageProfile, getUserChats, manageChatParticipants,
//    // manageContacts, blockUnblockUser, getNotifications, updateDeleteMessage, deleteChat if they are not already defined.
//
//    // Example of a helper method that needs to be present (from your original code)
//    private void manageProfile(Scanner scanner) {
//        // Implementation for managing profile
//        System.out.println("Manage Profile functionality not yet fully implemented in this example.");
//    }
//
//    private void getUserChats() {
//        Request request = new Request(Command.GET_USER_CHATS);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess() && "All chats retrieved for user.".equals(response.getMessage())) {
//            Type chatListType = new TypeToken<List<Chat>>() {}.getType();
//            List<Chat> chats = gson.fromJson(response.getData(), chatListType);
//            System.out.println("\n--- Your Chats ---");
//            if (chats == null || chats.isEmpty()) {
//                System.out.println("You are not a participant in any chats.");
//            } else {
//                for (Chat chat : chats) {
//                    System.out.printf("ID: %d, Name: %s (Type: %s), Created by User %d at %s\n",
//                            chat.getId(),
//                            (chat.getChatName() != null ? chat.getChatName() : "Private Chat"),
//                            chat.getChatType(),
//                            chat.getCreatorId(),
//                            chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
//                }
//            }
//        } else if (response != null) {
//            System.out.println("Failed to get your chats: " + response.getMessage());
//        }
//    }
//
//    private void manageChatParticipants(Scanner scanner) {
//        System.out.println("\n--- Chat Participant Management ---");
//        System.out.println("1. Add Participant to Chat");
//        System.out.println("2. Get Chat Participants");
//        System.out.println("3. Update Participant Role");
//        System.out.println("4. Remove Participant from Chat");
//        System.out.println("5. Back to main menu");
//        System.out.print("Choose an option: ");
//        String choice = scanner.nextLine();
//
//        switch (choice) {
//            case "1":
//                System.out.print("Enter Chat ID: ");
//                int addPartChatId = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter User ID to add: ");
//                int userIdToAdd = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter Role (e.g., member, admin, creator): ");
//                String role = scanner.nextLine();
//                addChatParticipant(addPartChatId, userIdToAdd, role);
//                break;
//            case "2":
//                System.out.print("Enter Chat ID to list participants: ");
//                int getPartChatId = Integer.parseInt(scanner.nextLine());
//                getChatParticipants(getPartChatId);
//                break;
//            case "3":
//                System.out.print("Enter Chat ID where participant exists: ");
//                int updatePartChatId = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter User ID of participant to update role: ");
//                int userIdToUpdateRole = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter new Role (e.g., member, admin, creator): ");
//                String newRole = scanner.nextLine();
//                updateChatParticipantRole(updatePartChatId, userIdToUpdateRole, newRole);
//                break;
//            case "4":
//                System.out.print("Enter Chat ID to remove from: ");
//                int removePartChatId = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter User ID of the participant to remove: ");
//                int userIdToRemove = Integer.parseInt(scanner.nextLine());
//                removeChatParticipant(removePartChatId, userIdToRemove);
//                break;
//            case "5":
//                break;
//            default:
//                System.out.println("Invalid option.");
//        }
//    }
//
//
//    // --------------
//
//
//
//    private void addChatParticipant(int chatId, int userId, String role) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("chat_id", chatId);
//        data.put("user_id", userId);
//        data.put("role", role);
//
//        Request request = new Request(Command.ADD_CHAT_PARTICIPANT, data);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess()) {
//            System.out.println("Participant added successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to add participant: " + response.getMessage());
//        }
//    }
//
//    private void getChatParticipants(int chatId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("chat_id", chatId);
//        Request request = new Request(Command.GET_CHAT_PARTICIPANTS, params);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess()) {
//            Type participantListType = new TypeToken<List<orgs.model.ChatParticipant>>() {}.getType();
//            List<orgs.model.ChatParticipant> participants = gson.fromJson(response.getData(), participantListType);
//            System.out.println("\n--- Participants in Chat ID: " + chatId + " ---");
//            if (participants == null || participants.isEmpty()) {
//                System.out.println("No participants found in this chat or you don't have permission to view them.");
//            } else {
//                for (orgs.model.ChatParticipant p : participants) {
//                    System.out.println("User ID: " + p.getUserId() + ", Role: " + p.getRole() + ", Joined: " + p.getJoinedAt().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
//                }
//            }
//        } else if (response != null) {
//            System.out.println("Failed to get chat participants: " + response.getMessage());
//        }
//    }
//
//    private void updateChatParticipantRole(int chatId, int userId, String newRole) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("chat_id", chatId);
//        data.put("user_id", userId);
//        data.put("new_role", newRole);
//
//        Request request = new Request(Command.UPDATE_CHAT_PARTICIPANT_ROLE, data);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess()) {
//            System.out.println("Participant role updated successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to update participant role: " + response.getMessage());
//        }
//    }
//
//    private void removeChatParticipant(int chatId, int userId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("chat_id", chatId);
//        data.put("user_id", userId);
//
//        Request request = new Request(Command.REMOVE_CHAT_PARTICIPANT, data);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess()) {
//            System.out.println("Participant removed successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to remove participant: " + response.getMessage());
//        }
//    }
//
//    // New method for Contacts management (placeholder)
//    private void manageContacts(Scanner scanner) {
//        System.out.println("\n--- Contact Management ---");
//        System.out.println("1. Add Contact");
//        System.out.println("2. Get My Contacts");
//        System.out.println("3. Remove Contact");
//        System.out.println("4. Back to main menu");
//        System.out.print("Choose an option: ");
//        String choice = scanner.nextLine();
//
//        switch (choice) {
//            case "1":
//                System.out.print("Enter User ID to add as contact: ");
//                int contactIdToAdd = Integer.parseInt(scanner.nextLine());
//                addContact(contactIdToAdd);
//                break;
//            case "2":
//                getContacts();
//                break;
//            case "3":
//                System.out.print("Enter User ID to remove from contacts: ");
//                int contactIdToRemove = Integer.parseInt(scanner.nextLine());
//                removeContact(contactIdToRemove);
//                break;
//            case "4":
//                break;
//            default:
//                System.out.println("Invalid option.");
//        }
//    }
//
//    private void addContact(int contactUserId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("contact_user_id", contactUserId);
//        Request request = new Request(Command.ADD_CONTACT, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Contact added successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to add contact: " + response.getMessage());
//        }
//    }
//
//    private void getContacts() {
//        Request request = new Request(Command.GET_CONTACTS);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess() && "User contacts retrieved.".equals(response.getMessage())) {
//            Type contactListType = new TypeToken<List<User>>() {}.getType(); // Assuming contacts are full User objects
//            List<User> contacts = gson.fromJson(response.getData(), contactListType);
//            System.out.println("\n--- Your Contacts ---");
//            if (contacts == null || contacts.isEmpty()) {
//                System.out.println("You have no contacts.");
//            } else {
//                for (User contact : contacts) {
//                    System.out.printf("ID: %d, Name: %s %s, Phone: %s, Online: %s\n",
//                            contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(), contact.isOnline());
//                }
//            }
//        } else if (response != null) {
//            System.out.println("Failed to get contacts: " + response.getMessage());
//        }
//    }
//
//    private void removeContact(int contactUserId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("contact_user_id", contactUserId);
//        Request request = new Request(Command.REMOVE_CONTACT, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Contact removed successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to remove contact: " + response.getMessage());
//        }
//    }
//
//    private void blockUnblockUser(int targetUserId, String action) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("target_user_id", targetUserId);
//        data.put("action", action); // "block" or "unblock"
//
//        Request request = new Request(Command.BLOCK_UNBLOCK_USER, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println(response.getMessage());
//        } else if (response != null) {
//            System.out.println("Action failed: " + response.getMessage());
//        }
//    }
//
//    private void getNotifications() {
//        Request request = new Request(Command.MY_NOTIFICATIONS);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess() && "User notifications retrieved.".equals(response.getMessage())) {
//            Type notificationListType = new TypeToken<List<orgs.model.Notification>>() {}.getType();
//            List<orgs.model.Notification> notifications = gson.fromJson(response.getData(), notificationListType);
//            System.out.println("\n--- Your Notifications ---");
//            if (notifications == null || notifications.isEmpty()) {
//                System.out.println("You have no notifications.");
//            } else {
//                for (orgs.model.Notification notif : notifications) {
//                    String status = notif.isRead() ? "(READ)" : "(UNREAD)";
//                    System.out.printf("ID: %d %s, Type: %s, Content: %s, Created: %s\n",
//                            notif.getId(), status, notif.getEventType(), notif.getMessage(),
//                            notif.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
//                }
//
//                // Offer to manage notifications
//                System.out.println("\nNotification Options:");
//                System.out.println("1. Mark Notification as Read");
//                System.out.println("2. Delete Notification");
//                System.out.println("3. Back");
//                System.out.print("Choose an option: ");
//                String choice = scanner.nextLine();
//                switch (choice) {
//                    case "1":
//                        System.out.print("Enter Notification ID to mark as read: ");
//                        int notifIdToMarkRead = Integer.parseInt(scanner.nextLine());
//                        markNotificationAsRead(notifIdToMarkRead);
//                        break;
//                    case "2":
//                        System.out.print("Enter Notification ID to delete: ");
//                        int notifIdToDelete = Integer.parseInt(scanner.nextLine());
//                        deleteNotification(notifIdToDelete);
//                        break;
//                    case "3":
//                        break;
//                    default:
//                        System.out.println("Invalid option.");
//                }
//            }
//        } else if (response != null) {
//            System.out.println("Failed to get notifications: " + response.getMessage());
//        }
//    }
//
//    private void markNotificationAsRead(int notificationId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("notification_id", notificationId);
//        Request request = new Request(Command.MARK_NOTIFICATION_AS_READ, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Notification marked as read!");
//        } else if (response != null) {
//            System.out.println("Failed to mark notification as read: " + response.getMessage());
//        }
//    }
//
//    private void deleteNotification(int notificationId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("notification_id", notificationId);
//        Request request = new Request(Command.DELETE_NOTIFICATION, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Notification deleted!");
//        } else if (response != null) {
//            System.out.println("Failed to delete notification: " + response.getMessage());
//        }
//    }
//
//    private void updateDeleteMessage(Scanner scanner) {
//        System.out.println("\n--- Message Management ---");
//        System.out.println("1. Update Message Content");
//        System.out.println("2. Delete Message");
//        System.out.println("3. Mark Message as Read");
//        System.out.println("4. Back to main menu");
//        System.out.print("Choose an option: ");
//        String choice = scanner.nextLine();
//
//        switch (choice) {
//            case "1":
//                System.out.print("Enter Message ID to update: ");
//                int updateMsgId = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter new content: ");
//                String newContent = scanner.nextLine();
//                updateMessage(updateMsgId, newContent);
//                break;
//            case "2":
//                System.out.print("Enter Message ID to delete: ");
//                int deleteMsgId = Integer.parseInt(scanner.nextLine());
//                deleteMessage(deleteMsgId);
//                break;
//            case "3":
//                System.out.print("Enter Message ID to mark as read: ");
//                int readMsgId = Integer.parseInt(scanner.nextLine());
//                markMessageAsRead(readMsgId);
//                break;
//            case "4":
//                break;
//            default:
//                System.out.println("Invalid option.");
//        }
//    }
//
//    private void updateMessage(int messageId, String newContent) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("message_id", messageId);
//        data.put("new_content", newContent);
//        Request request = new Request(Command.UPDATE_MESSAGE, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Message updated successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to update message: " + response.getMessage());
//        }
//    }
//
//    private void deleteMessage(int messageId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("message_id", messageId);
//        Request request = new Request(Command.DELETE_MESSAGE, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Message deleted successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to delete message: " + response.getMessage());
//        }
//    }
//
//    private void markMessageAsRead(int messageId) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("message_id", messageId);
//        data.put("user_id", currentUser.getId()); // Server will need to know who marked it read
//        Request request = new Request(Command.MARK_MESSAGE_AS_READ, data);
//        Response response = sendRequestAndAwaitResponse(request);
//        if (response != null && response.isSuccess()) {
//            System.out.println("Message marked as read!");
//        } else if (response != null) {
//            System.out.println("Failed to mark message as read: " + response.getMessage());
//        }
//    }
//
//    // ---------------
//
//
//
//    private void deleteChat(int chatId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("chatId", chatId);
//        Request request = new Request(Command.DELETE_CHAT, params);
//        Response response = sendRequestAndAwaitResponse(request);
//
//        if (response != null && response.isSuccess()) {
//            System.out.println("Chat " + chatId + " deleted successfully!");
//        } else if (response != null) {
//            System.out.println("Failed to delete chat: " + response.getMessage());
//        }
//    }
//
//
//    private Response sendRequestAndAwaitResponse(Request request) {
//        try {
//            responseQueue.clear(); // Clear any stale responses
//
//            out.println(gson.toJson(request));
//
//            Response response = responseQueue.poll(30, TimeUnit.SECONDS); // 30-second timeout
//
//            if (response == null) {
//                System.err.println("No response from server within timeout for command: " + request.getCommand());
//                return new Response(false, "Server response timed out.", null);
//            }
//            return response;
//        } catch (InterruptedException e) {
//            System.err.println("Waiting for response interrupted: " + e.getMessage());
//            Thread.currentThread().interrupt();
//            return new Response(false, "Client interrupted.", null);
//        }
//    }
//
//    private void sendFileBytes(String filePath, String transferId) {
//        if (filePath == null || filePath.isEmpty()) {
//            System.err.println("No file path provided for transfer.");
//            return;
//        }
//        if (transferId == null || transferId.isEmpty()) {
//            System.err.println("No transfer ID provided by server for file transfer.");
//            return;
//        }
//
//        File file = new File(filePath);
//        if (!file.exists() || !file.isFile()) {
//            System.err.println("File not found or is not a regular file: " + filePath);
//            return;
//        }
//
//        try (Socket fileSocket = new Socket(SERVER_IP, FILE_TRANSFER_PORT);
//             OutputStream os = fileSocket.getOutputStream();
//             BufferedReader serverResponseReader = new BufferedReader(new InputStreamReader(fileSocket.getInputStream()));
//             FileInputStream fis = new FileInputStream(file)) {
//
//            System.out.println("Connecting to file transfer server on port " + FILE_TRANSFER_PORT + "...");
//
//            PrintWriter socketWriter = new PrintWriter(os, true);
//            socketWriter.println(transferId);
//            System.out.println("Sent transferId: " + transferId + " to file server.");
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            long totalBytesSent = 0;
//            long fileSize = file.length();
//
//            System.out.println("Sending file: " + file.getName() + " (" + fileSize + " bytes)");
//
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//                totalBytesSent += bytesRead;
//                // Optional: Print progress
//                // System.out.print("\rSent: " + totalBytesSent + " / " + fileSize + " bytes");
//            }
//            os.flush();
//
//            System.out.println("\nFile '" + file.getName() + "' sent successfully!");
//
//            String fileTransferStatus = serverResponseReader.readLine();
//            if (fileTransferStatus != null) {
//                System.out.println("File server response: " + fileTransferStatus);
//            }
//
//        } catch (IOException e) {
//            System.err.println("Error during file transfer: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//
//    public void getFileByMedia(Media media, String saveDirectory) {
//        if (media == null ) {
//            System.err.println("Error: Invalid media object. Missing mediaId or fileName.");
//            return;
//        }
//        media.setFileName("temp");
//        ;        try {
//            // Step 1: Send a request to the main server asking to initiate the download
//            Map<String, Object> data = new HashMap<>();
//            data.put("mediaId", media.getId());
//            data.put("fileName", media.getFileName());
//
//            Request request = new Request(Command.GET_FILE_BY_MEDIA, data);
//            out.println(gson.toJson(request));
//
//            // Step 2: Wait for a response from the server on the main socket
//            Response response = responseQueue.poll(30, TimeUnit.SECONDS);
//
//            if (response == null) {
//                System.err.println("Server response timed out for file download request.");
//                return;
//            }
//
//            if (response.isSuccess() && "READY_TO_SEND_FILE".equals(response.getMessage())) {
//                Type type = new TypeToken<Map<String, Object>>() {}.getType();
//                Map<String, Object> responseData = gson.fromJson(response.getData(), type);
//                String transferId = (String) responseData.get("transfer_id");
//                long fileSize = ((Double) responseData.get("fileSize")).longValue();
//
//                System.out.println("Server is ready to send the file. Initiating download...");
//                receiveFileBytes(transferId, media.getFileName(), fileSize, saveDirectory);
//
//            } else {
//                System.err.println("Server failed to initiate file download: " + response.getMessage());
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error during file download process: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Add a new method to handle receiving the file bytes
//    private void receiveFileBytes(String transferId, String fileName, long fileSize, String saveDirectory) {
//        try {
//            File saveDir = new File(saveDirectory);
//            if (!saveDir.exists()) {
//                saveDir.mkdirs();
//            }
//            File outputFile = new File(saveDir, fileName);
//
//            try (Socket fileSocket = new Socket(SERVER_IP, FILE_TRANSFER_PORT);
//                 InputStream is = fileSocket.getInputStream();
//                 OutputStream os = fileSocket.getOutputStream();
//                 FileOutputStream fos = new FileOutputStream(outputFile)) {
//
//                System.out.println("Connecting to file transfer server for download...");
//                PrintWriter socketWriter = new PrintWriter(os, true);
//
//                // Send the transfer ID to the file server to identify the file
//                socketWriter.println(transferId);
//                System.out.println("Sent transferId: " + transferId + " to file server for download.");
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                long totalBytesReceived = 0;
//
//                System.out.println("Receiving file: " + fileName + " (" + fileSize + " bytes)");
//
//                while (totalBytesReceived < fileSize && (bytesRead = is.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesReceived))) != -1) {
//                    fos.write(buffer, 0, bytesRead);
//                    totalBytesReceived += bytesRead;
//                    // Optional: Print progress
//                    // System.out.print("\rReceived: " + totalBytesReceived + " / " + fileSize + " bytes");
//                }
//                fos.flush();
//
//                if (totalBytesReceived == fileSize) {
//                    System.out.println("\nFile '" + fileName + "' received successfully and saved to " + outputFile.getAbsolutePath());
//                } else {
//                    System.err.println("\nFile transfer incomplete. Expected: " + fileSize + ", Received: " + totalBytesReceived);
//                    outputFile.delete(); // Clean up incomplete file
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error during file download: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//
//
//    @Override
//    public void close() throws Exception {
//        closeConnection();
//    }
//
//    private void closeConnection() {
//        try {
//            if (socket != null && !socket.isClosed()) {
//                socket.close();
//            }
//            if (out != null) {
//                out.close();
//            }
//            if (in != null) {
//                in.close();
//            }
//            if (scanner != null) {
//                scanner.close();
//            }
//            System.out.println("Client connection closed.");
//        } catch (IOException e) {
//            System.err.println("Error closing client resources: " + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        try (ChatClient client = new ChatClient()) {
//            client.startClient();
//        } catch (Exception e) {
//            System.err.println("Client application error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}