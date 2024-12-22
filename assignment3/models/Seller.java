package assignment3.models;

import java.io.*;
import java.util.*;

public class Seller {
    private PropertyManager propertyManager;
    private UserManager userManager;
    private User currentUser;

    public Seller() {
        propertyManager = new PropertyManager();
        userManager = new UserManager();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void showSellerMenu(Scanner scanner, String sellerEmail) {
        this.currentUser = userManager.getUserByEmail(sellerEmail);
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
            System.out.print("Enter your choice (1-9): ");

            if (scanner.hasNextInt()) {
                int sellerChoice = scanner.nextInt();
                scanner.nextLine();

                switch (sellerChoice) {
                    case 1 -> createProperty(scanner, sellerEmail);
                    case 2 -> editProperty(scanner, sellerEmail);
                    case 3 -> showArchivedProperties(sellerEmail);
                    case 4 -> viewAllPropertiesForSeller();
                    case 5 -> viewMyProperties(sellerEmail);
                    case 6 -> showMessagesForSellerFromFile(sellerEmail);
                    case 7 -> viewAndApproveEditRequests(scanner);
                    case 8 -> signContract(scanner, sellerEmail);
                    case 9 -> {
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> System.out.println("Invalid input. Please enter a number between 1 and 9.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
            }
        }
    }

    private void createProperty(Scanner scanner, String sellerEmail) {
        String propertyId = generatePropertyId();
        System.out.print("Enter property type (e.g., House, Apartment, etc.): ");
        String type = scanner.nextLine();

        System.out.print("Enter property address: ");
        String address = scanner.nextLine();

        System.out.print("Enter property price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter your phone number (e.g., +1234567890): ");
        String phoneNumber = scanner.nextLine();

        while (!phoneNumber.matches("\\+\\d+")) {
            System.out.println("Invalid phone number. Use only numbers and '+'.");
            System.out.print("Enter phone number again: ");
            phoneNumber = scanner.nextLine();
        }

        Property property = new Property(propertyId, type, address, price, "Selling", sellerEmail, phoneNumber);
        propertyManager.addProperty(property);
        System.out.println("Property created successfully with ID: " + propertyId);
    }

    private void editProperty(Scanner scanner, String sellerEmail) {
        System.out.print("Enter property ID to edit: ");
        String propertyId = scanner.nextLine();

        Property property = propertyManager.getPropertyById(propertyId);
        if (property == null) {
            System.out.println("Property not found with ID: " + propertyId);
            return;
        }

        if (property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
            System.out.println("Editing Property: " + propertyId);
            System.out.println("1. Type");
            System.out.println("2. Address");
            System.out.println("3. Price");
            System.out.println("4. Seller Phone");
            System.out.println("5. Status");

            System.out.print("Enter your choice (e.g., 1,2 to change both Type and Address): ");
            String choices = scanner.nextLine();

            if (choices.contains("1")) {
                System.out.print("Enter new type (current: " + property.getType() + "): ");
                property.setType(scanner.nextLine());
            }
            if (choices.contains("2")) {
                System.out.print("Enter new address (current: " + property.getAddress() + "): ");
                property.setAddress(scanner.nextLine());
            }
            if (choices.contains("3")) {
                System.out.print("Enter new price (current: " + property.getPrice() + "): ");
                property.setPrice(scanner.nextDouble());
                scanner.nextLine();
            }
            if (choices.contains("4")) {
                System.out.print("Enter new seller phone (current: " + property.getSellerPhoneNumber() + "): ");
                property.setSellerPhoneNumber(scanner.nextLine());
            }
            if (choices.contains("5")) {
                System.out.print("Enter new status (current: " + property.getStatus() + "): ");
                property.setStatus(scanner.nextLine());
            }

            propertyManager.saveAllPropertiesToFile();
            System.out.println("Property updated successfully.");
        } else {
            System.out.println("You are not authorized to edit this property.");
        }
    }

    private void showArchivedProperties(String sellerEmail) {
        System.out.println("\n--- Archived Properties ---");
        boolean foundProperties = false;

        for (Property property : propertyManager.getAllProperties().values()) {
            if (property.getStatus().equalsIgnoreCase("Sold") &&
                    property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                System.out.println(property);
                System.out.println("---------------------------");
                foundProperties = true;
            }
        }

        if (!foundProperties) {
            System.out.println("No archived properties found.");
        }
    }


    private void viewAllPropertiesForSeller() {
        for (Property property : propertyManager.getAllProperties().values()) {
            System.out.println(property);
        }
    }

    private void viewMyProperties(String sellerEmail) {
        for (Property property : propertyManager.getAllProperties().values()) {
            if (sellerEmail.equalsIgnoreCase(property.getSellerEmail())) {
                System.out.println(property);
            }
        }
    }

    private void showMessagesForSellerFromFile(String sellerEmail) {
        if (sellerEmail == null || sellerEmail.isEmpty()) {
            System.out.println("Error: Invalid seller email.");
            return;
        }

        System.out.println("\n--- Messages for Seller: " + sellerEmail + " ---");
        boolean hasMessages = false;
        String filePath = "messages.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the CSV row
                if (parts.length == 3) { // Ensure all fields are present
                    String buyerUsername = parts[0].trim();
                    String sellerEmailFromFile = parts[1].trim();
                    String messageContent = parts[2].trim();

                    // Compare emails case-insensitively and trim spaces
                    if (sellerEmailFromFile.equalsIgnoreCase(sellerEmail.trim())) {
                        hasMessages = true;
                        System.out.println("From: " + buyerUsername + " | Message: " + messageContent);
                    }
                } else {
                    System.out.println("Warning: Malformed message entry in file: " + line);
                }
            }

            if (!hasMessages) {
                System.out.println("No messages found for this seller.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Messages file not found at " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading messages file: " + e.getMessage());
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
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equalsIgnoreCase(currentUser.getEmail()) && parts[3].equalsIgnoreCase("Pending")) {
                    System.out.println("Property ID: " + parts[1] + " | Agent: " + parts[2] + " | Status: " + parts[3]);
                    requests.add(parts);
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
        System.out.print("Enter the agent's email: ");
        String agentEmail = scanner.nextLine().trim();
        System.out.print("Approve or Deny (A/D): ");
        String decision = scanner.nextLine().trim();

        boolean requestUpdated = false;
        for (String[] request : requests) {
            if (request[1].equals(propertyId) && request[2].equalsIgnoreCase(agentEmail)) {
                request[3] = decision.equalsIgnoreCase("A") ? "Approved" : "Denied";
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
                writer.write(String.join(",", request));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving updated permissions: " + e.getMessage());
            return;
        }

        System.out.println("Permission status updated successfully.");
    }

    private void signContract(Scanner scanner, String sellerEmail) {
        System.out.print("Enter Property ID for contract signing: ");
        String propertyId = scanner.nextLine();

        Property property = propertyManager.getPropertyById(propertyId);
        if (property == null) {
            System.out.println("Property not found.");
            return;
        }

        if (sellerEmail.equalsIgnoreCase(property.getSellerEmail())) {
            if ("Signed by Buyer".equalsIgnoreCase(property.getContractStatus())) {
                property.setContractStatus("Signed by Both");
                property.setStatus("Sold");
                propertyManager.saveAllPropertiesToFile();
                System.out.println("Contract signed successfully.");
            } else {
                System.out.println("Buyer has not signed the contract yet.");
            }
        } else {
            System.out.println("You are not authorized to sign this contract.");
        }
    }

    private String generatePropertyId() {
        Random random = new Random();
        StringBuilder propertyId = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            propertyId.append(random.nextInt(10));
        }
        return propertyId.toString();
    }


}
