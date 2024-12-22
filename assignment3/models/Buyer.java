package assignment3.models;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * @author najam
 * Pojo class
 * Class definition should be added here
 *
 */

public class Buyer {
    private UserManager userManager;
    private PropertyManager propertyManager;
    private User currentUser;

    public Buyer() {
        propertyManager = new PropertyManager();
        userManager = new UserManager();

    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void showBuyerMenu(Scanner scanner, String buyerUsername) {
        while (true) {
            System.out.println("\n--- Buyer Menu ---");
            System.out.println("1. Search Properties");
            System.out.println("2. View All Properties");
            System.out.println("3. Contact Seller/Agent");
            System.out.println("4. Make Payment");
            System.out.println("5. Sign Contract");
            System.out.println("6. Log Out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                searchProperties(scanner);
            } else if (choice == 2) {
                viewAllPropertiesForBuyer();
            } else if (choice == 3) {
                contactSellerOrAgent(scanner, buyerUsername);
            } else if (choice == 4) {
                makePayment(scanner, buyerUsername);
            } else if (choice == 5) {
                signContract(scanner, buyerUsername);
            } else if (choice == 6) {
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
            // Check if the contract status is "Signed by Both"
            if (!"Signed by Both".equalsIgnoreCase(property.getContractStatus())) {
                System.out.println("Payment cannot proceed. The contract is not signed by both Buyer and Seller.");
                return;
            }

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

    // Method to display all properties for the buyer
    private void viewAllPropertiesForBuyer() {
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
    private void signContract(Scanner scanner, String userEmail) {
        System.out.print("Enter Property ID for contract signing: ");
        String propertyId = scanner.nextLine();

        Property property = propertyManager.getPropertyById(propertyId);
        if (property == null) {
            System.out.println("Property not found.");
            return;
        }

        if ("Selling".equalsIgnoreCase(property.getStatus()) && "Not Signed".equalsIgnoreCase(property.getContractStatus())) {
            property.setContractStatus("Signed by Buyer");
            propertyManager.saveAllPropertiesToFile();
            System.out.println("Contract signed successfully as Buyer.");
        } else {
            System.out.println("The property is either unavailable or the contract is already signed.");
        }
    }
}
