package assignment3;
import java.util.Scanner;

public class CommandHandler {
    private UserManager userManager;

    public CommandHandler() {
        userManager = new UserManager(); 
    }

    public void startApplication() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (true) {
            System.out.println("1. Log In");
            System.out.println("2. Sign Up");
            System.out.print("Enter your choice (1 or 2): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice == 1 || choice == 2) {
                    break;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid input. Please enter 1 for Log In or 2 for Sign Up.");
        }

        if (choice == 1) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            User user = userManager.authenticate(username, password);

            if (user != null) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + user.getUsername());
            } else {
                System.out.println("Invalid username or password.");
            }
        } else if (choice == 2) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            while (!username.matches("[a-zA-Z0-9]+")) {
                System.out.println("Invalid username. Only letters and numbers are allowed.");
                System.out.print("Enter username again: ");
                username = scanner.nextLine();
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            while (!password.matches("[a-zA-Z0-9]+")) {
                System.out.println("Invalid password. Only letters and numbers are allowed.");
                System.out.print("Enter password again: ");
                password = scanner.nextLine();
            }

            System.out.print("Enter role (Buyer/Seller/Agent/Admin): ");
            String role = scanner.nextLine();

            while (!role.equalsIgnoreCase("Buyer") &&
                   !role.equalsIgnoreCase("Seller") &&
                   !role.equalsIgnoreCase("Agent") &&
                   !role.equalsIgnoreCase("Admin")) {
                System.out.println("Invalid role. Please enter one of the following: Buyer, Seller, Agent, Admin.");
                System.out.print("Enter role again: ");
                role = scanner.nextLine();
            }

            if (userManager.registerUser(username, password, role)) {
                System.out.println("User registered successfully!");
                System.out.println("Your email is: " + username + "@email.com");
            } else {
                System.out.println("User already exists.");
            }
        }
    }
}
