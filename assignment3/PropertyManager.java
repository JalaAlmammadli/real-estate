package assignment3;

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
                    String[] parts = line.split(","); // Split the CSV row
                    if (parts.length == 8) { // Ensure all fields are present
                        Property property = new Property(
                                parts[0], // propertyId
                                parts[1], // type
                                parts[2], // address
                                Double.parseDouble(parts[3]), // price
                                parts[4], // status
                                parts[5], // sellerEmail
                                parts[6]); // sellerPhoneNumber
                        property.setContractStatus(parts[7]); // contractStatus
                        properties.put(property.getPropertyId(), property);
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the properties from the file.");
                e.printStackTrace();
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
            if (property.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                return property;  // Return the property if the seller's email matches
            }
        }
        return null;  // Return null if no property found with the given seller's email
    }
}
