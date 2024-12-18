package assignment3;

import java.io.*;
import java.util.*;

public class CommandHandler {
    private UserManager userManager;
    private PropertyManager propertyManager;
    private User currentUser;

    public CommandHandler() {
        userManager = new UserManager();
        propertyManager = new PropertyManager();
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
                scanner.nextLine(); // Consume newline
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
                currentUser = user;
                System.out.println("Login successful!");
                System.out.println("Welcome, " + user.getUsername());
                System.out.println("Debug: User role is " + user.getRole()); // Debugging output

                if (user.getRole().equalsIgnoreCase("Admin")) {
                    showAdminMenu(scanner);
                } else if (user.getRole().equalsIgnoreCase("Agent")) {
                    showAgentMenu(scanner);
                } else if (user.getRole().equalsIgnoreCase("Seller")) {
                    showSellerMenu(scanner);
                } else if (user.getRole().equalsIgnoreCase("Buyer")) {
                    showBuyerMenu(scanner, user.getUsername());
                } else {
                    System.out.println("Access not allowed for your role.");
                }
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

    private void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Retrieve Accounts");
            System.out.println("2. Get Seller Properties");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                retrieveAccounts();
            } else if (choice == 2) {
                getSellerProperties(scanner);
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void retrieveAccounts() {
        System.out.println("\n--- All Users ---");
        for (User user : userManager.getAllUsers().values()) {
            System.out.println(user);
        }
    }

    private void getSellerProperties(Scanner scanner) {
        System.out.print("Enter seller email to retrieve properties: ");
        String sellerEmail = scanner.nextLine().trim();

        System.out.println("\n--- Properties of " + sellerEmail + " ---");

        boolean foundProperties = false;
        for (Property property : propertyManager.getAllProperties().values()) {
            if (property.getSellerEmail().equalsIgnoreCase(sellerEmail.trim())) {
                System.out.println(property);
                System.out.println("---------------------------");
                foundProperties = true;
            }
        }

        if (!foundProperties) {
            System.out.println("No properties found for this seller.");
        }
    }

    private void showAgentMenu(Scanner scanner) {
        System.out.println("Agent menu goes here (Implementation placeholder).");
    }

    private void showSellerMenu(Scanner scanner) {
        System.out.println("Seller menu goes here (Implementation placeholder).");
    }

    private void showBuyerMenu(Scanner scanner, String username) {
        System.out.println("Buyer menu goes here (Implementation placeholder).");
    }
}