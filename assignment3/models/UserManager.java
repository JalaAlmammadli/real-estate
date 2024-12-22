package assignment3.models;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users;

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
        String filePath = "users.csv";
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the CSV row
                if (parts.length == 4) { // Ensure all fields are present
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
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
        String filePath = "users.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                // Write user data as a CSV row
                writer.write(String.join(",",
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole(),
                        user.getEmail()));
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
        String filePath = "messages.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write message data as a CSV row
            writer.write(String.join(",",
                    buyerUsername,
                    sellerUsername,
                    messageContent));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving message to file: " + e.getMessage());
        }
    }

    // Loads messages from the file
    public void loadMessagesFromFile() {
        String filePath = "messages.csv";
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the CSV row
                if (parts.length == 3) { // Ensure all fields are present
                    String buyerUsername = parts[0];
                    String sellerUsername = parts[1];
                    String messageContent = parts[2];
                    System.out.println("Message from " + buyerUsername + " to " + sellerUsername + ": " + messageContent);
                }
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
