package assignment3.models;

public class RealEstateApp {
    public static void main(String[] args) {
        String divider = "====================================================================================";

        System.out.println(divider);
        System.out.println("                Welcome to the PARALLAX Real Estate Application");
        System.out.println(divider);
        System.out.println("Your one stop solution for buying and selling properties!");
        System.out.println("Please log in to access your account.\n");

        UserManager userManager = new UserManager();
        PropertyManager propertyManager = new PropertyManager();

        CommandHandler commandHandler = new CommandHandler(userManager, propertyManager);
        commandHandler.startApplication();
    }
}

