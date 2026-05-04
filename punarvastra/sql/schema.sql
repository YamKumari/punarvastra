DROP DATABASE IF EXISTS punarvastra;
CREATE DATABASE punarvastra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE punarvastra;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(20) UNIQUE NOT NULL,
                       address TEXT,
                       role ENUM('USER','ADMIN') DEFAULT 'USER',
                       approved BOOLEAN DEFAULT TRUE,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       INDEX idx_users_email (email),
                       INDEX idx_users_phone (phone)
);

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) UNIQUE NOT NULL,
                            slug VARCHAR(50) UNIQUE NOT NULL,
                            description TEXT
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          seller_id INT NULL,
                          title VARCHAR(150) NOT NULL,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                          size VARCHAR(20),
                          product_condition VARCHAR(30),
                          brand VARCHAR(50),
                          image VARCHAR(255),
                          stock INT DEFAULT 1 CHECK (stock >= 0),
                          category_id INT,
                          listing_status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
                          FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE SET NULL,
                          INDEX idx_products_category (category_id),
                          INDEX idx_products_title (title),
                          INDEX idx_products_listing (listing_status),
                          INDEX idx_products_seller (seller_id)
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
                        shipping_address TEXT NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        payment_method ENUM('COD','ESEWA') DEFAULT 'COD',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        INDEX idx_orders_user (user_id),
                        INDEX idx_orders_status (status)
);

CREATE TABLE order_items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT NOT NULL,
                             product_id INT NOT NULL,
                             quantity INT NOT NULL CHECK (quantity > 0),
                             price_at_purchase DECIMAL(10,2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE inquiries (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL,
                           subject VARCHAR(150) NOT NULL,
                           message TEXT NOT NULL,
                           is_read BOOLEAN DEFAULT FALSE,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
