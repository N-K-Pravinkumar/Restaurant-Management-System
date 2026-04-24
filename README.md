**Restaurant Management System (Full Stack)
**
A full stack Restaurant Management System built using Java, Spring Boot, Microservices, and Angular to streamline restaurant operations such as order management, billing, and customer workflows.

This project demonstrates scalable backend architecture, secure API design, and real-world full stack development.

**Features
**
Order Management (Create, Update, Delete orders)
Billing System with real-time calculation
Menu Management
Kitchen Order Tracking
Role-Based Access Control (Admin, Manager, Staff)
Modular and scalable microservices architecture

**Architecture
**
Backend developed using Spring Boot (REST APIs)
Microservices-based modular design
Frontend built using Angular
Secure authentication using JWT + Spring Security
Database integration with MySQL

**Tech Stack
**
**Backend:
**
Java
Spring Boot
Microservices
REST APIs
Spring Security (JWT)
MySQL

**Frontend:
**
Angular
HTML
CSS

**Tools:
**
Git & GitHub
Postman (API Testing)
Maven

Setup Instructions

**Clone Repository
**
git clone https://github.com/N-K-Pravinkumar/Restaurant-Management-System.git

cd Restaurant-Management-System

**Backend Setup
**
cd backend
mvn clean install
mvn spring-boot:run

Frontend Setup

cd frontend
npm install
ng serve

**Database Setup
**
Install MySQL
Create database:

CREATE DATABASE restaurant_db;

Update database credentials in application.properties

**API Endpoints (Sample)
**
GET /menu → Fetch menu
POST /orders → Create order
GET /orders → Get all orders
DELETE /orders/{id} → Delete order

**Key Highlights
**
Built 10+ REST APIs for restaurant workflows
Implemented JWT authentication with RBAC
Designed microservices architecture for scalability
Optimized database queries for high concurrency performance
Integrated Angular frontend with backend APIs

**Author
**
Pravinkumar N K
Software Engineer | Java | Spring Boot | Angular

**Acknowledgements
**
Inspired by real-world restaurant operations
Built as a hands-on full stack learning project
