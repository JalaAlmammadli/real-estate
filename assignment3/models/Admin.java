package assignment3.models;

import java.util.Scanner;

public class Admin {
    private UserManager userManager;
    private PropertyManager propertyManager;

    public Admin() {
        propertyManager = new PropertyManager();
        userManager = new UserManager();
    }

    public void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Retrieve Accounts");
            System.out.println("2. Get Seller Properties");
            System.out.println("3. View All Properties");
            System.out.println("4. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            if (choice == 1) {
                retrieveAccounts();
            } else if (choice == 2) {
                getSellerProperties(scanner);
            } else if (choice == 3) { // Handle the new option
                viewAllProperties();
            } else if (choice == 4) {
                System.out.println("Logged out successfully.");
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

        // Retrieve all properties from PropertyManager
        for (Property property : propertyManager.getAllProperties().values()) {
            if (property.getSellerEmail() != null && property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                System.out.println(property);
                System.out.println("---------------------------");
                foundProperties = true;
            }
        }

        if (!foundProperties) {
            System.out.println("No properties found for this seller.");
        }
    }
    // Method to display all properties
    private void viewAllProperties() {
        System.out.println("\n--- All Properties ---");
        if (propertyManager.getAllProperties().isEmpty()) {
            System.out.println("No properties found.");
            return;
        }

        for (Property property : propertyManager.getAllProperties().values()) {
            System.out.println(property);
            System.out.println("---------------------------");
        }
    }
}
