package assignment3;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String username;
    private String password;
    private String role;
    private String email;
    private int messageCount;
    private Set<String> messageSenders;

public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.email = username + "@email.com";
    this.messageCount = 0;
    this.messageSenders = new HashSet<>();
}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + role + "," + email;
    }
    public void incrementMessageCount(String sender) {
        messageCount++;
        messageSenders.add(sender);
    }
    
    public int getMessageCount() {
        return messageCount;
    }
    
    public Set<String> getMessageSenders() {
        return messageSenders;
    }
    
}
