package assignment3;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users;
    private final String filePath = "users.txt";

    public UserManager() {
        users = new HashMap<>();
        loadUsersFromFile();
    }

    public boolean registerUser(String username, String password, String role) {
        if (users.containsKey(username)) {
            return false;
        }
        User newUser = new User(username, password, role);
        users.put(username, newUser);
        saveUsersToFile(); 
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

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
}
