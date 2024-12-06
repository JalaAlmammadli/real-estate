package assignment3;
public class User {
    private String username;
    private String password;
    private String role;
    private String email;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = username + "@email.com";
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
}
