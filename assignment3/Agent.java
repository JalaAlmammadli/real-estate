package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Agent {
    private PropertyManager propertyManager;

    public Agent() {
        propertyManager = new PropertyManager();
    }

    public void showAgentMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Agent Menu ---");
            System.out.println("1. Request Property Edit");
            System.out.println("2. Edit Properties");
            System.out.println("3. Help with Contract");
            System.out.println("4. View All Properties");
            System.out.println("5. View Managed Properties");
            System.out.println("6. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                requestPropertyEdit(scanner);
            } else if (choice == 2) {
                System.out.print("Enter your email for validation: ");
                String agentEmail = scanner.nextLine();
                editProperty(scanner, agentEmail);
            } else if (choice == 3) {
                prepareContract(scanner);
            } else if (choice == 4) {
                viewAllPropertiesForAgent();
            } else if (choice == 5) { // New option
                System.out.print("Enter your email to view managed properties: ");
                String agentEmail = scanner.nextLine();
                viewManagedProperties(agentEmail);
            } else if (choice == 6) {
                System.out.println("Logged out successfully.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void requestPropertyEdit(Scanner scanner) {
        System.out.print("Enter your email: "); // Ask agent for email
        String agentEmail = scanner.nextLine().trim();

        System.out.print("Enter property ID to edit: ");
        String propertyId = scanner.nextLine().trim();
        Property property = propertyManager.getPropertyById(propertyId);

        if (property == null) {
            System.out.println("Property not found.");
            return;
        }

        String sellerEmail = property.getSellerEmail();
        if (sellerEmail == null || sellerEmail.isEmpty()) {
            System.out.println("This property has no assigned seller.");
            return;
        }

        // Pass agentEmail as the third argument
        savePermissionRequest(sellerEmail, propertyId, agentEmail);
        System.out.println("Edit request sent to the seller: " + sellerEmail);
    }

    private void savePermissionRequest(String sellerEmail, String propertyId, String agentEmail) {
        String filePath = "permissions.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write seller email, property ID, agent email, and status ("Pending") to the file
            writer.write(String.join(",", sellerEmail, propertyId, agentEmail, "Pending"));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving permission request: " + e.getMessage());
        }
    }

    // agent helping with contract
    private void prepareContract(Scanner scanner) {
        System.out.print("Enter Property ID to assist with contract preparation: ");
        String propertyId = scanner.nextLine();
        Property property = propertyManager.getPropertyById(propertyId);
        if (property != null) {
            System.out.println("Preparing contract for property: " + propertyId);
            System.out.println("Property Type: " + property.getType());
            System.out.println("Property Address: " + property.getAddress());
            System.out.println("Property Price: " + property.getPrice());
            // Simulate contract preparation process
            System.out.println("Contract prepared successfully for property: " + propertyId);
        } else {
            System.out.println("Property not found with ID: " + propertyId);
        }
    }
    // Method to display all properties for the agent
    private void viewAllPropertiesForAgent() {
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
    // Method to display all properties an agent has permission to edit
    private void viewManagedProperties(String agentEmail) {
        System.out.println("\n--- Managed Properties ---");
        boolean foundProperties = false;

        String filePath = "permissions.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split CSV row
                if (parts.length == 4 && parts[2].equalsIgnoreCase(agentEmail) && parts[3].equalsIgnoreCase("Approved")) {
                    // Fetch property by property ID
                    Property property = propertyManager.getPropertyById(parts[1]);
                    if (property != null) {
                        System.out.println(property);
                        System.out.println("---------------------------");
                        foundProperties = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading permissions file: " + e.getMessage());
        }

        if (!foundProperties) {
            System.out.println("You do not have permission to edit any properties.");
        }
    }

    public void getPermissionStatus(Scanner scanner) {
        System.out.print("Enter property ID to check permission status: ");
        String propertyId = scanner.nextLine();

        Property property = propertyManager.getPropertyById(propertyId);
        if (property != null) {
            String status = property.getPermissionStatus();
            if (status == null || status.isEmpty()) {
                System.out.println("Permission status: Not set (default status).");
            } else {
                System.out.println("Permission status: " + status);
            }
        } else {
            System.out.println("Property not found with ID: " + propertyId);
        }
    }
    private void editProperty(Scanner scanner, String agentEmail) {
        System.out.print("Enter property ID to edit: ");
        String propertyId = scanner.nextLine();

        // Fetch the property
        Property property = propertyManager.getPropertyById(propertyId);
        if (property == null) {
            System.out.println("Property not found with ID: " + propertyId);
            return;
        }

        // Check if the agent has permission
        if (propertyManager.hasPermissionForProperty(property.getSellerEmail(), propertyId, agentEmail)) {
            System.out.println("Editing Property: " + propertyId);
            System.out.println("What would you like to edit?");
            System.out.println("1. Type");
            System.out.println("2. Address");
            System.out.println("3. Price");
            System.out.println("4. Seller Phone");
            System.out.println("5. Status (e.g., change from Selling to Sold)");

            System.out.print("Enter your choice (e.g., 1,2 to change both Type and Address): ");
            String choices = scanner.nextLine();

            if (choices.contains("1")) {
                System.out.print("Enter new type (current: " + property.getType() + "): ");
                String newType = scanner.nextLine();
                property.setType(newType);
            }

            if (choices.contains("2")) {
                System.out.print("Enter new address (current: " + property.getAddress() + "): ");
                String newAddress = scanner.nextLine();
                property.setAddress(newAddress);
            }

            if (choices.contains("3")) {
                System.out.print("Enter new price (current: " + property.getPrice() + "): ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline
                property.setPrice(newPrice);
            }

            if (choices.contains("4")) {
                System.out.print("Enter new seller phone number (current: " + property.getSellerPhoneNumber() + "): ");
                String newPhoneNumber = scanner.nextLine();
                property.setSellerPhoneNumber(newPhoneNumber);
            }

            if (choices.contains("5")) {
                System.out.print("Enter new status (current: " + property.getStatus() + "): ");
                String newStatus = scanner.nextLine();
                property.setStatus(newStatus);
            }

            System.out.println("Property updated successfully!");
            propertyManager.saveAllPropertiesToFile();
        } else {
            System.out.println("Permission to edit this property has not been granted.");
        }
    }
}
