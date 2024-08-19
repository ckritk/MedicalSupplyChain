SQL> @ C:\test2.sql;
SQL> @"C:\Users\lavan\Downloads\drop.sql"
SQL> -- Drop Triggers
SQL> DROP TRIGGER trg_process_order;

Trigger dropped.

SQL> 
SQL> -- Drop Sequences
SQL> DROP SEQUENCE TranscSeq;

Sequence dropped.

SQL> DROP SEQUENCE WholesalerSeq;

Sequence dropped.

SQL> DROP SEQUENCE BatchSeq;

Sequence dropped.

SQL> DROP SEQUENCE ManufacturerSeq;

Sequence dropped.

SQL> DROP SEQUENCE DosageSeq;

Sequence dropped.

SQL> DROP SEQUENCE PharmacySeq;

Sequence dropped.

SQL> DROP SEQUENCE OrderSeq;

Sequence dropped.

SQL> DROP SEQUENCE ProviderSeq;

Sequence dropped.

SQL> DROP SEQUENCE RequestSeq;

Sequence dropped.

SQL> 
SQL> -- Drop Procedures
SQL> DROP PROCEDURE ProcessExpiredMedicinesBatch;

Procedure dropped.

SQL> DROP PROCEDURE ProcessExpiredMedicinesInv;

Procedure dropped.

SQL> DROP PROCEDURE UpdateTransactionsReq;

Procedure dropped.

SQL> DROP PROCEDURE UpdateTransactionsOrder;

Procedure dropped.

SQL> DROP PROCEDURE UpdateOrderedMedicinePrice;

Procedure dropped.

SQL> DROP PROCEDURE PlaceOrder;

Procedure dropped.

SQL> 
SQL> -- Drop Tables in reverse order of their dependencies
SQL> DROP TABLE Transactions CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Collection_Requests CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Request_Details CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Providers CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Ordered_Medicine CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Order_details CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Pharmacy CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Inventory CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Wholesalers CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE ManufacturePrice CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Batch CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Manufacturers CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Dosage CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Excipient CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE API CASCADE CONSTRAINTS;

Table dropped.

SQL> DROP TABLE Medicine CASCADE CONSTRAINTS;

Table dropped.

SQL> @"C:\Users\lavan\Downloads\create.sql"
SQL> -- Medicine Table
SQL> CREATE TABLE Medicine (
  2  	 MedName VARCHAR2(255) PRIMARY KEY,
  3  	 PrescriptionRequirement CHAR(1) CHECK (PrescriptionRequirement IN ('Y', 'N')) NOT NULL,
  4  	 Strength VARCHAR2(50) NOT NULL,
  5  	 ExpiryFromManufacture NUMBER NOT NULL CHECK (ExpiryFromManufacture > 0),
  6  	 Unit VARCHAR2(10) NOT NULL
  7  );

Table created.

SQL> 
SQL> -- API Table
SQL> CREATE TABLE API (
  2  	 MedName VARCHAR2(255),
  3  	 API VARCHAR2(255) NOT NULL,
  4  	 PRIMARY KEY (MedName, API),
  5  	 FOREIGN KEY (MedName) REFERENCES Medicine(MedName) ON DELETE CASCADE
  6  );

Table created.

SQL> 
SQL> -- Excipient Table
SQL> CREATE TABLE Excipient (
  2  	 MedName VARCHAR2(255),
  3  	 Excipient VARCHAR2(255) NOT NULL,
  4  	 PRIMARY KEY (MedName, Excipient),
  5  	 FOREIGN KEY (MedName) REFERENCES Medicine(MedName) ON DELETE CASCADE
  6  );

Table created.

SQL> 
SQL> -- Dosage Table
SQL> CREATE TABLE Dosage (
  2  	 MedicineID NUMBER PRIMARY KEY,
  3  	 MedName VARCHAR2(255),
  4  	 DosageForm VARCHAR2(50) NOT NULL,
  5  	 FOREIGN KEY (MedName) REFERENCES Medicine(MedName) ON DELETE CASCADE
  6  );

Table created.

SQL> 
SQL> 
SQL> -- Manufacturers Table
SQL> CREATE TABLE Manufacturers (
  2  	 ManufacturerID NUMBER PRIMARY KEY,
  3  	 Name VARCHAR2(255) NOT NULL,
  4  	 State VARCHAR2(100) NOT NULL,
  5  	 City VARCHAR2(100) NOT NULL,
  6  	 Pincode VARCHAR2(20) NOT NULL,
  7  	 Street VARCHAR2(255) NOT NULL,
  8  	 Email VARCHAR2(255) NOT NULL UNIQUE,
  9  	 Password VARCHAR2(255) NOT NULL,
 10  	 PhNo VARCHAR2(20) NOT NULL,
 11  	 CertificationDetails VARCHAR2(500)
 12  );

Table created.

SQL> 
SQL> 
SQL> -- Batch Table
SQL> CREATE TABLE Batch (
  2  	 BatchID NUMBER PRIMARY KEY,
  3  	 ManufacturerID NUMBER NOT NULL,
  4  	 MedicineID NUMBER NOT NULL,
  5  	 PackagesManufactured NUMBER NOT NULL,
  6  	 ManufacturedDate DATE NOT NULL,
  7  	 QuantityPerPackage NUMBER NOT NULL,
  8  	     CHECK (QuantityPerPackage > 0),
  9  	 FOREIGN KEY (ManufacturerID) REFERENCES Manufacturers(ManufacturerID) ON DELETE CASCADE,
 10  	 FOREIGN KEY (MedicineID) REFERENCES Dosage(MedicineID) ON DELETE CASCADE
 11  );

Table created.

SQL> 
SQL> -- ManufacturePrice Table
SQL> CREATE TABLE ManufacturePrice (
  2  	 MedicineID NUMBER NOT NULL,
  3  	 ManufacturerID NUMBER NOT NULL,
  4  	 QuantityPerPackage NUMBER NOT NULL CHECK (QuantityPerPackage > 0),
  5  	 PricePerPackage DECIMAL(10, 2) NOT NULL CHECK (PricePerPackage > 0),
  6  	 PRIMARY KEY (MedicineID, ManufacturerID, QuantityPerPackage),
  7  	 FOREIGN KEY (MedicineID) REFERENCES Dosage(MedicineID) ON DELETE CASCADE,
  8  	 FOREIGN KEY (ManufacturerID) REFERENCES Manufacturers(ManufacturerID) ON DELETE CASCADE
  9  );

Table created.

SQL> 
SQL> 
SQL> -- Wholesalers Table
SQL> CREATE TABLE Wholesalers (
  2  	 WholesalerID NUMBER PRIMARY KEY,
  3  	 Name VARCHAR2(255) NOT NULL,
  4  	 State VARCHAR2(100) NOT NULL,
  5  	 City VARCHAR2(100) NOT NULL,
  6  	 Pincode VARCHAR2(20) NOT NULL,
  7  	 Street VARCHAR2(255) NOT NULL,
  8  	 Email VARCHAR2(255) NOT NULL UNIQUE,
  9  	 Password VARCHAR2(255) NOT NULL,
 10  	 PhNo VARCHAR2(20) NOT NULL,
 11  	 CertificationDetails VARCHAR2(500)
 12  );

Table created.

SQL> 
SQL> -- Inventory Table
SQL> CREATE TABLE Inventory (
  2  	 WholesalerID NUMBER NOT NULL,
  3  	 BatchID NUMBER NOT NULL,
  4  	 QuantityInStock NUMBER NOT NULL CHECK (QuantityInStock >= 0),
  5  	 PricePerPackage DECIMAL(10, 2) NOT NULL CHECK (PricePerPackage > 0),
  6  	 PRIMARY KEY (WholesalerID, BatchID),
  7  	 FOREIGN KEY (WholesalerID) REFERENCES Wholesalers(WholesalerID) ON DELETE CASCADE,
  8  	 FOREIGN KEY (BatchID) REFERENCES Batch(BatchID) ON DELETE CASCADE
  9  );

Table created.

SQL> 
SQL> -- Pharmacy
SQL> CREATE TABLE Pharmacy (
  2  	 PharmacyID NUMBER PRIMARY KEY,
  3  	 Name VARCHAR2(255) NOT NULL,
  4  	 State VARCHAR2(100) NOT NULL,
  5  	 City VARCHAR2(100) NOT NULL,
  6  	 Street VARCHAR2(255) NOT NULL,
  7  	 Pincode VARCHAR2(20) NOT NULL,
  8  	 Email VARCHAR2(255) NOT NULL UNIQUE,
  9  	 Password VARCHAR2(255) NOT NULL,
 10  	 PhNo VARCHAR2(20) NOT NULL,
 11  	 CertificationDetails VARCHAR2(500)
 12  );

Table created.

SQL> 
SQL> -- Order_details
SQL> CREATE TABLE Order_details (
  2  	 OrderID NUMBER PRIMARY KEY,
  3  	 OrderDate DATE NOT NULL,
  4  	 OrderedByPharmacy NUMBER,
  5  	 OrderedByWholesaler NUMBER,
  6  	 OrderedFromWholesaler NUMBER,
  7  	 OrderedFromManufacturer NUMBER,
  8  	 SupplyStatus VARCHAR2(50) NOT NULL,
  9  	 CONSTRAINT chk_order_source CHECK (
 10  	     (OrderedByPharmacy IS NOT NULL AND OrderedByWholesaler IS NULL AND OrderedFromWholesaler IS NOT NULL AND OrderedFromManufacturer IS NULL) OR
 11  	     (OrderedByPharmacy IS NULL AND OrderedByWholesaler IS NOT NULL AND OrderedFromWholesaler IS NULL AND OrderedFromManufacturer IS NOT NULL)
 12  	 ),
 13  	 FOREIGN KEY (OrderedByPharmacy) REFERENCES Pharmacy(PharmacyID) ON DELETE CASCADE,
 14  	 FOREIGN KEY (OrderedByWholesaler) REFERENCES Wholesalers(WholesalerID) ON DELETE CASCADE,
 15  	 FOREIGN KEY (OrderedFromWholesaler) REFERENCES Wholesalers(WholesalerID) ON DELETE CASCADE,
 16  	 FOREIGN KEY (OrderedFromManufacturer) REFERENCES Manufacturers(ManufacturerID) ON DELETE CASCADE
 17  );

Table created.

SQL> 
SQL> -- Ordered_Medicine
SQL> CREATE TABLE Ordered_Medicine (
  2  	 OrderID NUMBER NOT NULL,
  3  	 MedicineID NUMBER NOT NULL,
  4  	 PackagesOrdered NUMBER NOT NULL CHECK (PackagesOrdered > 0),
  5  	 PricePerPackage DECIMAL(10, 2) NOT NULL CHECK (PricePerPackage > 0),
  6  	 PRIMARY KEY (OrderID, MedicineID),
  7  	 FOREIGN KEY (OrderID) REFERENCES Order_details(OrderID) ON DELETE CASCADE,
  8  	 FOREIGN KEY (MedicineID) REFERENCES Dosage(MedicineID) ON DELETE CASCADE
  9  );

Table created.

SQL> 
SQL> -- Providers
SQL> CREATE TABLE Providers (
  2  	 ProviderID NUMBER PRIMARY KEY,
  3  	 Name VARCHAR2(255) NOT NULL,
  4  	 State VARCHAR2(100) NOT NULL,
  5  	 City VARCHAR2(100) NOT NULL,
  6  	 Street VARCHAR2(255) NOT NULL,
  7  	 Pincode VARCHAR2(20) NOT NULL,
  8  	 Email VARCHAR2(255) NOT NULL UNIQUE,
  9  	 Password VARCHAR2(255) NOT NULL,
 10  	 PhNo VARCHAR2(20) NOT NULL,
 11  	 CertificationDetails VARCHAR2(500),
 12  	 CoverageArea VARCHAR2(255),
 13  	 ChargePerPackage DECIMAL(10, 2) NOT NULL,
 14  	      CHECK (ChargePerPackage > 0)
 15  );

Table created.

SQL> 
SQL> -- Request_Details
SQL> CREATE TABLE Request_Details (
  2  	 RequestID NUMBER PRIMARY KEY,
  3  	 PharmacyID NUMBER,
  4  	 WholesalerID NUMBER,
  5  	 ManufacturerID NUMBER,
  6  	 ProviderID NUMBER NOT NULL,
  7  	 Scheduled_pickup_date DATE,
  8  	 Status CHAR(1) NOT NULL CHECK (Status IN ('Y', 'N')),
  9  	 CONSTRAINT chk_one_not_null CHECK (
 10  	     (PharmacyID IS NOT NULL AND WholesalerID IS NULL AND ManufacturerID IS NULL) OR
 11  	     (PharmacyID IS NULL AND WholesalerID IS NOT NULL AND ManufacturerID IS NULL) OR
 12  	     (PharmacyID IS NULL AND WholesalerID IS NULL AND ManufacturerID IS NOT NULL)
 13  	 ),
 14  	 FOREIGN KEY (ProviderID) REFERENCES Providers(ProviderID) ON DELETE CASCADE
 15  );

Table created.

SQL> 
SQL> -- Collection_Requests
SQL> CREATE TABLE Collection_Requests (
  2  	 RequestID NUMBER,
  3  	 WasteType NUMBER NOT NULL,
  4  	 Quantity NUMBER NOT NULL,
  5  	 FOREIGN KEY (RequestID) REFERENCES Request_Details(RequestID) ON DELETE CASCADE,
  6  	 FOREIGN KEY (WasteType) REFERENCES Dosage(MedicineID) ON DELETE CASCADE,
  7  	 PRIMARY KEY(RequestID,WasteType)
  8  );

Table created.

SQL> 
SQL> -- Transactions
SQL> CREATE TABLE Transactions (
  2  	 TransactionID NUMBER PRIMARY KEY,
  3  	 RequestID NUMBER,
  4  	 OrderID NUMBER,
  5  	 InvoiceAmount DECIMAL(10, 2),
  6  	 OrderStatus VARCHAR2(50),
  7  	 CONSTRAINT chk_one_not_null_transaction CHECK (
  8  	     (RequestID IS NOT NULL AND OrderID IS NULL) OR
  9  	     (RequestID IS NULL AND OrderID IS NOT NULL)
 10  	 ),
 11  	 FOREIGN KEY (RequestID) REFERENCES Request_Details(RequestID) ON DELETE CASCADE
 12  );

Table created.

SQL> @"C:\Users\lavan\Downloads\procedure.sql"
SQL> -- Sequence
SQL> CREATE SEQUENCE DosageSeq	     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE ManufacturerSeq START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE BatchSeq	     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE WholesalerSeq   START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE PharmacySeq     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE OrderSeq	     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE ProviderSeq     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE RequestSeq      START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> CREATE SEQUENCE TranscSeq	     START WITH 1 INCREMENT BY 1;

Sequence created.

SQL> 
SQL> @"C:\Users\lavan\Downloads\insert.sql"
SQL> -- Delete all rows from Medicine table
SQL> DELETE FROM Medicine;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from API table
SQL> DELETE FROM API;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Excipient table
SQL> DELETE FROM Excipient;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Dosage table
SQL> DELETE FROM Dosage;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Manufacturers table
SQL> DELETE FROM Manufacturers;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Batch table
SQL> DELETE FROM Batch;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from ManufacturePrice table
SQL> DELETE FROM ManufacturePrice;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Wholesalers table
SQL> DELETE FROM Wholesalers;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Inventory table
SQL> DELETE FROM Inventory;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Pharmacy table
SQL> DELETE FROM Pharmacy;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Order_details table
SQL> DELETE FROM Order_details;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Ordered_Medicine table
SQL> DELETE FROM Ordered_Medicine;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Providers table
SQL> DELETE FROM Providers;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Request_Details table
SQL> DELETE FROM Request_Details;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Collection_Requests table
SQL> DELETE FROM Collection_Requests;

0 rows deleted.

SQL> 
SQL> -- Delete all rows from Transactions table
SQL> DELETE FROM Transactions;

0 rows deleted.

SQL> 
SQL> -- Insert statements for Medicine table
SQL> INSERT INTO Medicine (MedName, PrescriptionRequirement, Strength, ExpiryFromManufacture, Unit)
  2  VALUES ('Paracetamol', 'Y', '500mg', 24, 'Tablet');

1 row created.

SQL> 
SQL> INSERT INTO Medicine (MedName, PrescriptionRequirement, Strength, ExpiryFromManufacture, Unit)
  2  VALUES ('Amoxicillin', 'Y', '250mg', 15, 'Capsule');

1 row created.

SQL> 
SQL> INSERT INTO Medicine (MedName, PrescriptionRequirement, Strength, ExpiryFromManufacture, Unit)
  2  VALUES ('Insulin', 'Y', '100 Units/ml', 12, 'Injection');

1 row created.

SQL> 
SQL> INSERT INTO Medicine (MedName, PrescriptionRequirement, Strength, ExpiryFromManufacture, Unit)
  2  VALUES ('Cough Syrup', 'N', '5mg/5ml', 10, 'Syrup');

1 row created.

SQL> 
SQL> INSERT INTO Medicine (MedName, PrescriptionRequirement, Strength, ExpiryFromManufacture, Unit)
  2  VALUES ('Hydrocortisone Cream', 'N', '1%', 20, 'Cream');

1 row created.

SQL> 
SQL> -- Insert statements for API table (more than 5 rows)
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Paracetamol', 'Acetaminophen');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Paracetamol', 'Aspirin');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Amoxicillin', 'Penicillin');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Insulin', 'Human insulin');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Insulin', 'Insulin glargine');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Cough Syrup', 'Dextromethorphan');

1 row created.

SQL> 
SQL> INSERT INTO API (MedName, API)
  2  VALUES ('Hydrocortisone Cream', 'Hydrocortisone');

1 row created.

SQL> 
SQL> 
SQL> -- Insert statements for Excipient table (more than 5 rows)
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Paracetamol', 'Microcrystalline cellulose');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Paracetamol', 'Povidone');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Amoxicillin', 'Sodium lauryl sulfate');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Insulin', 'Zinc oxide');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Cough Syrup', 'Sucrose');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Hydrocortisone Cream', 'White petrolatum');

1 row created.

SQL> 
SQL> INSERT INTO Excipient (MedName, Excipient)
  2  VALUES ('Hydrocortisone Cream', 'Glycerin');

1 row created.

SQL> 
SQL> 
SQL> -- Insert statements for Dosage table (more than 5 rows)
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Paracetamol', 'Tablet');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Paracetamol', 'Syrup');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Amoxicillin', 'Capsule');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Insulin', 'Injection');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Insulin', 'Nasal spray');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Cough Syrup', 'Drops');

1 row created.

SQL> 
SQL> INSERT INTO Dosage (MedicineID, MedName, DosageForm)
  2  VALUES (DosageSeq.NEXTVAL, 'Hydrocortisone Cream', 'Ointment');

1 row created.

SQL> 
SQL> 
SQL> -- Insert statements for Manufacturers table
SQL> INSERT INTO Manufacturers (ManufacturerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (ManufacturerSeq.NEXTVAL, 'PharmaTech Inc.', 'Maharashtra', 'Mumbai', '400001', '123 Main St', 'pharmatech@example.com', 'password123', '+91-9876543210', 'ISO 9001 Certified');

1 row created.

SQL> 
SQL> INSERT INTO Manufacturers (ManufacturerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (ManufacturerSeq.NEXTVAL, 'MediPro Laboratories', 'Delhi', 'New Delhi', '110001', '456 Elm St', 'medipro@example.com', 'securepass', '+91-9876543211', 'FDA Approved');

1 row created.

SQL> 
SQL> INSERT INTO Manufacturers (ManufacturerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (ManufacturerSeq.NEXTVAL, 'PharmaCare Ltd.', 'Karnataka', 'Bangalore', '560001', '789 Oak St', 'pharmacare@example.com', 'mypassword', '+91-9876543212', 'GMP Certified');

1 row created.

SQL> 
SQL> INSERT INTO Manufacturers (ManufacturerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (ManufacturerSeq.NEXTVAL, 'BioGen Solutions', 'Telangana', 'Hyderabad', '500001', '101 Maple St', 'biogen@example.com', 'biopass', '+91-9876543213', 'Bio-Tech Certified');

1 row created.

SQL> 
SQL> INSERT INTO Manufacturers (ManufacturerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (ManufacturerSeq.NEXTVAL, 'MediCo Pharma', 'Tamil Nadu', 'Chennai', '600001', '777 Pine St', 'medico@example.com', 'medico123', '+91-9876543214', 'ISO 13485 Certified');

1 row created.

SQL> 
SQL> -- Insert statements for Batch table
SQL> --HERE
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 1, 1, 1000, TO_DATE('2024-06-01', 'YYYY-MM-DD'), 50);

1 row created.

SQL> 
SQL> --HERE --
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 1, 2, 1, TO_DATE('2024-05-15', 'YYYY-MM-DD'), 40);

1 row created.

SQL> 
SQL> --HERE --
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 1, 3, 1200, TO_DATE('2024-05-10', 'YYYY-MM-DD'), 60);

1 row created.

SQL> 
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 2, 4, 600, TO_DATE('2023-05-20', 'YYYY-MM-DD'), 30);

1 row created.

SQL> 
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 2, 5, 1500, TO_DATE('2024-06-03', 'YYYY-MM-DD'), 75);

1 row created.

SQL> 
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 3, 6, 1000, TO_DATE('2024-05-25', 'YYYY-MM-DD'), 50);

1 row created.

SQL> 
SQL> INSERT INTO Batch (BatchID, ManufacturerID, MedicineID, packagesmanufactured, ManufacturedDate, QuantityPerPackage)
  2  VALUES (BatchSeq.NEXTVAL, 3, 7, 800, TO_DATE('2024-06-02', 'YYYY-MM-DD'), 40);

1 row created.

SQL> 
SQL> -- Insert statements for ManufacturePrice table
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (1, 1, 50, 10.00);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (2, 1, 40, 8.50);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (3, 1, 60, 12.00);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (4, 2, 30, 7.00);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (5, 2, 75, 15.00);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (6, 3, 50, 9.00);

1 row created.

SQL> 
SQL> INSERT INTO ManufacturePrice (MedicineID, ManufacturerID, QuantityPerPackage, PricePerPackage)
  2  VALUES (7, 3, 40, 8.00);

1 row created.

SQL> 
SQL> -- Insert statements for Wholesalers table
SQL> INSERT INTO Wholesalers (WholesalerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (WholesalerSeq.NEXTVAL, 'MediSup India', 'Maharashtra', 'Mumbai', '400001', '123 MG Road', 'medisup@example.com', 'medisuppass', '022-12345678', 'ISO 9001 Certified');

1 row created.

SQL> 
SQL> INSERT INTO Wholesalers (WholesalerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (WholesalerSeq.NEXTVAL, 'HealthLink Solutions', 'Delhi', 'New Delhi', '110001', '456 Nehru Place', 'healthlink@example.com', 'healthlinkpass', '011-23456789', 'GMP Certified');

1 row created.

SQL> 
SQL> INSERT INTO Wholesalers (WholesalerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (WholesalerSeq.NEXTVAL, 'PharmaConnect', 'Karnataka', 'Bangalore', '560001', '789 Brigade Road', 'pharmaconnect@example.com', 'pharmaconnectpass', '080-34567890', 'FDA Approved');

1 row created.

SQL> 
SQL> INSERT INTO Wholesalers (WholesalerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (WholesalerSeq.NEXTVAL, 'MediSupply Pvt. Ltd.', 'Tamil Nadu', 'Chennai', '600001', '101 Mount Road', 'medisupply@example.com', 'medisupplypass', '044-45678901', 'ISO 13485 Certified');

1 row created.

SQL> 
SQL> INSERT INTO Wholesalers (WholesalerID, Name, State, City, Pincode, Street, Email, Password, PhNo, CertificationDetails)
  2  VALUES (WholesalerSeq.NEXTVAL, 'PharmaLink Enterprises', 'Telangana', 'Hyderabad', '500001', '222 Hitech City', 'pharmalink@example.com', 'pharmalinkpass', '040-56789012', 'WHO GMP Certified');

1 row created.

SQL> 
SQL> -- Insert statements for Inventory table
SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (1, 1, 500, 25.00);

1 row created.

SQL> 
SQL> --HERE
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (2, 2, 750, 30.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (3, 3, 600, 20.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (4, 4, 900, 35.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (5, 5, 800, 28.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (1, 6, 550, 27.00);

1 row created.

SQL> 
SQL> --HERE
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (2, 7, 720, 32.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (3, 1, 620, 22.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (4, 2, 950, 38.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (5, 3, 870, 29.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (1, 4, 480, 24.00);

1 row created.

SQL> 
SQL> --HERE
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (2, 5, 700, 31.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (3, 6, 580, 21.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (4, 7, 920, 36.00);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (5, 1, 830, 27.50);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (1, 2, 510, 26.50);

1 row created.

SQL> 
SQL> --HERE
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (2, 3, 740, 32.50);

1 row created.

SQL> 
SQL> --HERE
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (2, 4, 630, 23.50);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (4, 5, 960, 39.50);

1 row created.

SQL> 
SQL> INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
  2  VALUES (5, 6, 890, 30.50);

1 row created.

SQL> 
SQL> -- Insert statements for Pharmacy table
SQL> INSERT INTO Pharmacy (PharmacyID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails)
  2  VALUES (PharmacySeq.NEXTVAL, 'MediHealth Pharmacy', 'Maharashtra', 'Mumbai', '123 Shivaji Nagar', '400001', 'medihealth@example.com', 'medihealth123', '022-12345678', 'ISO 9001 Certified');

1 row created.

SQL> 
SQL> INSERT INTO Pharmacy (PharmacyID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails)
  2  VALUES (PharmacySeq.NEXTVAL, 'CureWell Pharmacy', 'Delhi', 'New Delhi', '456 Lajpat Nagar', '110001', 'curewell@example.com', 'curewell456', '011-98765432', 'Pharma Council Certified');

1 row created.

SQL> 
SQL> INSERT INTO Pharmacy (PharmacyID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails)
  2  VALUES (PharmacySeq.NEXTVAL, 'GreenLife Pharmacy', 'Karnataka', 'Bangalore', '789 MG Road', '560001', 'greenlife@example.com', 'greenlife789', '080-11223344', 'GMP Certified');

1 row created.

SQL> 
SQL> INSERT INTO Pharmacy (PharmacyID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails)
  2  VALUES (PharmacySeq.NEXTVAL, 'HealthPlus Pharmacy', 'Tamil Nadu', 'Chennai', '101 Anna Salai', '600001', 'healthplus@example.com', 'healthplus101', '044-99887766', 'FDA Approved');

1 row created.

SQL> 
SQL> INSERT INTO Pharmacy (PharmacyID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails)
  2  VALUES (PharmacySeq.NEXTVAL, 'MediServe Pharmacy', 'West Bengal', 'Kolkata', '321 Park Street', '700001', 'mediserve@example.com', 'mediserve321', '033-44556677', 'ISO 14001 Certified');

1 row created.

SQL> 
SQL> -- Insert statements for Order_details table
SQL> INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
  2  VALUES (OrderSeq.NEXTVAL, TO_DATE('2024-06-01', 'YYYY-MM-DD'), NULL, 1, NULL, 2 , 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
  2  VALUES (OrderSeq.NEXTVAL, TO_DATE('2024-06-02', 'YYYY-MM-DD'), 2, NULL, 3, NULL, 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
  2  VALUES (OrderSeq.NEXTVAL, TO_DATE('2024-06-03', 'YYYY-MM-DD'), NULL, 3, NULL, 4, 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
  2  VALUES (OrderSeq.NEXTVAL, TO_DATE('2024-06-04', 'YYYY-MM-DD'), NULL, 3, NULL, 2, 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
  2  VALUES (OrderSeq.NEXTVAL, TO_DATE('2024-06-05', 'YYYY-MM-DD'), 5, NULL, 4, NULL, 'Pending');

1 row created.

SQL> 
SQL> -- Insert statements for Ordered_Medicine table
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (1, 1, 100, 20.50);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (1, 2, 50, 30.75);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (2, 3, 75, 25.00);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (2, 4, 100, 18.25);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (2, 5, 125, 22.90);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (3, 6, 80, 15.00);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (3, 7, 120, 28.50);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (4, 1, 60, 20.50);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (4, 2, 90, 30.75);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (5, 3, 150, 25.00);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (5, 4, 80, 18.25);

1 row created.

SQL> 
SQL> INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
  2  VALUES (5, 5, 110, 22.90);

1 row created.

SQL> 
SQL> -- Insert statements for Providers table
SQL> INSERT INTO Providers (ProviderID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails, CoverageArea, ChargePerPackage)
  2  VALUES (ProviderSeq.NEXTVAL, 'MediSupply', 'Maharashtra', 'Mumbai', '789 Palm St', '400001', 'medisupply@example.com', 'securepass', '+91-9876543210', 'ISO 9001 Certified', 'Mumbai Metro Area', 15.50);

1 row created.

SQL> 
SQL> INSERT INTO Providers (ProviderID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails, CoverageArea, ChargePerPackage)
  2  VALUES (ProviderSeq.NEXTVAL, 'MediConnect', 'Delhi', 'New Delhi', '456 Oak St', '110001', 'mediconnect@example.com', 'password123', '+91-8765432109', 'FDA Approved', 'Delhi NCR', 18.75);

1 row created.

SQL> 
SQL> INSERT INTO Providers (ProviderID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails, CoverageArea, ChargePerPackage)
  2  VALUES (ProviderSeq.NEXTVAL, 'HealthFirst Logistics', 'Karnataka', 'Bangalore', '123 Maple St', '560001', 'healthfirst@example.com', 'mypassword', '+91-7654321098', 'GMP Certified', 'Bangalore Urban', 20.25);

1 row created.

SQL> 
SQL> INSERT INTO Providers (ProviderID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails, CoverageArea, ChargePerPackage)
  2  VALUES (ProviderSeq.NEXTVAL, 'CareCourier Services', 'Tamil Nadu', 'Chennai', '101 Pine St', '600001', 'carecourier@example.com', 'courierpass', '+91-6543210987', NULL, 'Chennai Metropolitan Area', 16.80);

1 row created.

SQL> 
SQL> INSERT INTO Providers (ProviderID, Name, State, City, Street, Pincode, Email, Password, PhNo, CertificationDetails, CoverageArea, ChargePerPackage)
  2  VALUES (ProviderSeq.NEXTVAL, 'MediLogistics Solutions', 'Telangana', 'Hyderabad', '789 Elm St', '500001', 'medilogistics@example.com', 'logisticpass', '+91-5432109876', 'ISO 13485 Certified', 'Hyderabad Metropolitan Region', 19.00);

1 row created.

SQL> 
SQL> -- Insert statements for Request_Details table
SQL> INSERT INTO Request_Details (RequestID, PharmacyID, WholesalerID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
  2  VALUES (RequestSeq.NEXTVAL, 1, NULL, NULL, 1, TO_DATE('2024-06-01', 'YYYY-MM-DD'), 'Y');

1 row created.

SQL> 
SQL> INSERT INTO Request_Details (RequestID, PharmacyID, WholesalerID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
  2  VALUES (RequestSeq.NEXTVAL, NULL, 1, NULL, 2, TO_DATE('2024-06-02', 'YYYY-MM-DD'), 'N');

1 row created.

SQL> 
SQL> INSERT INTO Request_Details (RequestID, PharmacyID, WholesalerID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
  2  VALUES (RequestSeq.NEXTVAL, NULL, NULL, 1, 3, TO_DATE('2024-06-03', 'YYYY-MM-DD'), 'N');

1 row created.

SQL> 
SQL> INSERT INTO Request_Details (RequestID, PharmacyID, WholesalerID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
  2  VALUES (RequestSeq.NEXTVAL, 2, NULL, NULL, 4, TO_DATE('2024-06-04', 'YYYY-MM-DD'), 'N');

1 row created.

SQL> 
SQL> INSERT INTO Request_Details (RequestID, PharmacyID, WholesalerID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
  2  VALUES (RequestSeq.NEXTVAL, NULL, 2, NULL, 5, TO_DATE('2024-06-05', 'YYYY-MM-DD'), 'Y');

1 row created.

SQL> 
SQL> -- Insert statements for Collection_Requests table
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (1, 1, 100);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (1, 2, 200);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (2, 3, 150);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (3, 4, 120);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (3, 5, 180);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (4, 6, 130);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (4, 7, 140);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (5, 1, 110);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (5, 2, 220);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (5, 3, 190);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (5, 4, 160);

1 row created.

SQL> 
SQL> INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
  2  VALUES (5, 5, 180);

1 row created.

SQL> 
SQL> -- Insert statements for Transactions table
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, 1, NULL, 15.50 * (SELECT SUM(Quantity) FROM Collection_Requests WHERE RequestID = 1), 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, 2, NULL, 18.75 * (SELECT SUM(Quantity) FROM Collection_Requests WHERE RequestID = 2), 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, 3, NULL, 20.25 * (SELECT SUM(Quantity) FROM Collection_Requests WHERE RequestID = 3), 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, 4, NULL, 16.80 * (SELECT SUM(Quantity) FROM Collection_Requests WHERE RequestID = 4), 'Completed');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, 5, NULL, 19.00 * (SELECT SUM(Quantity) FROM Collection_Requests WHERE RequestID = 5), 'Pending');

1 row created.

SQL> 
SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, NULL, 1, NULL, 'Completed');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, NULL, 2, NULL, 'Pending');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, NULL, 3, NULL, 'Completed');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, NULL, 4, NULL, 'Completed');

1 row created.

SQL> 
SQL> INSERT INTO Transactions (TransactionID, RequestID, OrderID, InvoiceAmount, OrderStatus)
  2  VALUES (TranscSeq.NEXTVAL, NULL, 5, NULL, 'Pending');

1 row created.

SQL> select * from batch where manufacturerid =1;

   BATCHID MANUFACTURERID MEDICINEID PACKAGESMANUFACTURED MANUFACTU             
---------- -------------- ---------- -------------------- ---------             
QUANTITYPERPACKAGE                                                              
------------------                                                              
         1              1          1                 1000 01-JUN-24             
                50                                                              
                                                                                
         2              1          2                    1 15-MAY-24             
                40                                                              
                                                                                
         3              1          3                 1200 10-MAY-24             
                60                                                              
                                                                                

SQL> select * from inventory where wholesalerid = 2;

WHOLESALERID    BATCHID QUANTITYINSTOCK PRICEPERPACKAGE                         
------------ ---------- --------------- ---------------                         
           2          2             750              30                         
           2          7             720              32                         
           2          5             700              31                         
           2          3             740            32.5                         
           2          4             630            23.5                         

SQL> @"C:\Users\lavan\OneDrive\Documents\dbms_proj_spool2.sql"
SQL> CREATE OR REPLACE TRIGGER trg_process_order
  2  AFTER INSERT ON Ordered_Medicine
  3  FOR EACH ROW
  4  DECLARE
  5  	 v_price_per_package DECIMAL(10, 2);
  6  	 v_packages_ordered NUMBER;
  7  	 v_ordered_by_pharmacy NUMBER;
  8  	 v_ordered_by_wholesaler NUMBER;
  9  	 v_ordered_from_wholesaler NUMBER;
 10  	 v_ordered_from_manufacturer NUMBER;
 11  	 v_out_of_stock EXCEPTION;
 12  	 v_inventory_empty NUMBER;
 13  	 v_batch_empty NUMBER;
 14  	 v_existing_inventory_row NUMBER;
 15  	 v_insufficient_stock EXCEPTION;
 16  	 v_invoice_amount NUMBER;
 17  
 18  BEGIN
 19  	 -- Retrieve order details
 20  	 SELECT OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer
 21  	 INTO v_ordered_by_pharmacy, v_ordered_by_wholesaler, v_ordered_from_wholesaler, v_ordered_from_manufacturer
 22  	 FROM Order_details
 23  	 WHERE OrderID = :NEW.OrderID;
 24  
 25  	 IF v_ordered_by_pharmacy IS NULL AND v_ordered_from_wholesaler IS NULL THEN
 26  	     -- Wholesaler ordered from Manufacturer
 27  
 28  	     -- Process the order and update inventory
 29  	     v_packages_ordered := :NEW.PackagesOrdered;
 30  
 31  	     -- Check if there are inventory records available for the requested medicine
 32  	     SELECT SUM(b.PackagesManufactured) INTO v_batch_empty
 33  	     FROM Batch b
 34  	     WHERE b.ManufacturerID = v_ordered_from_manufacturer
 35  	     AND b.MedicineID = :NEW.MedicineID;
 36  
 37  	     IF v_batch_empty IS NULL OR v_batch_empty = 0 THEN
 38  		 -- Rollback transaction if no records are found
 39  		 RAISE v_out_of_stock;
 40  	     ELSIF v_batch_empty < v_packages_ordered THEN
 41  		 RAISE v_insufficient_stock;
 42  
 43  	     ELSE
 44  		 SELECT PricePerPackage INTO v_price_per_package
 45  		 FROM ManufacturePrice
 46  		 WHERE MedicineID = :NEW.MedicineID AND ManufacturerID = v_ordered_from_manufacturer;
 47  
 48  
 49  		 FOR batch_rec IN (
 50  		     SELECT b.BatchID, b.PackagesManufactured
 51  		     FROM Batch b
 52  		     WHERE b.MedicineID = :NEW.MedicineID
 53  		     AND b.ManufacturerID = v_ordered_from_manufacturer
 54  		     ORDER BY b.ManufacturedDate
 55  		 )
 56  		 LOOP
 57  		     IF v_packages_ordered <= 0 THEN
 58  			 EXIT;
 59  		     END IF;
 60  
 61  		     IF batch_rec.PackagesManufactured >= v_packages_ordered THEN
 62  			 -- Ensure PackagesManufactured does not violate the constraint
 63  			 IF batch_rec.PackagesManufactured - v_packages_ordered < 0 THEN
 64  			     RAISE v_insufficient_stock;
 65  			 END IF;
 66  
 67  			 SELECT COUNT(*) INTO v_existing_inventory_row
 68  			 FROM Inventory
 69  			 WHERE BatchID = batch_rec.BatchID AND WholesalerID = v_ordered_by_wholesaler;
 70  
 71  			 IF v_existing_inventory_row > 0 THEN
 72  			     -- If an existing inventory row is found, update its QuantityInStock
 73  			     UPDATE Inventory
 74  			     SET QuantityInStock = QuantityInStock + v_packages_ordered
 75  			     WHERE BatchID = batch_rec.BatchID AND WholesalerID = v_ordered_by_wholesaler;
 76  			 ELSE
 77  			     -- If no existing inventory row is found, insert a new row
 78  			     INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
 79  			     VALUES (v_ordered_by_wholesaler, batch_rec.BatchID, v_packages_ordered, v_price_per_package);
 80  			 END IF;
 81  
 82  			 UPDATE Batch
 83  			 SET PackagesManufactured = batch_rec.PackagesManufactured - v_packages_ordered
 84  			 WHERE BatchID = batch_rec.BatchID AND ManufacturerID = v_ordered_from_manufacturer;
 85  
 86  			 v_packages_ordered := 0;
 87  		     ELSE
 88  			 -- Ensure PackagesManufactured does not violate the constraint
 89  			 IF batch_rec.PackagesManufactured < 0 THEN
 90  			     RAISE v_insufficient_stock;
 91  			 END IF;
 92  
 93  			 SELECT COUNT(*) INTO v_existing_inventory_row
 94  			 FROM Inventory
 95  			 WHERE BatchID = batch_rec.BatchID AND WholesalerID = v_ordered_by_wholesaler;
 96  
 97  			 IF v_existing_inventory_row > 0 THEN
 98  			     -- If an existing inventory row is found, update its QuantityInStock
 99  			     UPDATE Inventory
100  			     SET QuantityInStock = QuantityInStock + batch_rec.PackagesManufactured
101  			     WHERE BatchID = batch_rec.BatchID AND WholesalerID = v_ordered_by_wholesaler;
102  			 ELSE
103  			     -- If no existing inventory row is found, insert a new row
104  			     INSERT INTO Inventory (WholesalerID, BatchID, QuantityInStock, PricePerPackage)
105  			     VALUES (v_ordered_by_wholesaler, batch_rec.BatchID, batch_rec.PackagesManufactured, v_price_per_package);
106  			 END IF;
107  
108  			 v_packages_ordered := v_packages_ordered - batch_rec.PackagesManufactured;
109  
110  			 UPDATE Batch
111  			 SET PackagesManufactured = 0
112  			 WHERE BatchID = batch_rec.BatchID AND ManufacturerID = v_ordered_from_manufacturer;
113  		     END IF;
114  		 END LOOP;
115  	     END IF;
116  	 ELSE
117  	     -- Pharmacy ordered from Wholesaler
118  	     -- Check if there are inventory records available for the requested medicine
119  	     v_packages_ordered := :NEW.PackagesOrdered;
120  
121  	     SELECT SUM(i.QuantityInStock) INTO v_inventory_empty
122  	     FROM Inventory i
123  	     JOIN Batch b ON i.BatchID = b.BatchID
124  	     WHERE i.WholesalerID = v_ordered_from_wholesaler
125  	     AND b.MedicineID = :NEW.MedicineID;
126  
127  	     IF v_inventory_empty IS NULL OR v_inventory_empty = 0 THEN
128  		 -- Rollback transaction if no records are found
129  		 RAISE v_out_of_stock;
130  	     ELSIF v_inventory_empty < v_packages_ordered THEN
131  		 RAISE v_insufficient_stock;
132  	     ELSE
133  
134  		 SELECT i.PricePerPackage INTO v_price_per_package
135  		 FROM Inventory i
136  		 JOIN Batch b ON i.BatchID = b.BatchID
137  		 WHERE b.MedicineID = :NEW.MedicineID AND WholesalerID = v_ordered_from_wholesaler;
138  
139  		 -- Loop through inventory records and process the order
140  		 FOR inventory_rec IN (
141  		     SELECT i.BatchID, i.QuantityInStock, i.PricePerPackage
142  		     FROM Inventory i
143  		     JOIN Batch b ON i.BatchID = b.BatchID
144  		     WHERE i.WholesalerID = v_ordered_from_wholesaler
145  		     AND b.MedicineID = :NEW.MedicineID
146  		     ORDER BY i.BatchID
147  		 )
148  		 LOOP
149  		     IF v_packages_ordered <= 0 THEN
150  			 EXIT;
151  		     END IF;
152  
153  		     IF inventory_rec.QuantityInStock >= v_packages_ordered THEN
154  			 UPDATE Inventory
155  			 SET QuantityInStock = inventory_rec.QuantityInStock - v_packages_ordered
156  			 WHERE WholesalerID = v_ordered_from_wholesaler AND BatchID = inventory_rec.BatchID;
157  
158  			 v_packages_ordered := 0;
159  		     ELSE
160  			 UPDATE Inventory
161  			 SET QuantityInStock = 0
162  			 WHERE WholesalerID = v_ordered_from_wholesaler AND BatchID = inventory_rec.BatchID;
163  
164  			 v_packages_ordered := v_packages_ordered - inventory_rec.QuantityInStock;
165  		     END IF;
166  		 END LOOP;
167  	     END IF;
168  	 END IF;
169  
170  	 -- Calculate invoice amount
171  	 v_invoice_amount := v_price_per_package * :NEW.PackagesOrdered;
172  
173  	 -- Update the Transactions table
174  	 UPDATE Transactions
175  	 SET InvoiceAmount = InvoiceAmount + v_invoice_amount
176  	 WHERE OrderID = :NEW.OrderID;
177  
178  	 DBMS_OUTPUT.PUT_LINE('Price per package: ' || v_price_per_package);
179  	 DBMS_OUTPUT.PUT_LINE('Packages ordered: ' || :NEW.PackagesOrdered);
180  	 DBMS_OUTPUT.PUT_LINE('Invoice amount: ' || v_invoice_amount);
181  
182  
183  EXCEPTION
184  	 WHEN v_out_of_stock THEN
185  	     DBMS_OUTPUT.PUT_LINE('MedicineID is out of stock');
186  	 WHEN v_insufficient_stock THEN
187  	     DBMS_OUTPUT.PUT_LINE('Insufficient stock for the requested MedicineID');
188  END;
189  /

Trigger created.

SQL> 
SQL> 
SQL> 
SQL> CREATE OR REPLACE PROCEDURE UpdateTransactionsReq (p_request_id NUMBER) AS
  2  	 v_invoice_amount DECIMAL(10, 2);
  3  	 v_charge_per_package DECIMAL(10, 2);
  4  	 v_existing_transaction_id NUMBER;
  5  BEGIN
  6  	 -- Fetch ChargePerPackage
  7  	 SELECT p.ChargePerPackage
  8  	 INTO v_charge_per_package
  9  	 FROM Providers p
 10  	 INNER JOIN Request_Details rd ON rd.ProviderID = p.ProviderID
 11  	 WHERE rd.RequestID = p_request_id;
 12  
 13  	 -- Calculate invoice amount
 14  	 SELECT SUM(cr.Quantity * v_charge_per_package)
 15  	 INTO v_invoice_amount
 16  	 FROM Collection_Requests cr
 17  	 WHERE cr.RequestID = p_request_id;
 18  
 19  	 -- Check if there is an existing transaction for the RequestID with status "Pending"
 20  	 BEGIN
 21  	     SELECT TransactionID
 22  	     INTO v_existing_transaction_id
 23  	     FROM Transactions
 24  	     WHERE RequestID = p_request_id
 25  	     AND OrderStatus = 'Pending';
 26  	 EXCEPTION
 27  	     WHEN NO_DATA_FOUND THEN
 28  		 v_existing_transaction_id := NULL;
 29  	 END;
 30  
 31  	 IF v_existing_transaction_id IS NOT NULL THEN
 32  	     -- Update the existing transaction with the new invoice amount
 33  	     UPDATE Transactions
 34  	     SET InvoiceAmount = InvoiceAmount + v_invoice_amount
 35  	     WHERE TransactionID = v_existing_transaction_id;
 36  	 ELSE
 37  	     -- Insert a new transaction with the calculated invoice amount
 38  	     INSERT INTO Transactions (TransactionID, OrderID, RequestID, InvoiceAmount, OrderStatus)
 39  	     VALUES (TranscSeq.NEXTVAL, NULL, p_request_id, v_invoice_amount, 'Pending');
 40  	 END IF;
 41  END;
 42  /

Procedure created.

SQL> 
SQL> 
SQL> CREATE OR REPLACE PROCEDURE ProcessExpiredMedicinesBatch (
  2  	 p_provider_name IN VARCHAR2
  3  ) AS
  4  	 v_current_date DATE := SYSDATE;
  5  	 v_scheduled_pickup_date DATE;
  6  	 v_request_id NUMBER;
  7  	 v_expiry_date DATE;
  8  	 v_med_name VARCHAR2(255);
  9  	 v_new_request_id NUMBER;
 10  BEGIN
 11  	 -- Get the current date
 12  	 v_current_date := SYSDATE;
 13  	 -- Set the scheduled pickup date to 2 days from the current date
 14  	 v_scheduled_pickup_date := v_current_date + 2;
 15  
 16  	 -- Process expired medicines from batches
 17  	 FOR batch_row IN (SELECT BatchID, MedicineID, PackagesManufactured, ManufacturerID, ManufacturedDate
 18  			   FROM Batch)
 19  	 LOOP
 20  	     -- Get the MedName from the Dosage table
 21  	     SELECT MedName
 22  	     INTO v_med_name
 23  	     FROM Dosage
 24  	     WHERE MedicineID = batch_row.MedicineID;
 25  
 26  	     -- Calculate the expiry date for each batch
 27  	     SELECT ADD_MONTHS(batch_row.ManufacturedDate, ExpiryFromManufacture)
 28  	     INTO v_expiry_date
 29  	     FROM Medicine
 30  	     WHERE MedName = v_med_name;
 31  
 32  	     -- Check if the batch is expired
 33  	     IF v_expiry_date < v_current_date THEN
 34  		 -- Check if a request already exists with 'N' status for the same Manufacturer
 35  		 BEGIN
 36  		     SELECT RequestID
 37  		     INTO v_request_id
 38  		     FROM Request_Details
 39  		     WHERE ManufacturerID = batch_row.ManufacturerID
 40  		     AND Status = 'N'
 41  		     AND ROWNUM = 1;
 42  
 43  		 EXCEPTION
 44  		     WHEN NO_DATA_FOUND THEN
 45  			 v_request_id := NULL;
 46  		 END;
 47  
 48  		 IF v_request_id IS NULL THEN
 49  		     -- No existing request found or existing request found with 'Y' status, generate a new RequestID
 50  		     SELECT RequestSeq.NEXTVAL INTO v_new_request_id FROM DUAL;
 51  
 52  		     -- Create a request for waste collection
 53  		     INSERT INTO Request_Details (RequestID, ManufacturerID, ProviderID, Scheduled_pickup_date, Status)
 54  		     VALUES (
 55  			 v_new_request_id,
 56  			 batch_row.ManufacturerID,
 57  			 (SELECT ProviderID FROM Providers WHERE Name = p_provider_name),
 58  			 v_scheduled_pickup_date, -- Using calculated pickup date
 59  			 'N'
 60  		     );
 61  		 ELSE
 62  		     -- Existing request with 'N' status found, reuse the same RequestID
 63  		     v_new_request_id := v_request_id;
 64  		 END IF;
 65  
 66  		 -- Create a collection request
 67  		 INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
 68  		 VALUES (v_new_request_id, batch_row.MedicineID, batch_row.PackagesManufactured);
 69  
 70  		 -- Call UpdateTransactions procedure to handle the transaction
 71  		 UpdateTransactionsReq(v_new_request_id);
 72  	     END IF;
 73  	 END LOOP;
 74  END;
 75  /

Procedure created.

SQL> 
SQL> 
SQL> CREATE OR REPLACE PROCEDURE ProcessExpiredMedicinesInv (
  2  	 p_provider_name IN VARCHAR2,
  3  	 p_wholesaler_id IN NUMBER
  4  ) AS
  5  	 v_current_date DATE := SYSDATE;
  6  	 v_expiry_date DATE;
  7  	 v_request_id NUMBER;
  8  	 v_manufactured_date DATE;
  9  	 v_scheduled_pickup_date DATE;
 10  	 v_new_request_id NUMBER;
 11  BEGIN
 12  	 -- Calculate the scheduled pickup date as 2 days from the current date
 13  	 v_scheduled_pickup_date := v_current_date + 2;
 14  
 15  	 -- Process expired medicines from Inventory
 16  	 FOR inventory_row IN (SELECT i.WholesalerID, i.BatchID, i.QuantityInStock, b.MedicineID
 17  			       FROM Inventory i
 18  			       JOIN Batch b ON i.BatchID = b.BatchID
 19  			       WHERE i.WholesalerID = p_wholesaler_id) -- Joined with Batch table to get MedicineID
 20  	 LOOP
 21  	     -- Get ManufacturedDate from Batch
 22  	     SELECT ManufacturedDate
 23  	     INTO v_manufactured_date
 24  	     FROM Batch
 25  	     WHERE BatchID = inventory_row.BatchID;
 26  
 27  	     -- Calculate the expiry date for each inventory
 28  	     SELECT ADD_MONTHS(v_manufactured_date, m.ExpiryFromManufacture) -- Used v_manufactured_date and m.ExpiryFromManufacture
 29  	     INTO v_expiry_date
 30  	     FROM Medicine m
 31  	     WHERE m.MedName = (
 32  		 SELECT MedName
 33  		 FROM Dosage
 34  		 WHERE MedicineID = inventory_row.MedicineID
 35  	     );
 36  
 37  	     -- Check if the inventory is expired
 38  	     IF v_expiry_date < v_current_date THEN
 39  		 -- Check if a request already exists with 'N' status for the same Pharmacy/Wholesaler/Manufacturer
 40  		 BEGIN
 41  		     SELECT RequestID
 42  		     INTO v_request_id
 43  		     FROM Request_Details
 44  		     WHERE WholesalerID = inventory_row.WholesalerID
 45  		     AND Status = 'N'
 46  		     AND ROWNUM = 1;
 47  
 48  		 EXCEPTION
 49  		     WHEN NO_DATA_FOUND THEN
 50  			 v_request_id := NULL;
 51  		 END;
 52  
 53  		 IF v_request_id IS NULL THEN
 54  		     -- No existing request found or existing request found with 'Y' status, generate a new RequestID
 55  		     SELECT RequestSeq.NEXTVAL INTO v_new_request_id FROM DUAL;
 56  
 57  		     -- Create a request for waste collection
 58  		     INSERT INTO Request_Details (RequestID, WholesalerID, ProviderID, Scheduled_pickup_date, Status)
 59  		     VALUES (
 60  			 v_new_request_id,
 61  			 inventory_row.WholesalerID,
 62  			 (SELECT ProviderID FROM Providers WHERE Name = p_provider_name),
 63  			 v_scheduled_pickup_date, -- Using calculated pickup date
 64  			 'N'
 65  		     );
 66  		 ELSE
 67  		     -- Existing request with 'N' status found, reuse the same RequestID
 68  		     v_new_request_id := v_request_id;
 69  		 END IF;
 70  
 71  		 -- Create a collection request
 72  		 INSERT INTO Collection_Requests (RequestID, WasteType, Quantity)
 73  		 VALUES (v_new_request_id, inventory_row.MedicineID, inventory_row.QuantityInStock);
 74  
 75  		 -- Call UpdateTransactions procedure to handle the transaction
 76  		 UpdateTransactionsReq(v_new_request_id);
 77  	     END IF;
 78  	 END LOOP;
 79  END;
 80  /

Procedure created.

SQL> 
SQL> 
SQL> 
SQL> CREATE OR REPLACE PROCEDURE UpdateTransactionsOrder (
  2  	 p_order_id IN NUMBER
  3  )
  4  AS
  5  BEGIN
  6  	 -- Insert into Transactions
  7  	 INSERT INTO Transactions (TransactionID, OrderID, RequestID, InvoiceAmount, OrderStatus)
  8  	 VALUES (TranscSeq.NEXTVAL, p_order_id, NULL, 0, 'Pending');
  9  END;
 10  /

Procedure created.

SQL> 
SQL> CREATE OR REPLACE PROCEDURE UpdateOrderedMedicinePrice (
  2  	 p_order_id IN NUMBER,
  3  	 p_medicine_list IN SYS.ODCINUMBERLIST,
  4  	 v_ordered_by_pharmacy_id NUMBER,
  5  	 v_ordered_by_wholesaler_id NUMBER,
  6  	 v_ordered_from_wholesaler_id NUMBER,
  7  	 v_ordered_from_manufacturer_id NUMBER
  8  )
  9  AS
 10  	 v_price_per_package DECIMAL(10, 2);
 11  BEGIN
 12  	 -- Loop through each medicine ID in the list
 13  	 FOR i IN 1..p_medicine_list.COUNT LOOP
 14  	     BEGIN
 15  		 -- Determine whether to get the price per package from ManufacturePrice or Inventory
 16  		 IF v_ordered_from_manufacturer_id IS NOT NULL THEN
 17  		     -- Get price per package from ManufacturePrice table
 18  		     SELECT PricePerPackage INTO v_price_per_package
 19  		     FROM ManufacturePrice
 20  		     WHERE MedicineID = p_medicine_list(i) AND ManufacturerID = v_ordered_from_manufacturer_id;
 21  		 ELSE
 22  		     -- Get price per package from Inventory table
 23  		     SELECT i.PricePerPackage INTO v_price_per_package
 24  		     FROM Inventory i
 25  		     JOIN Batch b ON i.BatchID = b.BatchID
 26  		     WHERE b.MedicineID = p_medicine_list(i) AND WholesalerID = v_ordered_from_wholesaler_id;
 27  		 END IF;
 28  	     EXCEPTION
 29  		 WHEN NO_DATA_FOUND THEN
 30  		     -- Set price per package to NULL if not found
 31  		     v_price_per_package := NULL;
 32  		     DBMS_OUTPUT.PUT_LINE('No data found for MedicineID: ' || p_medicine_list(i) || ' in UpdateOrderedMedicinePrice for order ID: ' || p_order_id);
 33  	     END;
 34  
 35  	     -- Update Ordered_Medicine table with the found price or NULL
 36  	     IF v_price_per_package IS NOT NULL THEN
 37  		 UPDATE Ordered_Medicine
 38  		 SET PricePerPackage = v_price_per_package
 39  		 WHERE OrderID = p_order_id AND MedicineID = p_medicine_list(i);
 40  	     END IF;
 41  	 END LOOP;
 42  END;
 43  /

Procedure created.

SQL> 
SQL> CREATE OR REPLACE PROCEDURE PlaceOrder (
  2  	 p_order_date IN DATE,
  3  	 p_who IN VARCHAR2,
  4  	 p_who_name IN VARCHAR2,
  5  	 p_from IN VARCHAR2,
  6  	 p_from_name IN VARCHAR2,
  7  	 p_medicine_list IN SYS.ODCINUMBERLIST,
  8  	 p_packages_ordered_list IN SYS.ODCINUMBERLIST
  9  ) AS
 10  	 v_order_id NUMBER;
 11  	 v_ordered_by_pharmacy_id NUMBER;
 12  	 v_ordered_by_wholesaler_id NUMBER;
 13  	 v_ordered_from_wholesaler_id NUMBER;
 14  	 v_ordered_from_manufacturer_id NUMBER;
 15  BEGIN
 16  	 -- Determine who is placing the order and set the respective IDs
 17  	 IF p_who = 'Pharmacy' THEN
 18  	     SELECT PharmacyID INTO v_ordered_by_pharmacy_id
 19  	     FROM Pharmacy
 20  	     WHERE Name = p_who_name;
 21  	     v_ordered_by_wholesaler_id := NULL;
 22  	 ELSE
 23  	     SELECT WholesalerID INTO v_ordered_by_wholesaler_id
 24  	     FROM Wholesalers
 25  	     WHERE Name = p_who_name;
 26  	     v_ordered_by_pharmacy_id := NULL;
 27  	 END IF;
 28  
 29  	 -- Determine from where the order is placed and set the respective IDs
 30  	 IF p_from = 'Wholesaler' THEN
 31  	     SELECT WholesalerID INTO v_ordered_from_wholesaler_id
 32  	     FROM Wholesalers
 33  	     WHERE Name = p_from_name;
 34  	     v_ordered_from_manufacturer_id := NULL;
 35  	 ELSE
 36  	     SELECT ManufacturerID INTO v_ordered_from_manufacturer_id
 37  	     FROM Manufacturers
 38  	     WHERE Name = p_from_name;
 39  	     v_ordered_from_wholesaler_id := NULL;
 40  	 END IF;
 41  
 42  	 -- Start transaction
 43  	 BEGIN
 44  	     -- Populate Order_details table
 45  	     INSERT INTO Order_details (OrderID, OrderDate, OrderedByPharmacy, OrderedByWholesaler, OrderedFromWholesaler, OrderedFromManufacturer, SupplyStatus)
 46  	     VALUES (OrderSeq.NEXTVAL, p_order_date, v_ordered_by_pharmacy_id, v_ordered_by_wholesaler_id, v_ordered_from_wholesaler_id, v_ordered_from_manufacturer_id, 'Pending')
 47  	     RETURNING OrderID INTO v_order_id;
 48  
 49  	     -- Update transaction
 50  	     UpdateTransactionsOrder(v_order_id);
 51  
 52  	     -- Populate Ordered_Medicine table
 53  	     FOR i IN 1..p_medicine_list.COUNT LOOP
 54  		 INSERT INTO Ordered_Medicine (OrderID, MedicineID, PackagesOrdered, PricePerPackage)
 55  		 VALUES (v_order_id, p_medicine_list(i), p_packages_ordered_list(i), 1);
 56  	     END LOOP;
 57  
 58  	     -- Commit transaction
 59  	     COMMIT;
 60  
 61  	     -- Update OrderedMedicinePrice
 62  	     BEGIN
 63  		 UpdateOrderedMedicinePrice(
 64  		     v_order_id,
 65  		     p_medicine_list,
 66  		     v_ordered_by_pharmacy_id,
 67  		     v_ordered_by_wholesaler_id,
 68  		     v_ordered_from_wholesaler_id,
 69  		     v_ordered_from_manufacturer_id
 70  		 );
 71  	     EXCEPTION
 72  		 WHEN NO_DATA_FOUND THEN
 73  		     DBMS_OUTPUT.PUT_LINE('No data found in UpdateOrderedMedicinePrice for order ID: ' || v_order_id);
 74  		     -- Handle exception as needed
 75  	     END;
 76  
 77  	 COMMIT;
 78  
 79  	 END;
 80  END;
 81  /

Procedure created.

SQL> 
SQL> 
SQL> 
SQL> 
SQL> BEGIN
  2  	 PlaceOrder(
  3  	     TO_DATE('2024-06-15', 'YYYY-MM-DD'), -- OrderDate
  4  	     'Pharmacy', -- Who
  5  	     'CureWell Pharmacy', -- WhoName
  6  	     'Wholesaler', -- From
  7  	     'HealthLink Solutions', -- FromName
  8  	     SYS.ODCINUMBERLIST(1, 2, 3), -- List of MedicineID
  9  	     SYS.ODCINUMBERLIST(1, 2, 3) -- List of PackagesOrdered
 10  	 );
 11  END;
 12  /

PL/SQL procedure successfully completed.

SQL> 
SQL> BEGIN
  2  	 PlaceOrder(
  3  	     TO_DATE('2024-06-15', 'YYYY-MM-DD'), -- OrderDate
  4  	     'Wholesaler', -- Who
  5  	     'HealthLink Solutions', -- WhoName
  6  	     'Manufacturer', -- From
  7  	     'PharmaTech Inc.', -- FromName
  8  	     SYS.ODCINUMBERLIST(1, 2, 3), -- List of MedicineID
  9  	     SYS.ODCINUMBERLIST(1, 2, 3) -- List of PackagesOrdered
 10  	 );
 11  END;
 12  /

PL/SQL procedure successfully completed.

SQL> 
SQL> 
SQL> CREATE OR REPLACE PROCEDURE ClearCollectedMedicinesInv(
  2  	 p_RequestID IN NUMBER
  3  )
  4  AS
  5  	 v_WholesalerID NUMBER;
  6  	 CURSOR medicine_cursor IS
  7  	     SELECT WasteType
  8  	     FROM Collection_Requests
  9  	     WHERE RequestID = p_RequestID;
 10  
 11  	 TYPE MedicineTableType IS TABLE OF Collection_Requests.WasteType%TYPE;
 12  	 v_MedicineTable MedicineTableType;
 13  
 14  	 v_BatchID NUMBER;
 15  BEGIN
 16  	 -- Find the associated WholesalerID
 17  	 SELECT WholesalerID
 18  	 INTO v_WholesalerID
 19  	 FROM Request_Details
 20  	 WHERE RequestID = p_RequestID;
 21  
 22  	 -- Check if WholesalerID is found
 23  	 IF v_WholesalerID IS NULL THEN
 24  	     RAISE_APPLICATION_ERROR(-20001, 'No WholesalerID found for the given RequestID.');
 25  	 END IF;
 26  
 27  	 -- Fetch all associated MedicineID values
 28  	 OPEN medicine_cursor;
 29  	 FETCH medicine_cursor BULK COLLECT INTO v_MedicineTable;
 30  	 CLOSE medicine_cursor;
 31  
 32  	 -- Iterate through each MedicineID and find the corresponding BatchID
 33  	 FOR i IN v_MedicineTable.FIRST..v_MedicineTable.LAST LOOP
 34  	     SELECT BatchID
 35  	     INTO v_BatchID
 36  	     FROM Batch
 37  	     WHERE MedicineID = v_MedicineTable(i);
 38  
 39  	     -- Delete the row from Inventory
 40  	     DELETE FROM Inventory
 41  	     WHERE WholesalerID = v_WholesalerID
 42  	       AND BatchID = v_BatchID;
 43  	 END LOOP;
 44  
 45  	 -- Commit the transaction
 46  	 COMMIT;
 47  EXCEPTION
 48  	 WHEN NO_DATA_FOUND THEN
 49  	     RAISE_APPLICATION_ERROR(-20002, 'No matching data found.');
 50  	 WHEN OTHERS THEN
 51  	     ROLLBACK;
 52  	     RAISE;
 53  END ClearCollectedMedicinesInv;
 54  /

Procedure created.

SQL> select * from batch where manufacturerid =1;

   BATCHID MANUFACTURERID MEDICINEID PACKAGESMANUFACTURED MANUFACTU             
---------- -------------- ---------- -------------------- ---------             
QUANTITYPERPACKAGE                                                              
------------------                                                              
         1              1          1                  999 01-JUN-24             
                50                                                              
                                                                                
         2              1          2                    1 15-MAY-24             
                40                                                              
                                                                                
         3              1          3                 1197 10-MAY-24             
                60                                                              
                                                                                

SQL> select * from inventory where wholesalerid = 2;

WHOLESALERID    BATCHID QUANTITYINSTOCK PRICEPERPACKAGE                         
------------ ---------- --------------- ---------------                         
           2          2             748              30                         
           2          7             720              32                         
           2          5             700              31                         
           2          3             740            32.5                         
           2          4             630            23.5                         
           2          1               1              10                         

6 rows selected.

SQL> SPOOL OFF
