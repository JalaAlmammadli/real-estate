[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/cU5K5-bZ)
# csci3509-2024
Skeleton/template code for students of CSCI3509 to develop on.

**Design Rationale**

The Real Estate Application's class diagram shows use of object-oriented design concepts and practices. It shows why specific design decisions were taken, including principles such as abstraction, encapsulation, inheritance, and polymorphism, also their compatibility with design patterns. 

**Main Design Decisions:**

**1. Using Inheritance for User Roles:**

The roles Admin, Seller, Buyer, and Agent are derived from the basic class User. This inheritance tree represents the shared behaviors and attributes of all users, such as username, password, email, and role.
This method is compatible with the Liskov Substitution Principle (LSP), which asserts that objects in a superclass should be interchangeable with objects in its subclasses without changing the program. By using this principle in User class, we avoided duplicating code and we simplified role-specific classes by focusing on just on their distinctive behaviors. Also, this principle supports extensibility, so that adding extra roles would be simple to do.
Disadvantages:Introduces tight connection between subclasses and the base class, which may need careful reworking if the User structure changes dramatically.

**2. Aggregation in PropertyManager:**

PropertyManager aggregates Property objects, managing the creation, retrieval, and storage of properties. Aggregation allows a PropertyManager to "own" multiple Property objects without implying lifecycle dependency (i.e., properties can exist independently). This design aligns with the Single Responsibility Principle (SRP), as PropertyManager is only responsible for managing property-related logic, while Property handles individual property details. In this way, it's reusable across different contexts, and it enhances cohesion, which means PropertyManager clearly encapsulates logic for property storage and retrieval. However, it requires handling relationships explicitly if properties are dynamically added or removed.

**3. CommandHandler as a Mediator Decision:**

The CommandHandler class mediates interactions between users (Admin, Buyer, Seller, and Agent) and managers (UserManager, PropertyManager). This design follows the Mediator Pattern, as it reduces direct dependencies between classes. By using this design, we ensure that role-based classes remain focused on their specific logic. We also avoid cyclic dependencies where CommandHandler coordinates interactions without roles interacting directly. The main advantage of this design is it simplified maintenance, as modifying user flows requires adjusting only CommandHandler. Also, roles and managers operate independently of each other. However, it may slightly increase complexity, as CommandHandler must understand the interactions it mediates.

**4. Multiplicity and Relationships**

1 Seller ↔ * Properties, which means a seller can manage multiple properties.
1 Agent ↔ * Properties, meaning agent can manage multiple properties (but through editing permissions).
1 Buyer ↔ * Properties: A buyer can view and purchase multiple properties.
Here, Dependency Inversion Principle (DIP) is used at decoupling high-level modules (e.g., CommandHandler) from low-level modules (e.g., Property), and navigability arrows shows clear flow of data and interactions. This design can capture domain-specific relationships accurately. It also supports extensibility, by integrating new roles or property types without disrupting existing associations. But, it requires careful documentation for making sure relationships are clearly understood by developers.

**5. Encapsulation of User and Property Data:**

All attributes in User and Property are private, and have public getters and setters.  Here, encapsulation is used for hiding internal details of classes, protecting object states from unintended changes. This design provides better security by controlling access to data, and makes the system easier as it limits the internal changes. But for achieving this, we need additional code for getters and setters.

**6. Use of File-Based Storage:**

UserManager and PropertyManager use file-based storage to save user and property data.
File storage is a lightweight and straightforward solution for this system, and it follows the KISS Principle (Keep It Simple, Stupid). It doesn't create unnecessary complexity introduced by database management systems for small-scale projects. Therefore, it's simple, and easy to implement and debug. Alsp, the system remains operational without complex dependencies.
However, there is scalability drawback because, as the application grows, file-based storage may become inefficient compared to databases.

**7. Extensibility Through Interfaces and Patterns:**

This design allows future extensions without changing existing classes, as it used inheritance for role-specific behaviors, so new roles can easily be added. Also, it makes managers (UserManager, PropertyManager) replaceable with database-backed versions.
This follows the Open-Closed Principle (OCP), which means the system can grow by adding new features without breaking existing functionalities. This keeps the system adaptable to new requirements with minimal changes and makes sure new features integrates smoothly into the existing design. But, we may need slightly more planning upfront for making the system prepared for future changes.

**Citations**

1) Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). Design Patterns: Elements of Reusable Object-Oriented Software. Addison-Wesley.
2) Martin, R. C. (2002). Agile Software Development: Principles, Patterns, and Practices. Pearson.
3) Beck, K. (1999). Extreme Programming Explained: Embrace Change. Addison-Wesley.



Fariz video: https://adauniversity-my.sharepoint.com/personal/fibrahimov19866_ada_edu_az/_layouts/15/stream.aspx?id=%2Fpersonal%2Ffibrahimov19866%5Fada%5Fedu%5Faz%2FDocuments%2Fbandicam%202024%2D12%2D22%2012%2D34%2D39%2D127%2Emp4&referrer=StreamWebApp%2EWeb&referrerScenario=AddressBarCopied%2Eview%2E58b83c44%2D9894%2D4bbb%2Db471%2D8d95b8169367&isDarkMode=true


[AI DECLARATION (1) (1) 2.docx.zip](https://github.com/user-attachments/files/18223014/AI.DECLARATION.1.1.2.docx.zip)

