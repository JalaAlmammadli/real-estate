package assignment3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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

                // Role-based menu
                if (user.getRole().equalsIgnoreCase("Seller")) {
                    showSellerMenu(scanner, user.getEmail());
                } else if (user.getRole().equalsIgnoreCase("Buyer")) {
                    showBuyerMenu(scanner, user.getUsername());
                } else if (user.getRole().equalsIgnoreCase("Agent")) {
                    showAgentMenu(scanner);  // Show agent menu
                } else if (user.getRole().equalsIgnoreCase("Admin")) {
                    showAdminMenu(scanner);  // Show admin menu
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
            System.out.println("3. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            if (choice == 1) {
                retrieveAccounts();
            } else if (choice == 2) {
                getSellerProperties(scanner);
            } else if (choice == 3) {
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

    private void showAgentMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Agent Menu ---");
            System.out.println("1. Request Property Edit");
            System.out.println("2. Edit Properties");
            System.out.println("3. Help with Contract");
            System.out.println("4. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                requestPropertyEdit(scanner);
            } else if (choice == 2) {
                System.out.print("Enter your email for validation: ");
                String sellerEmail = scanner.nextLine();
                editProperty(scanner, sellerEmail); // Use CommandHandler's editProperty
            } else if (choice == 3) {
                prepareContract(scanner);
            } else if (choice == 4) {
                System.out.println("Logged out successfully.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showSellerMenu(Scanner scanner, String sellerEmail) {
        while (true) {
            System.out.println("\n--- Seller Menu ---");
            System.out.println("1. Create Property");
            System.out.println("2. Edit Property");
            System.out.println("3. Archive Property");
            System.out.println("4. Mails");
            System.out.println("5. Approve/Deny Requests");
            System.out.println("6. Sign Contract");
            System.out.println("7. Log Out");
            System.out.print("Enter your choice (1, 2, 3, 4, 5, 6, or 7): ");

            if (scanner.hasNextInt()) {
                int sellerChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (sellerChoice == 1) {
                    createProperty(scanner, sellerEmail);
                } else if (sellerChoice == 2) {
                    editProperty(scanner, sellerEmail);
                } else if (sellerChoice == 3) {
                    showArchivedProperties(sellerEmail);
                } else if (sellerChoice == 4) {
                    showMessagesForSellerFromFile(sellerEmail);
                } else if (sellerChoice == 5) {
                    viewAndApproveEditRequests(scanner);
                } else if (sellerChoice == 6) {
                    signContract(scanner, sellerEmail);
                } else if (sellerChoice == 7) {
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

    private void showBuyerMenu(Scanner scanner, String buyerUsername) {
        while (true) {
            System.out.println("\n--- Buyer Menu ---");
            System.out.println("1. Search Properties");
            System.out.println("2. Contact Seller/Agent");
            System.out.println("3. Make Payment");
            System.out.println("4. Sign Contract");
            System.out.println("5. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            if (choice == 1) {
                searchProperties(scanner);
            } else if (choice == 2) {
                contactSellerOrAgent(scanner, buyerUsername);
            } else if (choice == 3) {
                makePayment(scanner, buyerUsername);
            } else if (choice == 4) {
                signContract(scanner, buyerUsername);
            } else if (choice == 5) {
                System.out.println("Logged out successfully.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void makePayment(Scanner scanner, String buyerUsername) {
        System.out.print("Enter property ID to buy: ");
        String propertyId = scanner.nextLine();

        // Find the property by ID
        Property property = propertyManager.getPropertyById(propertyId);
        if (property != null) {
            System.out.println("You have selected the following property:");
            System.out.println(property);

            System.out.print("Enter your card number: ");
            String cardNumber = scanner.nextLine();
            System.out.println("Card number " + cardNumber + " accepted (no validation).");

            System.out.print("Enter 'yes' to proceed with the payment, or 'no' to cancel: ");
            String proceed = scanner.nextLine();

            if (proceed.equalsIgnoreCase("yes")) {
                System.out.println("Payment for property ID " + propertyId + " has been successfully processed.");

                System.out.println("Notification sent to the seller for property ID " + propertyId);
                property.setStatus("Sold");
                propertyManager.saveAllPropertiesToFile();
                System.out.println("Property status updated to 'Sold'.");
            } else {
                System.out.println("Payment process cancelled.");
            }
        } else {
            System.out.println("Property ID not found.");
        }
    }


    private void contactSellerOrAgent(Scanner scanner, String buyerUsername) {
        System.out.print("Enter seller email to contact (e.g., Sellerman@email.com): ");
        String sellerEmail = scanner.nextLine();

        User seller = userManager.getUserByEmail(sellerEmail);

        if (seller != null) {
            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();

            saveMessageToFile(buyerUsername, seller, messageContent);

            System.out.println("Message sent to seller at: " + sellerEmail);
        } else {
            System.out.println("Seller with email " + sellerEmail + " not found.");
        }
    }

    private void saveMessageToFile(String buyerUsername, User seller, String messageContent) {
        String filePath = "messages.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write message data as a CSV row
            writer.write(String.join(",",
                    buyerUsername,
                    seller.getEmail(),
                    messageContent));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving message to file: " + e.getMessage());
        }
    }

    private void searchProperties(Scanner scanner) {
        System.out.print("Enter property type (or type 'all' for all types): ");
        String type = scanner.nextLine();

        System.out.print("Enter a part of the address (optional): ");
        String addressPart = scanner.nextLine();

        System.out.print("Enter status (Selling, Sold, or all): ");
        String status = scanner.nextLine().trim();

        System.out.println("\n--- Search Results ---");
        boolean foundAny = false; // Track matches

        for (Property property : propertyManager.getAllProperties().values()) {
            boolean matches = true;

            if (!type.equalsIgnoreCase("all") && !property.getType().equalsIgnoreCase(type)) {
                matches = false;
            }

            if (!addressPart.isEmpty() && !property.getAddress().toLowerCase().contains(addressPart.toLowerCase())) {
                matches = false;
            }

            if (!status.equalsIgnoreCase("all") && !property.getStatus().equalsIgnoreCase(status)) {
                matches = false;
            }

            if (matches) {
                System.out.println(property);
                System.out.println("---------------------------");
                foundAny = true; // Mark as found
            }
        }

        if (!foundAny) {
            System.out.println("No properties found with this criteria."); // Print message for no matches
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
}
