package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Seller {
    private PropertyManager propertyManager;
    private User currentUser;

    public Seller() {
        propertyManager = new PropertyManager();
    }

    public void showSellerMenu(Scanner scanner, String sellerEmail) {
        while (true) {
            System.out.println("\n--- Seller Menu ---");
            System.out.println("1. Create Property");
            System.out.println("2. Edit Property");
            System.out.println("3. Archive Property");
            System.out.println("4. View All Properties");
            System.out.println("5. View My Properties");
            System.out.println("6. Mails");
            System.out.println("7. Approve/Deny Requests");
            System.out.println("8. Sign Contract");
            System.out.println("9. Log Out");
            System.out.print("Enter your choice (1, 2, 3, 4, 5, 6, 7, 8 or 9): ");

            if (scanner.hasNextInt()) {
                int sellerChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (sellerChoice == 1) {
                    createProperty(scanner, sellerEmail);
                } else if (sellerChoice == 2) {
                    editProperty(scanner, sellerEmail);
                } else if (sellerChoice == 3) {
                    showArchivedProperties(sellerEmail);
                } else if (sellerChoice == 4) { // New option
                    viewAllPropertiesForSeller();
                } else if (sellerChoice == 5) { // New option
                    viewMyProperties(sellerEmail);
                } else if (sellerChoice == 6) {
                    showMessagesForSellerFromFile(sellerEmail);
                } else if (sellerChoice == 7) {
                    viewAndApproveEditRequests(scanner);
                } else if (sellerChoice == 8) {
                    signContract(scanner, sellerEmail);
                } else if (sellerChoice == 9) {
                    System.out.println("Logged out successfully.");
                    return; // Exits the method, breaking the loop
                } else {
                    System.out.println("Invalid choice. Please enter a valid option.");
                }
            } else {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
            }
        }
    }
    private void createProperty(Scanner scanner, String sellerEmail) {
        // Generate random 8-digit property ID
        String propertyId = generatePropertyId();

        System.out.print("Enter property type (e.g., House, Apartment, etc.): ");
        String type = scanner.nextLine();

        System.out.print("Enter property address: ");
        String address = scanner.nextLine();

        System.out.print("Enter property price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your phone number (e.g., +1234567890): ");
        String phoneNumber = scanner.nextLine();

        while (!phoneNumber.matches("\\+\\d+")) {
            System.out.println("Invalid phone number. Use only numbers and '+'.");
            System.out.print("Enter phone number again: ");
            phoneNumber = scanner.nextLine();
        }

        String status = "Selling"; // Default status

        // Create the property object
        Property property = new Property(propertyId, type, address, price, status, sellerEmail, phoneNumber);

        // Add property to the property manager and save to file
        propertyManager.addProperty(property);
        System.out.println("Property created successfully with ID: " + propertyId);
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

    private void showMessagesForSellerFromFile(String sellerEmail) {
        if (sellerEmail == null || sellerEmail.isEmpty()) {
            System.out.println("Error: Invalid seller email.");
            return;
        }

        System.out.println("Checking messages for: " + sellerEmail);

        boolean hasMessages = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("messages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the CSV row
                if (parts.length == 3) { // Ensure all fields are present
                    String buyerUsername = parts[0];
                    String sellerEmailFromFile = parts[1];
                    String messageContent = parts[2];

                    // Compare emails case-insensitively and trim spaces
                    if (sellerEmailFromFile.equalsIgnoreCase(sellerEmail.trim())) {
                        hasMessages = true;
                        System.out.println("From: " + buyerUsername + " | Message: " + messageContent);
                    }
                }
            }

            if (!hasMessages) {
                System.out.println("No new messages for this seller.");
            }
        } catch (IOException e) {
            System.out.println("Error reading messages file: " + e.getMessage());
        }
    }

    private void showArchivedProperties(String sellerEmail) {
        System.out.println("\n--- Archived Properties ---");
        for (Property property : propertyManager.getAllProperties().values()) {
            if (property.getStatus().equals("Sold") && property.getSellerEmail().equals(sellerEmail)) {
                System.out.println(property);
                System.out.println("---------------------------");
            }
        }
    }
    // Method for the seller to view all properties
    private void viewAllPropertiesForSeller() {
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
    // Method for the seller to view their own properties
    private void viewMyProperties(String sellerEmail) {
        System.out.println("\n--- My Properties ---");
        boolean foundProperties = false;

        for (Property property : propertyManager.getAllProperties().values()) {
            if (property.getSellerEmail() != null && property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                System.out.println(property);
                System.out.println("---------------------------");
                foundProperties = true;
            }
        }

        if (!foundProperties) {
            System.out.println("You do not own any properties.");
        }
    }
    private void viewAndApproveEditRequests(Scanner scanner) {
        String filePath = "permissions.csv";
        List<String[]> requests = new ArrayList<>();
        boolean foundRequest = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("\n--- Pending Requests ---");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split by commas
                if (parts.length == 4 && parts[0].equalsIgnoreCase(currentUser.getEmail()) && parts[3].equalsIgnoreCase("Pending")) {
                    System.out.println("Property ID: " + parts[1] + " | Agent: " + parts[2] + " | Status: " + parts[3]);
                    requests.add(parts); // Store the pending requests
                    foundRequest = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading permissions file: " + e.getMessage());
            return;
        }

        if (!foundRequest) {
            System.out.println("No pending requests for your properties.");
            return;
        }

        System.out.print("Enter the property ID to approve/deny: ");
        String propertyId = scanner.nextLine().trim();
        System.out.print("Enter the agent's email: "); // Ask for agent email
        String agentEmail = scanner.nextLine().trim();
        System.out.print("Approve or Deny (A/D): ");
        String decision = scanner.nextLine().trim();

        boolean requestUpdated = false;
        for (String[] request : requests) {
            if (request[1].equals(propertyId) && request[2].equalsIgnoreCase(agentEmail)) { // Match property ID and agent email
                request[3] = decision.equalsIgnoreCase("A") ? "Approved" : "Denied"; // Update status
                requestUpdated = true;
                break;
            }
        }

        if (!requestUpdated) {
            System.out.println("Property ID and Agent email combination not found in pending requests.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] request : requests) {
                writer.write(String.join(",", request)); // Write updated rows
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving updated permissions: " + e.getMessage());
            return;
        }

        System.out.println("Permission status updated successfully.");
    }
    private void signContract(Scanner scanner, String userEmail) {
        System.out.print("Enter Property ID for contract signing: ");
        String propertyId = scanner.nextLine();

        // Fetch the property by its ID
        Property property = propertyManager.getPropertyById(propertyId);
        if (property == null) {
            System.out.println("Property not found.");
            return;
        }

        // If the current user is a Buyer
        if (currentUser.getRole().equalsIgnoreCase("Buyer")) {
            if (!"Selling".equalsIgnoreCase(property.getStatus())) {
                System.out.println("This property is not available for signing.");
                return;
            }

            if (!"Not Signed".equalsIgnoreCase(property.getContractStatus())) {
                System.out.println("Contract has already been signed or finalized.");
                return;
            }

            property.setContractStatus("Signed by Buyer"); // Update contract status
            propertyManager.saveAllPropertiesToFile(); // Persist changes
            System.out.println("Contract signed successfully as Buyer.");
        }
        // If the current user is a Seller
        else if (currentUser.getRole().equalsIgnoreCase("Seller")) {
            if (!property.getSellerEmail().equalsIgnoreCase(userEmail)) {
                System.out.println("You are not authorized to sign this contract.");
                return;
            }

            if (!"Signed by Buyer".equalsIgnoreCase(property.getContractStatus())) {
                System.out.println("Buyer has not signed the contract yet.");
                return;
            }

            property.setContractStatus("Signed by Both"); // Update contract status
            property.setStatus("Sold"); // Finalize the sale
            propertyManager.saveAllPropertiesToFile(); // Persist changes
            System.out.println("Contract finalized successfully as Seller.");
        }
        // Invalid user role
        else {
            System.out.println("Only buyers and sellers can sign contracts.");
        }
    }
    private String generatePropertyId() {
        Random random = new Random();
        StringBuilder propertyId = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            propertyId.append(random.nextInt(10)); // Generate a random digit
        }
        return propertyId.toString();
    }
}
