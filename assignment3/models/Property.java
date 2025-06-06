package assignment3.models;

public class Property {
    private String propertyId;
    private String type;
    private String address;
    private double price;
    private String status; // E.g., "Selling", "Sold"
    private String contractStatus = "Not Signed"; // Default status
    private String sellerEmail;
    private String sellerPhoneNumber;
    private boolean contractSigned; // To track if the contract is signed

    // Default constructor
    public Property() {
        this.propertyId = "";
        this.type = "";
        this.address = "";
        this.price = 0.0;
        this.status = "";
        this.sellerEmail = "";
        this.sellerPhoneNumber = "";
    }

    // Constructor with essential parameters
    public Property(String propertyId, String type, String address, double price, String status) {
        this.propertyId = propertyId;
        this.type = type;
        this.address = address;
        this.price = price;
        this.status = status;
        this.sellerEmail = "";
        this.sellerPhoneNumber = "";
    }

    // Constructor with all parameters, including seller's contact information
    public Property(String propertyId, String type, String address, double price, String status, String sellerEmail, String sellerPhoneNumber) {
        this.propertyId = propertyId;
        this.type = type;
        this.address = address;
        this.price = price;
        this.status = status;
        this.sellerEmail = sellerEmail;
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    // Getters and Setters
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerPhoneNumber() {
        return sellerPhoneNumber;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber) {
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    public boolean isContractSigned() {
        return contractSigned;
    }

    public void setContractSigned(boolean contractSigned) {
        this.contractSigned = contractSigned;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }


    // toString method for printing property details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Property ID: ").append(propertyId)
                .append("\nType: ").append(type)
                .append("\nAddress: ").append(address)
                .append("\nPrice: ").append(price)
                .append("\nStatus: ").append(status)
                .append("\nContract Status: ").append(contractStatus);

        if (sellerEmail != null && !sellerEmail.isEmpty()) {
            sb.append("\nSeller Email: ").append(sellerEmail);
        }
        if (sellerPhoneNumber != null && !sellerPhoneNumber.isEmpty()) {
            sb.append("\nSeller Phone: ").append(sellerPhoneNumber);
        }
        return sb.toString();
    }

    private String permissionStatus; // Field to store the permission status

    // Getter for permissionStatus
    public String getPermissionStatus() {
        return permissionStatus;
    }

    // Setter for permissionStatus
    public void setPermissionStatus(String permissionStatus) {
        this.permissionStatus = permissionStatus;
    }
}
