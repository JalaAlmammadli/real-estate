package assignment3;

import java.util.Scanner;

public class CommandHandler {
    private UserManager userManager;
    private Seller seller;
    private Buyer buyer;
    private Agent agent;
    private Admin admin;

    public CommandHandler(UserManager userManager, PropertyManager propertyManager) {
        this.userManager = userManager;
        this.seller = new Seller();
        this.buyer = new Buyer();
        this.agent = new Agent();
        this.admin = new Admin();
    }

    // Метод для запуска приложения
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
            handleLogin(scanner);
        } else if (choice == 2) {
            handleSignUp(scanner);
        }
    }

    // Метод для обработки логина
    private void handleLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        User user = userManager.authenticate(username, password);
        
        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getUsername());

            // Переключение меню в зависимости от роли пользователя
            switch (user.getRole().toLowerCase()) {
                case "buyer":
                    buyer.showBuyerMenu(scanner, user.getUsername());
                    break;
                case "seller":
                    seller.showSellerMenu(scanner, user.getEmail());  // Вызов меню продавца
                    break;
                case "agent":
                    agent.showAgentMenu(scanner);
                    break;
                case "admin":
                    admin.showAdminMenu(scanner);
                    break;
                default:
                    System.out.println("Role not recognized.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // Метод для обработки регистрации
    private void handleSignUp(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Проверка корректности имени
        while (!username.matches("[a-zA-Z0-9]+")) {
            System.out.println("Invalid username. Only letters and numbers are allowed.");
            System.out.print("Enter username again: ");
            username = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Проверка корректности пароля
        while (!password.matches("[a-zA-Z0-9]+")) {
            System.out.println("Invalid password. Only letters and numbers are allowed.");
            System.out.print("Enter password again: ");
            password = scanner.nextLine();
        }

        System.out.print("Enter role (Buyer/Seller/Agent/Admin): ");
        String role = scanner.nextLine();

        // Проверка корректности роли
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
