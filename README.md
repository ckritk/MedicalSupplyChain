# PharmacySupplyChain

## Overview
The Pharmaceutical Supply Chain Management application is designed to streamline and optimize the interactions between manufacturers, wholesalers, pharmacies, and waste management agencies. It facilitates the efficient management of medicine production, distribution, and waste collection.

## Tech Stack
- **Java:** Programming language used for the application.
- **Oracle Database:** Database system used to store and manage data.

## Prerequisites
- **JDK 8 or higher**
- **Oracle Database**

## Installation

### Database Setup
1. **Run SQL Script:**
   Use the provided SQL script to set up your database:
   ```bash
   mysql -u your_username -p < setup_database.sql
   ```

### Java Project Setup
1. **Update Database Configuration:**
   In `DatabaseHandler.java`, update the URL, username, and password fields:
   ```java
   private static final String url = "jdbc:your_database_url";
   private static final String username = "your_database_username";
   private static final String password = "your_database_password";
   ```

2. **Compile and Run:**
   Navigate to the project root and compile the project:
   ```bash
   mvn clean install
   ```
   Run `Login.java` from your IDE or command line:
   ```bash
   java -cp target/pharmacy-supply-chain.jar Login
   ```

## Running the Application

### Start the Application
1. Ensure your database server is running.
2. Run `Login.java` to start the application.

## Troubleshooting

### Database Connection Issues
- **Verify Database Server:** Ensure that the database server is running and accessible.
- **Check Credentials:** Review `DatabaseHandler.java` to ensure the credentials are correct.

## Contributions
Contributions to the PharmacySupplyChain project are welcome. If you'd like to contribute, please submit a pull request with your changes, and we'll review it for inclusion in the project.

We appreciate your contributions and look forward to collaborating with you!
