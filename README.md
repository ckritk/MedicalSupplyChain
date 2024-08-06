# PharmacySupplyChain
## Overview
The pharmaceutical supply chain management application is designed to streamline and
optimize the interactions between manufacturers, wholesalers, pharmacies, and waste
management agencies. It facilitates the efficient management of medicine production,
distribution, and waste collection.
## Prerequisites
  ● JDK 8 or higher

  ● Oracle
## Setup Instructions
### Database Setup
#### 1. Run SQL Script:
Use the provided SQL script to set up your database.
### Java Project Setup
#### 2. Update Database Configuration:
In _DatabaseHandler.java_, update the url, username, and password fields:

_private static final String url = "jdbc:your_database_url";

private static final String username = "your_database_username";

private static final String password = "your_database_password";_
#### 3. Compile and Run:
Navigate to the project root and compile:

_mvn clean install_

  ○ Run _Login.java_ from your IDE or command line.
## Running the Application
### Start the Application:
  ○ Ensure your database server is running.

  ○ Run Login.java.
## Troubleshooting
### Database Connection Issues:
  ○ Verify database server is running and accessible.

  ○ Check DatabaseHandler.java for correct credentials.
