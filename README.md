# 🛒 Inventory & Order Management API

A Spring Boot REST API for managing products, categories, orders, and users with JWT-based authentication and role-based access control.

---

## 🚀 Features

### 👤 Authentication & Security
- JWT Authentication
- Role-based access control (ADMIN / USER)
- Secure endpoints with Spring Security

### 📦 Product Management
- Create, update, delete products
- Bulk product creation
- Assign multiple categories to products
- Activate / deactivate products
- Stock management

### 🏷️ Category Management
- Create, update, delete categories (Admin only)
- View all categories
- Many-to-many relationship with products

### 📊 Order Management
- Create orders
- View user orders
- Order status tracking (CONFIRMED, etc.)
- Order history per user

### 🔍 Filtering & Search
- Search products by name
- Filter by category
- Low stock products
- Pagination support
- Sorting support

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL
- Maven

---

## 📂 Project Structure
