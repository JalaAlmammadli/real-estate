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
        String filePath = "PropertyCollection.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Property ID: " + property.getPropertyId());
            writer.write("\nType: " + property.getType());
            writer.write("\nAddress: " + property.getAddress());
            writer.write("\nPrice: " + property.getPrice());
            writer.write("\nStatus: " + property.getStatus());
            writer.write("\nSeller Email: " + property.getSellerEmail());
            writer.write("\nSeller Phone: " + property.getSellerPhoneNumber());
            writer.write("\n-----------------------------\n"); // Separator between properties
        } catch (IOException e) {
            System.out.println("An error occurred while saving the property to the file.");
            e.printStackTrace();
        }
    }

    // Method to load all properties from the file
    private void loadPropertiesFromFile() {
        String filePath = "PropertyCollection.txt";
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Property property = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Property ID: ")) {
                        // Save the previous property if exists
                        if (property != null) {
                            properties.put(property.getPropertyId(), property);
                        }
                        // Create a new property for the next set of data
                        String propertyId = line.substring("Property ID: ".length());
                        property = new Property(propertyId, "", "", 0.0, "", "", ""); // Initialize property with ID
                    } else if (line.startsWith("Type: ")) {
                        property.setType(line.substring("Type: ".length()));
                    } else if (line.startsWith("Address: ")) {
                        property.setAddress(line.substring("Address: ".length()));
                    } else if (line.startsWith("Price: ")) {
                        property.setPrice(Double.parseDouble(line.substring("Price: ".length())));
                    } else if (line.startsWith("Status: ")) {
                        property.setStatus(line.substring("Status: ".length()));
                    } else if (line.startsWith("Seller Email: ")) {
                        property.setSellerEmail(line.substring("Seller Email: ".length()));
                    } else if (line.startsWith("Seller Phone: ")) {
                        property.setSellerPhoneNumber(line.substring("Seller Phone: ".length()));
                    }
                }
                // Add the last property if exists
                if (property != null) {
                    properties.put(property.getPropertyId(), property);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the properties from the file.");
                e.printStackTrace();
            }
        }
    }

    // Update the method to overwrite the file whenever properties are changed
    public void saveAllPropertiesToFile() {
        String filePath = "PropertyCollection.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {  // Overwrite the file each time
            for (Property property : properties.values()) {
                writer.write("Property ID: " + property.getPropertyId());
                writer.write("\nType: " + property.getType());
                writer.write("\nAddress: " + property.getAddress());
                writer.write("\nPrice: " + property.getPrice());
                writer.write("\nStatus: " + property.getStatus());
                writer.write("\nSeller Email: " + property.getSellerEmail());
                writer.write("\nSeller Phone: " + property.getSellerPhoneNumber());
                writer.write("\n-----------------------------\n");
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
