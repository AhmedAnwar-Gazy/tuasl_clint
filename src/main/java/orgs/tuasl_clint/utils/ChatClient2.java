// src/orgs/client/ChatClient.java
package orgs.tuasl_clint.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import orgs.tuasl_clint.models2.Chat;
import orgs.tuasl_clint.models2.Message;
import orgs.tuasl_clint.models2.User;
import orgs.tuasl_clint.protocol.Command;
import orgs.tuasl_clint.protocol.Request;
import orgs.tuasl_clint.protocol.Response;
import orgs.tuasl_clint.utils.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChatClient2 implements AutoCloseable {
    private static final String SERVER_IP = "192.168.1.99"; // Localhost
    private static final int SERVER_PORT = 7373;
    private static final int FILE_TRANSFER_PORT = 7374;
    public static final ChatClient2 chatClient2 =  new ChatClient2();

    public static ChatClient2 getChatClient2(){
        return chatClient2;
    }
    private String currentFilePathToSend;
    private String pendingFileTransferId;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
//    private Gson gson = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, new orgs.tuasl_clint.utils.LocalDateTimeAdapter())
//            .serializeNulls()
//            .create();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampAdapter())  // <-- Add this line
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    private Scanner scanner;
    private User currentUser;

    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();

    public ChatClient2() {
        this.scanner = new Scanner(System.in);
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to chat server on main port.");
            new Thread(this::listenForServerMessages, "ServerListener").start();

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            System.exit(1);
        }
    }

    public void listenForServerMessages() {
        try {
            String serverResponseJson;
            while ((serverResponseJson = in.readLine()) != null) {
                Response response = gson.fromJson(serverResponseJson, Response.class);
                System.out.println("[DEBUG - Raw Server Response]: " + serverResponseJson); // Added for debugging
//
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

                // Handle unsolicited new messages (e.g., from other users)
                if (response.isSuccess() && "New message received".equals(response.getMessage())) {
                    Message newMessage = gson.fromJson(response.getData(), Message.class);
                    System.out.println("\n[NEW MESSAGE from User " + newMessage.getMessageId() + " in Chat ID " + newMessage.getChatId() + "]: " + newMessage.getContent());
                    if (newMessage.getMessageType() != null && !newMessage.getMessageType().equals("text")) {
                        System.out.println("   (Media message, type: " + newMessage.getMessageType() + ", Media ID: " + newMessage.getMediaId() + ")");
                    }
                    System.out.print("> "); // Re-prompt
                }
                // Handle general success/failure messages for commands that don't need special parsing
                else if (response.isSuccess() && (
                        "Login successful!".equals(response.getMessage()) ||
                                "Registration successful!".equals(response.getMessage()) ||
                                "Message sent successfully!".equals(response.getMessage()) ||
                                "Profile updated successfully!".equals(response.getMessage()) ||
                                "User account deleted successfully.".equals(response.getMessage()) ||
                                "Chat created successfully!".equals(response.getMessage()) ||
                                "Chat deleted successfully!".equals(response.getMessage()) ||
                                "Participant added successfully!".equals(response.getMessage()) ||
                                "Participant role updated successfully!".equals(response.getMessage()) ||
                                "Participant removed successfully!".equals(response.getMessage()) ||
                                "Contact added successfully!".equals(response.getMessage()) ||
                                "Contact removed successfully!".equals(response.getMessage()) ||
                                "User blocked successfully!".equals(response.getMessage()) ||
                                "User unblocked successfully!".equals(response.getMessage()) ||
                                "Message updated successfully!".equals(response.getMessage()) ||
                                "Message deleted successfully!".equals(response.getMessage()) ||
                                "Message marked as read!".equals(response.getMessage()) ||
                                "Notification marked as read!".equals(response.getMessage()) ||
                                "Notification deleted successfully!".equals(response.getMessage()) ||
                                "Logged out successfully.".equals(response.getMessage())
                )) {
                    // These are direct responses to a command from the main loop, put them in queue
                    responseQueue.put(response);
                }
                // Handle general failure messages
                else if (!response.isSuccess() && (
                        response.getMessage().startsWith("Login failed") ||
                                response.getMessage().startsWith("Registration failed") ||
                                response.getMessage().startsWith("Failed to send message") ||
                                response.getMessage().startsWith("Error sending message") ||
                                response.getMessage().startsWith("Missing file details") ||
                                response.getMessage().startsWith("You are not a participant") ||
                                response.getMessage().startsWith("Failed to get profile") ||
                                response.getMessage().startsWith("Failed to update profile") ||
                                response.getMessage().startsWith("Failed to delete user") ||
                                response.getMessage().startsWith("Failed to create chat") ||
                                response.getMessage().startsWith("Failed to delete chat") ||
                                response.getMessage().startsWith("Failed to add participant") ||
                                response.getMessage().startsWith("Failed to get chat participants") ||
                                response.getMessage().startsWith("Failed to update participant role") ||
                                response.getMessage().startsWith("Failed to remove participant") ||
                                response.getMessage().startsWith("Failed to add contact") ||
                                response.getMessage().startsWith("Failed to get contacts") ||
                                response.getMessage().startsWith("Failed to remove contact") ||
                                response.getMessage().startsWith("Failed to block/unblock user") ||
                                response.getMessage().startsWith("Failed to get notifications") ||
                                response.getMessage().startsWith("Failed to mark notification") ||
                                response.getMessage().startsWith("Failed to delete notification") ||
                                response.getMessage().startsWith("Failed to update message") ||
                                response.getMessage().startsWith("Failed to delete message") ||
                                response.getMessage().startsWith("Failed to mark message as read") ||
                                response.getMessage().startsWith("Authentication required") ||
                                response.getMessage().startsWith("Server internal error") ||
                                response.getMessage().startsWith("Unknown command")
                )) {
                    // These are direct responses to a command from the main loop, put them in queue
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "Messages retrieved" response
                else if (response.isSuccess() && "Messages retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response); // Put in queue for handleUserInput to process
                }
                // NEW: Specific handling for "All users retrieved." response
                else if (response.isSuccess() && "All users retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "User profile retrieved." response
                else if (response.isSuccess() && "User profile retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "All chats retrieved for user." response
                else if (response.isSuccess() && "All chats retrieved for user.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "Chat details retrieved." response
                else if (response.isSuccess() && "Chat details retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "User contacts retrieved." response
                else if (response.isSuccess() && "User contacts retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                // NEW: Specific handling for "User notifications retrieved." response
                else if (response.isSuccess() && "User notifications retrieved.".equals(response.getMessage())) {
                    responseQueue.put(response);
                }
                else {
                    // Fallback for any other unexpected response or unhandled success/failure messages
                    // You might want to log these or handle them as generic responses.
                    System.out.println("[DEBUG - Unhandled Response]: " + serverResponseJson);
                    responseQueue.put(response); // Still put it in case the main loop is waiting
                }
            }
        } catch (SocketException e) {
            System.out.println("Server connection lost: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Listener thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            closeConnection();
        }
    }


    public void startClient() {
        System.out.println("Welcome to the Tuasil Messaging Client!");

        while (currentUser == null) {
            System.out.println("\n--- Auth Options ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Choose an option: ");
            String authChoice = scanner.nextLine();

            Map<String, Object> authData = new HashMap<>();
            String phoneNumber, password, firstName, lastName;

            if ("1".equals(authChoice)) {
                System.out.print("Enter phone number: ");
                phoneNumber = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                authData.put("phone_number", phoneNumber);
                authData.put("password", password);

                Request loginRequest = new Request(Command.LOGIN, authData);
                Response loginResponse = sendRequestAndAwaitResponse(loginRequest);

                if (loginResponse != null && loginResponse.isSuccess()) {
                    this.currentUser = gson.fromJson(loginResponse.getData(), User.class);
                    System.out.println("Logged in as: " + currentUser.getPhoneNumber() + " (" + currentUser.getFirstName() + " " + currentUser.getLastName() + ")");
                    break;
                } else if (loginResponse != null) {
                    System.out.println("Login failed: " + loginResponse.getMessage());
                }
            } else if ("2".equals(authChoice)) {
                System.out.print("Enter phone number: ");
                phoneNumber = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                System.out.print("Enter first name: ");
                firstName = scanner.nextLine();
                System.out.print("Enter last name: ");
                lastName = scanner.nextLine();

                authData.put("phone_number", phoneNumber);
                authData.put("password", password);
                authData.put("first_name", firstName);
                authData.put("last_name", lastName);

                Request registerRequest = new Request(Command.REGISTER, authData);
                Response registerResponse = sendRequestAndAwaitResponse(registerRequest);

                if (registerResponse != null && registerResponse.isSuccess()) {
                    System.out.println("Registration successful! You can now log in.");
                } else if (registerResponse != null) {
                    System.out.println("Registration failed: " + registerResponse.getMessage());
                }
            } else {
                System.out.println("Invalid option.");
            }
        }

        while (currentUser != null) {
            displayCommands();
            System.out.print("Enter command number: ");
            String commandInput = scanner.nextLine();
            handleUserInput(commandInput);
        }
    }
    public User Login(String phoneNumber, String Password){
        Map<String,Object> authData = new HashMap<>();
        authData.put("phone_number", phoneNumber);
        authData.put("password", Password);

        Request loginRequest = new Request(Command.LOGIN, authData);
        Response loginResponse = sendRequestAndAwaitResponse(loginRequest);

        if (loginResponse != null && loginResponse.isSuccess()) {
            User u = gson.fromJson(loginResponse.getData(), User.class);
            currentUser = u;
            System.out.println("Logged in as: " + u.getPhoneNumber() + " (" + u.getFirstName() + " " + u.getLastName() + ")");
            return u;
        } else if (loginResponse != null) {
            System.out.println("Login failed: " + loginResponse.getMessage());
            return null;
        }
        return null;
    }
    private void displayCommands() {
        System.out.println("\n--- Commands ---");
        System.out.println("1. Send Text Message");
        System.out.println("2. Send Image");
        System.out.println("3. Send Video");
        System.out.println("4. Send Voice Note");
        System.out.println("5. Send File (General)");
        System.out.println("6. Get Chat Messages");
        System.out.println("7. Create Chat");
        System.out.println("8. Manage Profile (View/Update/Delete)");
        System.out.println("9. Get All Users");
        System.out.println("10. My Chats");
        System.out.println("11. Manage Chat Participants");
        System.out.println("12. My Contacts (View/Manage)");
        System.out.println("13. Block/Unblock User");
        System.out.println("14. My Notifications");
        System.out.println("15. Update/Delete Message");
        System.out.println("16. Delete Chat");
        System.out.println("17. Logout");
    }
    public Response sendTextMessage(int chatId,String content){
        Map<String , Object> data = new HashMap<>();

        data.put("chat_id", chatId);
        data.put("content", content);
        Request request = new Request(Command.SEND_TEXT_MESSAGE, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null) {
            System.out.println("Server Response: " + response.getMessage());
            // Special handling for message sent to clear file transfer details
            if (response.isSuccess() && "Message sent successfully!".equals(response.getMessage()) && currentFilePathToSend != null) {
                System.out.println("File transfer details cleared.");
                currentFilePathToSend = null;
                pendingFileTransferId = null;
            }
        }
        return response;
    }
    private void handleUserInput(String commandInput) {
        Request request = null;
        Map<String, Object> data = new HashMap<>();

        try {
            switch (commandInput) {
                case "1": // Send Text Message
                    System.out.print("Enter Chat ID: ");
                    int textChatId = Integer.parseInt(scanner.nextLine()); // Use nextLine() then parse
                    System.out.print("Enter message content: ");
                    String textContent = scanner.nextLine();
                    data.put("chat_id", textChatId);
                    data.put("content", textContent);
                    request = new Request(Command.SEND_TEXT_MESSAGE, data);
                    break;

                case "2": // Send Image
                case "3": // Send Video
                case "4": // Send Voice Note
                case "5": // Send File (General)
                    System.out.print("Enter Chat ID: ");
                    int mediaChatId = Integer.parseInt(scanner.nextLine()); // Use nextLine() then parse
                    System.out.print("Enter local file path (e.g., C:/images/photo.jpg or /home/user/video.mp4): ");
                    String filePath = scanner.nextLine();

                    File file = new File(filePath);

                    if (!file.exists() || !file.isFile()) {
                        System.out.println("Error: File not found or is not a regular file at " + filePath);
                        return;
                    }
                    currentFilePathToSend = filePath;
                    long fileSize = file.length();
                    String fileName = file.getName();

                    System.out.print("Enter caption (optional, press Enter to skip): ");
                    String caption = scanner.nextLine();

                    String messageType;
                    Command mediaCommand;
                    switch (commandInput) {
                        case "2": mediaCommand = Command.SEND_IMAGE; messageType = "image"; break;
                        case "3": mediaCommand = Command.SEND_VIDEO; messageType = "video"; break;
                        case "4": mediaCommand = Command.SEND_VOICE_NOTE; messageType = "voiceNote"; break;
                        case "5": mediaCommand = Command.SEND_FILE; messageType = "file"; break;
                        default: mediaCommand = Command.UNKNOWN_COMMAND; messageType = "unknown"; break;
                    }

                    data.put("chat_id", mediaChatId);
                    data.put("content", caption.isEmpty() ? null : caption);
                    data.put("file_name", fileName);
                    data.put("file_size", fileSize);
                    data.put("message_type", messageType);
                    request = new Request(mediaCommand, data);
                    break;

                case "6": // Get Chat Messages
                    System.out.print("Enter Chat ID: ");
                    int getChatId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter limit (number of messages to fetch): ");
                    int limit = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter offset (starting point): ");
                    int offset = Integer.parseInt(scanner.nextLine());
                    data.put("chat_id", getChatId);
                    data.put("limit", limit);
                    data.put("offset", offset);
                    request = new Request(Command.GET_CHAT_MESSAGES, data);

                    // --- Processing the response for GET_CHAT_MESSAGES ---
                    Response messagesResponse = sendRequestAndAwaitResponse(request);
                    if (messagesResponse != null && messagesResponse.isSuccess() && "Messages retrieved.".equals(messagesResponse.getMessage())) {
                        Type messageListType = new TypeToken<List<Message>>() {}.getType();
                        List<Message> messages = gson.fromJson(messagesResponse.getData(), messageListType);
                        System.out.println("\n--- Messages in Chat ID: " + getChatId + " ---");
                        if (messages == null || messages.isEmpty()) {
                            System.out.println("No messages found in this chat.");
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            for (Message msg : messages) {
                                String senderInfo = (msg.getSenderUserId() == currentUser.getUserId()) ? "You" : "User " + msg.getSenderUserId();
                                String content = msg.getContent() != null ? msg.getContent() : "[No content]";
                                String mediaInfo = "";
                                if (msg.getMessageType() != null && !msg.getMessageType().equals("text")) {
                                    mediaInfo = String.format(" [Type: %s, Media ID: %s, File: %s]",
                                            msg.getMessageType(), msg.getMediaId() != null ? msg.getMediaId() : "N/A", msg.getMediaId() != null ? msg.getContent() : "N/A");
                                }
//                                System.out.printf("[%s] %s: %s%s (Views: %d)\n",
//                                        msg.getSentAt().format(formatter), senderInfo, content, mediaInfo, msg.getViewCount());
                            }
                        }
                    } else if (messagesResponse != null) {
                        System.out.println("Failed to get messages: " + messagesResponse.getMessage());
                    }
                    return; // Return here as the response has been handled
                // --- End of processing GET_CHAT_MESSAGES ---

                case "7": // Create Chat
                    System.out.print("Enter chat type (private, group, channel): ");
                    String chatType = scanner.nextLine();
                    System.out.print("Enter chat name (optional for private, required for group/channel): ");
                    String chatName = scanner.nextLine();
                    System.out.print("Enter chat description (optional): ");
                    String chatDescription = scanner.nextLine();
                    System.out.print("Enter public link (optional, for public channels only): ");
                    String publicLink = scanner.nextLine();

                    data.put("chat_type", chatType);
                    data.put("chat_name", chatName.isEmpty() ? null : chatName);
                    data.put("chat_description", chatDescription.isEmpty() ? null : chatDescription);
                    data.put("public_link", publicLink.isEmpty() ? null : publicLink);
                    request = new Request(Command.CREATE_CHAT, data);
                    break;

                case "8": // Manage Profile
                    manageProfile(scanner);
                    return;
                case "9": // Get All Users
                    request = new Request(Command.GET_ALL_USERS);
                    Response allUsersResponse = sendRequestAndAwaitResponse(request);
                    if (allUsersResponse != null && allUsersResponse.isSuccess() && "All users retrieved.".equals(allUsersResponse.getMessage())) {
                        Type userListType = new TypeToken<List<User>>() {}.getType();
                        List<User> users = gson.fromJson(allUsersResponse.getData(), userListType);
                        System.out.println("\n--- All Registered Users ---");
                        if (users == null || users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            for (User user : users) {
                                System.out.printf("ID: %d, Name: %s %s, Phone: %s, Online: %s\n",
                                        user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.isOnline());
                            }
                        }
                    } else if (allUsersResponse != null) {
                        System.out.println("Failed to get all users: " + allUsersResponse.getMessage());
                    }
                    return;

                case "10": // My Chats
                    getUserChats(); // This method already calls sendRequestAndAwaitResponse and processes it
                    return;
                case "11": // Manage Chat Participants
                    manageChatParticipants(scanner);
                    return;
                case "12": // My Contacts
                    manageContacts(scanner); // New method to add
                    return;
                case "13": // Block/Unblock User
                    System.out.print("Enter User ID to block/unblock: ");
                    int targetUserId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Action (block/unblock): ");
                    String action = scanner.nextLine();
                    blockUnblockUser(targetUserId, action);
                    return;
                case "14": // My Notifications
                    getNotifications();
                    return;
                case "15": // Update/Delete Message
                    updateDeleteMessage(scanner);
                    return;
                case "16": // Delete Chat
                    System.out.print("Enter Chat ID to delete: ");
                    int deleteChatId = Integer.parseInt(scanner.nextLine());
                    deleteChat(deleteChatId);
                    return;
                case "17": // Logout
                    request = new Request(Command.LOGOUT);
                    Response logoutResponse = sendRequestAndAwaitResponse(request);
                    if (logoutResponse != null && logoutResponse.isSuccess()) {
                        System.out.println(logoutResponse.getMessage());
                        currentUser = null; // Clear current user on successful logout
                    } else if (logoutResponse != null) {
                        System.out.println("Logout failed: " + logoutResponse.getMessage());
                    }
                    return; // Exit handleUserInput as logout changes client state

                default:
                    System.out.println("Invalid command number.");
                    return;
            }

            if (request != null) {
                // For commands handled by the main switch, send the request and print a general response.
                // Commands like '6', '9', '10', '12', '13', '14', '15', '16', '17' are handled specially
                // with their own response parsing and then return.
                // This block is for commands that just need a general success/failure message.
                Response response = sendRequestAndAwaitResponse(request);
                if (response != null) {
                    System.out.println("Server Response: " + response.getMessage());
                    // Special handling for message sent to clear file transfer details
                    if (response.isSuccess() && "Message sent successfully!".equals(response.getMessage()) && currentFilePathToSend != null) {
                        System.out.println("File transfer details cleared.");
                        currentFilePathToSend = null;
                        pendingFileTransferId = null;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number for IDs/limits.");
            // You might want to re-prompt or break out of a loop here
        } catch (Exception e) {
            System.err.println("Error handling user input: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean login(String username, String password) {
        // This method is no longer directly used by startClient's auth loop.
        // The auth loop now sends a login request and processes it directly.
        // This method would be for internal logic if you had a different login flow.
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        Request request = new Request(Command.LOGIN, data);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            this.currentUser = gson.fromJson(response.getData(), User.class);
            return true;
        } else {
            return false;
        }
    }

    private Response sendRequestAndAwaitResponse(Request request) {
        try {
            responseQueue.clear(); // Clear any stale responses

            out.println(gson.toJson(request));

            Response response = responseQueue.poll(30, TimeUnit.SECONDS); // 30-second timeout

            if (response == null) {
                System.err.println("No response from server within timeout for command: " + request.getCommand());
                return new Response(false, "Server response timed out.", null);
            }
            return response;
        } catch (InterruptedException e) {
            System.err.println("Waiting for response interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return new Response(false, "Client interrupted.", null);
        }
    }

    private void sendFileBytes(String filePath, String transferId) {
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("No file path provided for transfer.");
            return;
        }
        if (transferId == null || transferId.isEmpty()) {
            System.err.println("No transfer ID provided by server for file transfer.");
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("File not found or is not a regular file: " + filePath);
            return;
        }

        try (Socket fileSocket = new Socket(SERVER_IP, FILE_TRANSFER_PORT);
             OutputStream os = fileSocket.getOutputStream();
             BufferedReader serverResponseReader = new BufferedReader(new InputStreamReader(fileSocket.getInputStream()));
             FileInputStream fis = new FileInputStream(file)) {

            System.out.println("Connecting to file transfer server on port " + FILE_TRANSFER_PORT + "...");

            PrintWriter socketWriter = new PrintWriter(os, true);
            socketWriter.println(transferId);
            System.out.println("Sent transferId: " + transferId + " to file server.");

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesSent = 0;
            long fileSize = file.length();

            System.out.println("Sending file: " + file.getName() + " (" + fileSize + " bytes)");

            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                totalBytesSent += bytesRead;
                // Optional: Print progress
                // System.out.print("\rSent: " + totalBytesSent + " / " + fileSize + " bytes");
            }
            os.flush();

            System.out.println("\nFile '" + file.getName() + "' sent successfully!");

            String fileTransferStatus = serverResponseReader.readLine();
            if (fileTransferStatus != null) {
                System.out.println("File server response: " + fileTransferStatus);
            }

        } catch (IOException e) {
            System.err.println("Error during file transfer for " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    // --- Methods you specifically asked for (continued) ---

    private void manageProfile(Scanner scanner) {
        System.out.println("\n--- Your Profile ---");
        if (currentUser == null) {
            System.out.println("You must be logged in to manage profile.");
            return;
        }
        // Always fetch the latest profile before displaying
        Map<String, Object> params = new HashMap<>();
        params.put("userId", currentUser.getUserId());
        Request request = new Request(Command.GET_USER_PROFILE, params);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess() && "User profile retrieved.".equals(response.getMessage())) {
            User profile = gson.fromJson(response.getData(), User.class);
            if (profile != null) {
                currentUser = profile; // Update current user with latest profile data
                System.out.println("ID: " + currentUser.getUserId());
                System.out.println("Phone: " + currentUser.getPhoneNumber());
                System.out.println("First Name: " + currentUser.getFirstName());
                System.out.println("Last Name: " + (currentUser.getLastName() != null ? currentUser.getLastName() : "N/A"));
                System.out.println("Bio: " + (currentUser.getBio() != null ? currentUser.getBio() : "N/A"));
                System.out.println("Profile Picture URL: " + (currentUser.getProfilePictureUrl() != null ? currentUser.getProfilePictureUrl() : "N/A"));
                System.out.println("Online: " + currentUser.isOnline());
                if (currentUser.getLastSeenAt() != null) {
//                    System.out.println("Last Seen: " + currentUser.getLastSeenAt().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
                }
            } else {
                System.out.println("Failed to parse user profile data.");
            }
        } else if (response != null) {
            System.out.println("Failed to get profile: " + response.getMessage());
        }

        System.out.println("\nProfile Options:");
        System.out.println("1. Update Profile");
        System.out.println("2. Delete Account (WARNING: IRREVERSIBLE)");
        System.out.println("3. Back to main menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                updateUserProfile(scanner);
                break;
            case "2":
                System.out.print("Are you sure you want to delete your account? Type 'YES' to confirm: ");
                String confirm = scanner.nextLine();
                if ("YES".equals(confirm)) {
                    deleteUser();
                } else {
                    System.out.println("Account deletion cancelled.");
                }
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void updateUserProfile(Scanner scanner) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("userId", currentUser.getUserId()); // Always send user ID

        System.out.print("Enter new first name (leave blank to keep current: " + currentUser.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) updates.put("first_name", firstName);

        System.out.print("Enter new last name (leave blank to keep current: " + (currentUser.getLastName() != null ? currentUser.getLastName() : "") + "): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) updates.put("last_name", lastName);

        System.out.print("Enter new bio (leave blank to keep current: " + (currentUser.getBio() != null ? currentUser.getBio() : "") + "): ");
        String bio = scanner.nextLine();
        // Allow setting bio to null/empty string
        if (bio != null) updates.put("bio", bio.isEmpty() ? null : bio);

        System.out.print("Enter new profile picture URL (leave blank to keep current or type 'clear' to remove): ");
        String profilePicUrl = scanner.nextLine();
        if ("clear".equalsIgnoreCase(profilePicUrl)) {
            updates.put("profile_picture_url", null);
        } else if (!profilePicUrl.isEmpty()) {
            updates.put("profile_picture_url", profilePicUrl);
        }

        Request request = new Request(Command.UPDATE_USER_PROFILE, updates);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Profile updated successfully!");
            // Update local currentUser object to reflect changes
            currentUser = gson.fromJson(response.getData(), User.class);
        } else if (response != null) {
            System.out.println("Failed to update profile: " + response.getMessage());
        }
    }


    private void deleteUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", currentUser.getUserId());
        Request request = new Request(Command.DELETE_USER, params);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Your account has been deleted successfully.");
            this.currentUser = null; // Log out the user locally
        } else if (response != null) {
            System.out.println("Failed to delete account: " + response.getMessage());
        }
    }

    private void getUserChats() {
        Request request = new Request(Command.GET_USER_CHATS);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess() && "All chats retrieved for user.".equals(response.getMessage())) {
            Type chatListType = new TypeToken<List<Chat>>() {}.getType();
            List<Chat> chats = gson.fromJson(response.getData(), chatListType);
            System.out.println("\n--- Your Chats ---");
            if (chats == null || chats.isEmpty()) {
                System.out.println("You are not a participant in any chats.");
            } else {
                for (Chat chat : chats) {
                    System.out.printf("ID: %d, Name: %s (Type: %s), Created by User %d at %s\n",
                            chat.getChatType(),
                            (chat.getChatName() != null ? chat.getChatName() : "Private Chat"),
                            chat.getChatType(),
                            chat.getCreatorId(),
                            chat.getCreatedAt());//.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
        } else if (response != null) {
            System.out.println("Failed to get your chats: " + response.getMessage());
        }
    }


    private void deleteChat(int chatId) {
        Map<String, Object> params = new HashMap<>();
        params.put("chatId", chatId);
        Request request = new Request(Command.DELETE_CHAT, params);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Chat " + chatId + " deleted successfully!");
        } else if (response != null) {
            System.out.println("Failed to delete chat: " + response.getMessage());
        }
    }

    private void manageChatParticipants(Scanner scanner) {
        System.out.println("\n--- Chat Participant Management ---");
        System.out.println("1. Add Participant to Chat");
        System.out.println("2. Get Chat Participants");
        System.out.println("3. Update Participant Role");
        System.out.println("4. Remove Participant from Chat");
        System.out.println("5. Back to main menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter Chat ID: ");
                int addPartChatId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter User ID to add: ");
                int userIdToAdd = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Role (e.g., member, admin, creator): ");
                String role = scanner.nextLine();
                addChatParticipant(addPartChatId, userIdToAdd, role);
                break;
            case "2":
                System.out.print("Enter Chat ID to list participants: ");
                int getPartChatId = Integer.parseInt(scanner.nextLine());
                getChatParticipants(getPartChatId);
                break;
            case "3":
                System.out.print("Enter Chat ID where participant exists: ");
                int updatePartChatId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter User ID of participant to update role: ");
                int userIdToUpdateRole = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new Role (e.g., member, admin, creator): ");
                String newRole = scanner.nextLine();
                updateChatParticipantRole(updatePartChatId, userIdToUpdateRole, newRole);
                break;
            case "4":
                System.out.print("Enter Chat ID to remove from: ");
                int removePartChatId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter User ID of the participant to remove: ");
                int userIdToRemove = Integer.parseInt(scanner.nextLine());
                removeChatParticipant(removePartChatId, userIdToRemove);
                break;
            case "5":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }


    private void addChatParticipant(int chatId, int userId, String role) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat_id", chatId);
        data.put("user_id", userId);
        data.put("role", role);

        Request request = new Request(Command.ADD_CHAT_PARTICIPANT, data);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Participant added successfully!");
        } else if (response != null) {
            System.out.println("Failed to add participant: " + response.getMessage());
        }
    }

    private void getChatParticipants(int chatId) {
        Map<String, Object> params = new HashMap<>();
        params.put("chat_id", chatId);
        Request request = new Request(Command.GET_CHAT_PARTICIPANTS, params);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            Type participantListType = new TypeToken<List<orgs.tuasl_clint.models2.ChatParticipant>>() {}.getType();
            List<orgs.tuasl_clint.models2.ChatParticipant> participants = gson.fromJson(response.getData(), participantListType);
            System.out.println("\n--- Participants in Chat ID: " + chatId + " ---");
            if (participants == null || participants.isEmpty()) {
                System.out.println("No participants found in this chat or you don't have permission to view them.");
            } else {
                for (orgs.tuasl_clint.models2.ChatParticipant p : participants) {
                    System.out.println("User ID: " + p.getUserId() + ", Role: " + p.getRole() + ", Joined: " + p.getJoinedAt());
                }
            }
        } else if (response != null) {
            System.out.println("Failed to get chat participants: " + response.getMessage());
        }
    }

    private void updateChatParticipantRole(int chatId, int userId, String newRole) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat_id", chatId);
        data.put("user_id", userId);
        data.put("new_role", newRole);

        Request request = new Request(Command.UPDATE_CHAT_PARTICIPANT_ROLE, data);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Participant role updated successfully!");
        } else if (response != null) {
            System.out.println("Failed to update participant role: " + response.getMessage());
        }
    }

    private void removeChatParticipant(int chatId, int userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat_id", chatId);
        data.put("user_id", userId);

        Request request = new Request(Command.REMOVE_CHAT_PARTICIPANT, data);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess()) {
            System.out.println("Participant removed successfully!");
        } else if (response != null) {
            System.out.println("Failed to remove participant: " + response.getMessage());
        }
    }

    // New method for Contacts management (placeholder)
    private void manageContacts(Scanner scanner) {
        System.out.println("\n--- Contact Management ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Get My Contacts");
        System.out.println("3. Remove Contact");
        System.out.println("4. Back to main menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter User ID to add as contact: ");
                int contactIdToAdd = Integer.parseInt(scanner.nextLine());
                addContact(contactIdToAdd);
                break;
            case "2":
                getContacts();
                break;
            case "3":
                System.out.print("Enter User ID to remove from contacts: ");
                int contactIdToRemove = Integer.parseInt(scanner.nextLine());
                removeContact(contactIdToRemove);
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void addContact(int contactUserId) {
        Map<String, Object> data = new HashMap<>();
        data.put("contact_user_id", contactUserId);
        Request request = new Request(Command.ADD_CONTACT, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Contact added successfully!");
        } else if (response != null) {
            System.out.println("Failed to add contact: " + response.getMessage());
        }
    }

    private void getContacts() {
        Request request = new Request(Command.GET_CONTACTS);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess() && "User contacts retrieved.".equals(response.getMessage())) {
            Type contactListType = new TypeToken<List<User>>() {}.getType(); // Assuming contacts are full User objects
            List<User> contacts = gson.fromJson(response.getData(), contactListType);
            System.out.println("\n--- Your Contacts ---");
            if (contacts == null || contacts.isEmpty()) {
                System.out.println("You have no contacts.");
            } else {
                for (User contact : contacts) {
                    System.out.printf("ID: %d, Name: %s %s, Phone: %s, Online: %s\n",
                            contact.getUserId(), contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(), contact.isOnline());
                }
            }
        } else if (response != null) {
            System.out.println("Failed to get contacts: " + response.getMessage());
        }
    }

    private void removeContact(int contactUserId) {
        Map<String, Object> data = new HashMap<>();
        data.put("contact_user_id", contactUserId);
        Request request = new Request(Command.REMOVE_CONTACT, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Contact removed successfully!");
        } else if (response != null) {
            System.out.println("Failed to remove contact: " + response.getMessage());
        }
    }

    private void blockUnblockUser(int targetUserId, String action) {
        Map<String, Object> data = new HashMap<>();
        data.put("target_user_id", targetUserId);
        data.put("action", action); // "block" or "unblock"

        Request request = new Request(Command.BLOCK_UNBLOCK_USER, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println(response.getMessage());
        } else if (response != null) {
            System.out.println("Action failed: " + response.getMessage());
        }
    }

    private void getNotifications() {
        Request request = new Request(Command.MY_NOTIFICATIONS);
        Response response = sendRequestAndAwaitResponse(request);

        if (response != null && response.isSuccess() && "User notifications retrieved.".equals(response.getMessage())) {
            Type notificationListType = new TypeToken<List<orgs.tuasl_clint.models2.Notification>>() {}.getType();
            List<orgs.tuasl_clint.models2.Notification> notifications = gson.fromJson(response.getData(), notificationListType);
            System.out.println("\n--- Your Notifications ---");
            if (notifications == null || notifications.isEmpty()) {
                System.out.println("You have no notifications.");
            } else {
                for (orgs.tuasl_clint.models2.Notification notif : notifications) {
                    String status = notif.isRead() ? "(READ)" : "(UNREAD)";
                    System.out.printf("ID: %d %s, Type: %s, Content: %s, Created: %s\n",
                            notif.getId(), status, notif.getEventType(), notif.getMessage(),
                            notif.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
                }

                // Offer to manage notifications
                System.out.println("\nNotification Options:");
                System.out.println("1. Mark Notification as Read");
                System.out.println("2. Delete Notification");
                System.out.println("3. Back");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        System.out.print("Enter Notification ID to mark as read: ");
                        int notifIdToMarkRead = Integer.parseInt(scanner.nextLine());
                        markNotificationAsRead(notifIdToMarkRead);
                        break;
                    case "2":
                        System.out.print("Enter Notification ID to delete: ");
                        int notifIdToDelete = Integer.parseInt(scanner.nextLine());
                        deleteNotification(notifIdToDelete);
                        break;
                    case "3":
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } else if (response != null) {
            System.out.println("Failed to get notifications: " + response.getMessage());
        }
    }

    private void markNotificationAsRead(int notificationId) {
        Map<String, Object> data = new HashMap<>();
        data.put("notification_id", notificationId);
        Request request = new Request(Command.MARK_NOTIFICATION_AS_READ, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Notification marked as read!");
        } else if (response != null) {
            System.out.println("Failed to mark notification as read: " + response.getMessage());
        }
    }

    private void deleteNotification(int notificationId) {
        Map<String, Object> data = new HashMap<>();
        data.put("notification_id", notificationId);
        Request request = new Request(Command.DELETE_NOTIFICATION, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Notification deleted!");
        } else if (response != null) {
            System.out.println("Failed to delete notification: " + response.getMessage());
        }
    }

    private void updateDeleteMessage(Scanner scanner) {
        System.out.println("\n--- Message Management ---");
        System.out.println("1. Update Message Content");
        System.out.println("2. Delete Message");
        System.out.println("3. Mark Message as Read");
        System.out.println("4. Back to main menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter Message ID to update: ");
                int updateMsgId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new content: ");
                String newContent = scanner.nextLine();
                updateMessage(updateMsgId, newContent);
                break;
            case "2":
                System.out.print("Enter Message ID to delete: ");
                int deleteMsgId = Integer.parseInt(scanner.nextLine());
                deleteMessage(deleteMsgId);
                break;
            case "3":
                System.out.print("Enter Message ID to mark as read: ");
                int readMsgId = Integer.parseInt(scanner.nextLine());
                markMessageAsRead(readMsgId);
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void updateMessage(int messageId, String newContent) {
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", messageId);
        data.put("new_content", newContent);
        Request request = new Request(Command.UPDATE_MESSAGE, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Message updated successfully!");
        } else if (response != null) {
            System.out.println("Failed to update message: " + response.getMessage());
        }
    }

    private void deleteMessage(int messageId) {
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", messageId);
        Request request = new Request(Command.DELETE_MESSAGE, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Message deleted successfully!");
        } else if (response != null) {
            System.out.println("Failed to delete message: " + response.getMessage());
        }
    }

    private void markMessageAsRead(int messageId) {
        Map<String, Object> data = new HashMap<>();
        data.put("message_id", messageId);
        data.put("user_id", currentUser.getUserId()); // Server will need to know who marked it read
        Request request = new Request(Command.MARK_MESSAGE_AS_READ, data);
        Response response = sendRequestAndAwaitResponse(request);
        if (response != null && response.isSuccess()) {
            System.out.println("Message marked as read!");
        } else if (response != null) {
            System.out.println("Failed to mark message as read: " + response.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        closeConnection();
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("Client resources closed.");
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (ChatClient2 client = new ChatClient2()) {
            client.startClient();
        } catch (Exception e) {
            System.err.println("Client application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}