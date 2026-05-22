CREATE DATABASE IF NOT EXISTS cnpm;
USE cnpm;
-- ==========================================
-- 1. DROP TABLES IF THEY EXIST (For Clean Setup)
-- ==========================================
DROP TABLE IF EXISTS tbl_DetailedInvoiceMaterial;
DROP TABLE IF EXISTS tbl_DetailedInvoiceService;
DROP TABLE IF EXISTS tbl_DetailedInvoice;
DROP TABLE IF EXISTS tbl_CustomerSlot;
DROP TABLE IF EXISTS tbl_Slot;
DROP TABLE IF EXISTS tbl_User;
DROP TABLE IF EXISTS tbl_Customer;
DROP TABLE IF EXISTS tbl_Material;
DROP TABLE IF EXISTS tbl_Service;
DROP TABLE IF EXISTS tbl_ServiceStaff;

-- ==========================================
-- 2. CREATE TABLES
-- ==========================================

CREATE TABLE tbl_User (
    id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL
);

CREATE TABLE tbl_Slot (
    id INT PRIMARY KEY,
    idtblUser INT,
    FOREIGN KEY (idtblUser) REFERENCES tbl_User(id)
);

CREATE TABLE tbl_Customer (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

CREATE TABLE tbl_CustomerSlot (
    id INT PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    idtblSlot INT,
    idtblCustomer INT,
    FOREIGN KEY (idtblSlot) REFERENCES tbl_Slot(id),
    FOREIGN KEY (idtblCustomer) REFERENCES tbl_Customer(id)
);

CREATE TABLE tbl_DetailedInvoice (
    id INT PRIMARY KEY,
    date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    idtblCustomerSlot INT,
    FOREIGN KEY (idtblCustomerSlot) REFERENCES tbl_CustomerSlot(id)
);

CREATE TABLE tbl_Material (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    unitPrice INT NOT NULL
);

CREATE TABLE tbl_DetailedInvoiceMaterial (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    price INT NOT NULL,
    idtblMaterial INT,
    idtblDetailedInvoice INT,
    FOREIGN KEY (idtblMaterial) REFERENCES tbl_Material(id),
    FOREIGN KEY (idtblDetailedInvoice) REFERENCES tbl_DetailedInvoice(id)
);

CREATE TABLE tbl_Service (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    unitPrice INT NOT NULL
);

CREATE TABLE tbl_ServiceStaff (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL -- Added status column as requested
);

CREATE TABLE tbl_DetailedInvoiceService (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price INT NOT NULL,
    idtblService INT,
    idtblDetailedInvoice INT,
    idtblServiceStaff INT,
    FOREIGN KEY (idtblService) REFERENCES tbl_Service(id),
    FOREIGN KEY (idtblDetailedInvoice) REFERENCES tbl_DetailedInvoice(id),
    FOREIGN KEY (idtblServiceStaff) REFERENCES tbl_ServiceStaff(id)
);

-- ==========================================
-- 3. INSERT INITIAL DATA
-- ==========================================

-- tbl_User
INSERT INTO tbl_User (id, username, password, fullName, position) VALUES
(1, 'vietbnh', '123456', 'Bui Nguyen Hoang Viet', 'Receptionist');

-- tbl_Slot
INSERT INTO tbl_Slot (id, idtblUser) VALUES
(1, 1),
(2, 1),
(3, 1);

-- tbl_Customer
INSERT INTO tbl_Customer (id, name, phone) VALUES
(1, 'Nguyen Van A', '0123456789'),
(2, 'Tran Van B', '0128437283'),
(3, 'John Smith', '0128472634');

-- tbl_CustomerSlot
INSERT INTO tbl_CustomerSlot (id, idtblSlot, idtblCustomer, status) VALUES
(1, 1, 1, 'Pending'),
(2, 2, 2, 'Finished'),
(3, 3, 3, 'Reserved');

-- tbl_DetailedInvoice (Dates formatted as YYYY-MM-DD for SQL compatibility)
INSERT INTO tbl_DetailedInvoice (id, date, status, idtblCustomerSlot) VALUES
(1, CURDATE(), 'Pending', 1),  -- <-- UPDATED HERE to use current date
(2, '2026-04-28', 'Paid', 2);

-- Material
INSERT INTO tbl_Material (id, name, unitPrice) VALUES
(1, 'Hair gel', 20000),
(2, 'Hair perm product', 30000);

-- tbl_DetailedInvoiceMaterial
INSERT INTO tbl_DetailedInvoiceMaterial (id, quantity, price, idtblDetailedInvoice, idtblMaterial) VALUES
(1, 2, 20000, 1, 1),
(2, 1, 20000, 1, 2),
(3, 1, 30000, 2, 2);

-- Service
INSERT INTO tbl_Service (id, name, unitPrice) VALUES
(1, 'Hair perm', 30000),
(2, 'Hair wash', 20000);

-- ServiceStaff (Populated with 'Active' status)
INSERT INTO tbl_ServiceStaff (id, name, phone, status) VALUES
(1, 'Tran Van C', '0246813579', 'Active'),
(2, 'Nguyen Van D', '0127374829', 'Active');

-- tbl_DetailedInvoiceService
INSERT INTO tbl_DetailedInvoiceService (id, price, idtblService, idtblDetailedInvoice, idtblServiceStaff) VALUES
(1, 30000, 1, 1, 1),
(2, 20000, 2, 1, 1),
(3, 30000, 1, 2, 1),
(4, 30000, 1, 2, 2);