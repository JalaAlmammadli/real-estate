package assignment3.models;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PropertyManager {
    private Map<String, Property> properties;

    // Constructor
    public PropertyManager() {
        properties = new HashMap<>();
        loadPropertiesFromFile(); // Load properties from file when the manager is initialized
    }

    // Add a new property and save it to the file
    public void addProperty(Property property) {
        properties.put(property.getPropertyId(), property);
        savePropertyToFile(property); // Save this specific property to the file
    }

    // Get a property by its ID
    public Property getPropertyById(String propertyId) {
        return properties.get(propertyId);
    }

    // Get all properties
    public Map<String, Property> getAllProperties() {
        return properties;
    }

    // Get archived properties (e.g., "Sold", "Archived")
    public Map<String, Property> getArchivedProperties() {
        Map<String, Property> archivedProperties = new HashMap<>();
        for (Map.Entry<String, Property> entry : properties.entrySet()) {
            if ("Archived".equalsIgnoreCase(entry.getValue().getStatus())) {
                archivedProperties.put(entry.getKey(), entry.getValue());
            }
        }
        return archivedProperties;
    }

    // Method to save property data to the file
    private void savePropertyToFile(Property property) {
        String filePath = "PropertyCollection.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write property data as a CSV row
            writer.write(String.join(",",
                    property.getPropertyId(),
                    property.getType(),
                    property.getAddress(),
                    String.valueOf(property.getPrice()),
                    property.getStatus(),
                    property.getSellerEmail(),
                    property.getSellerPhoneNumber(),
                    property.getContractStatus()));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the property to the file.");
            e.printStackTrace();
        }
    }

    private void loadPropertiesFromFile() {
        String filePath = "PropertyCollection.csv";
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(","); // Split by commas for CSV
                    if (parts.length == 8) { // Ensure all fields are present
                        String propertyId = parts[0].trim();
                        String type = parts[1].trim();
                        String address = parts[2].trim(); // Full address is a single field
                        double price = Double.parseDouble(parts[3].trim());
                        String status = parts[4].trim();
                        String sellerEmail = parts[5].trim();
                        String sellerPhoneNumber = parts[6].trim();
                        String contractStatus = parts[7].trim();

                        Property property = new Property(propertyId, type, address, price, status, sellerEmail, sellerPhoneNumber);
                        property.setContractStatus(contractStatus); // Set contract status
                        properties.put(propertyId, property); // Store in the map
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading the properties file: " + e.getMessage());
            }
        }
    }

    // Update the method to overwrite the file whenever properties are changed
    public void saveAllPropertiesToFile() {
        String filePath = "PropertyCollection.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Property property : properties.values()) {
                // Write property data as a CSV row
                writer.write(String.join(",",
                        property.getPropertyId(),
                        property.getType(),
                        property.getAddress(),
                        String.valueOf(property.getPrice()),
                        property.getStatus(),
                        property.getSellerEmail(),
                        property.getSellerPhoneNumber(),
                        property.getContractStatus()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving properties to the file.");
            e.printStackTrace();
        }
    }

    public Property getPropertyBySellerEmail(String sellerEmail) {
        for (Property property : properties.values()) {
            if (property.getSellerEmail() != null && property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                return property;  // Return the property if the seller's email matches
            }
        }
        return null;  // Return null if no property found with the given seller's email
    }

    // New Method to Check Permission for a Property ID
    public boolean hasPermissionForProperty(String sellerEmail, String propertyId, String agentEmail) {
        String filePath = "permissions.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split CSV row
                if (parts.length == 4 && parts[0].equalsIgnoreCase(sellerEmail) &&
                        parts[1].equalsIgnoreCase(propertyId) &&
                        parts[2].equalsIgnoreCase(agentEmail) &&
                        parts[3].equalsIgnoreCase("Approved")) {
                    return true;  // Permission found and approved
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading permissions file: " + e.getMessage());
        }
        return false;  // Default to no permission
    }
}
