package assignment3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users;
    private final String filePath = "users.txt";
    private final String messagesFilePath = "messages.txt";

    public UserManager() {
        users = new HashMap<>();
        loadUsersFromFile();
    }
    public Map<String, User> getAllUsers() {
        return users;
    }
    // Registers a new user
    public boolean registerUser(String username, String password, String role) {
        if (users.containsKey(username)) {
            return false;
        }
        User newUser = new User(username, password, role);
        users.put(username, newUser);
        saveUsersToFile();
        return true;
    }

    // Authenticates a user
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Loads users from the file
    private void loadUsersFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    String email = parts[3];
                    User user = new User(username, password, role);
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }

    // Saves users to the file
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
        }
    }

    // Sends a message from a buyer to a seller
    public void sendMessageToSeller(String buyerUsername, String sellerUsername, String messageContent) {
        User buyer = users.get(buyerUsername);
        User seller = users.get(sellerUsername);

        if (buyer != null && seller != null) {
            saveMessageToFile(buyerUsername, sellerUsername, messageContent);
            buyer.incrementMessageCount(sellerUsername);
            saveUsersToFile();
            System.out.println("Message sent from " + buyerUsername + " to " + sellerUsername);
        } else {
            System.out.println("Invalid buyer or seller username.");
        }
    }

    // Saves a message to the file
    private void saveMessageToFile(String buyerUsername, String sellerUsername, String messageContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(messagesFilePath, true))) {
            writer.write(buyerUsername + " -> " + sellerUsername + ": " + messageContent);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving message to file: " + e.getMessage());
        }
    }

    // Loads messages from the file
    public void loadMessagesFromFile() {
        File file = new File(messagesFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading messages from file: " + e.getMessage());
        }
    }

    // Retrieves a user by email
    public User getUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}
