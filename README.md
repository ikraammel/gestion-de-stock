# Stock Management System

A full-stack inventory management application built with **Angular 20** and **Spring Boot 3**, designed to manage products, stock movements, suppliers, customers, purchasing, and sales from a centralized interface.

## Key Features

- **Product and category management:** Create, update, and consult articles and product categories.
- **Inventory tracking:** Manage stock movements and consult stock-related information.
- **Customer and supplier management:** Maintain customer and supplier records.
- **Order management:** Handle customer orders and supplier purchase orders.
- **Sales management:** Record and manage sales.
- **User accounts and authentication:** Login, registration, password changes, and JWT-based security.
- **Dashboard and statistics:** Dedicated interface components for overview and reporting.
- **Image uploads:** Support for article and entity photos.
- **API documentation:** OpenAPI/Swagger support and generated Angular API clients.

## Tech Stack

| Component | Technologies |
| --- | --- |
| Frontend | Angular 20, TypeScript, SCSS, Bootstrap 5 |
| Backend | Java 17, Spring Boot 3.5.5, Spring Web, Spring Data JPA |
| Security | Spring Security, JWT |
| Database | MySQL |
| API tooling | Springdoc OpenAPI, Swagger, OpenAPI Generator |
| Build tools | Maven, npm, Angular CLI |

## Project Structure

```text
gestion-de-stock/
├── gds-backend/
│   ├── pom.xml
│   └── src/main/java/com/ikram/gestiondestock/
│       ├── auth/          # Authentication
│       ├── config/        # Security and application configuration
│       ├── controller/    # REST endpoints
│       ├── dto/           # Data transfer objects
│       ├── model/         # JPA entities
│       ├── repository/    # Persistence
│       ├── services/      # Business logic
│       └── Validator/     # Input validation
└── gds-frontend/
    ├── src/app/           # Angular pages, components and services
    ├── src/gs-api/        # Generated API client
    └── package.json
```

## Getting Started

### Requirements

- Java 17
- Node.js and npm (compatible with Angular 20)
- MySQL
- Maven wrapper (included)

### 1. Clone the repository

```bash
git clone https://github.com/ikraammel/gestion-de-stock.git
cd gestion-de-stock
```

### 2. Configure and run the backend

Create a MySQL database named `gestion_stock`. Configure your own database credentials locally in `gds-backend/src/main/resources/application.yml`, without committing secrets.

```bash
cd gds-backend
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`.

The backend is configured to use port **8081**. When running, the OpenAPI definition can be accessed at `http://localhost:8081/v3/api-docs` (subject to security configuration).

### 3. Run the frontend

From a separate terminal:

```bash
cd gds-frontend
npm install
npm start
```

The Angular development server normally runs at `http://localhost:4200`. Configure API connectivity to your backend as needed.

## API Integration

The frontend includes generated TypeScript Angular API clients in `gds-frontend/src/gs-api/` and npm scripts for downloading the backend OpenAPI specification and regenerating the client.

## Reference

Project concepts were informed by the [Spring Boot & Angular stock-management tutorial series](https://www.youtube.com/watch?v=d5jCDvBYZUI&list=PL41m5U3u3wwlI59Jt6K2cyG2oKFbFJFQU). The features documented above are based on this repository's source code.
