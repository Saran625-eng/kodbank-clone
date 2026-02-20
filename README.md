# Kodbank - Banking Application

A full-stack Spring Boot banking application with JWT authentication, MySQL database, and responsive frontend.

## Features

- **User Registration**: Create account with default balance of $100,000
- **Secure Login**: JWT-based authentication with BCrypt password encryption
- **Balance Check**: View account balance with animated display and confetti effect
- **Token Management**: JWT tokens stored in cookies and database

## Tech Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Database**: MySQL (Aiven)
- **Security**: Spring Security, JWT
- **Frontend**: HTML, CSS, JavaScript

## Project Structure

```
kodbank/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── kodbank/
│       │           ├── KodbankApplication.java
│       │           ├── config/
│       │           │   └── WebConfig.java
│       │           ├── controller/
│       │           │   ├── AuthController.java
│       │           │   └── UserController.java
│       │           ├── dto/
│       │           │   ├── AuthResponse.java
│       │           │   ├── BalanceResponse.java
│       │           │   ├── LoginRequest.java
│       │           │   └── RegisterRequest.java
│       │           ├── entity/
│       │           │   ├── KodUser.java
│       │           │   └── UserToken.java
│       │           ├── exception/
│       │           │   └── GlobalExceptionHandler.java
│       │           ├── repository/
│       │           │   ├── KodUserRepository.java
│       │           │   └── UserTokenRepository.java
│       │           ├── security/
│       │           │   ├── JwtAuthenticationFilter.java
│       │           │   └── SecurityConfig.java
│       │           └── service/
│       │               ├── AuthService.java
│       │               ├── JwtService.java
│       │               └── UserService.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── index.html
│               ├── login.html
│               ├── register.html
│               └── dashboard.html
```

## Database Configuration

The application uses MySQL. Configure your database connection in `src/main/resources/application.properties`:

```
properties
# MySQL Database Configuration (Aiven)
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:kodbank}?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}
```

You can set these environment variables or use the default values.

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL Database

### Build and Run

```
bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |

### User

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/user/balance` | Get user balance (requires auth) |

## Security Features

- BCrypt password encryption
- JWT token authentication
- Token stored in database with expiry
- Token validation on every request
- Protected endpoints require valid JWT

## Default Configuration

- **Default Balance**: $100,000
- **Default Role**: CUSTOMER
- **JWT Expiration**: 24 hours (86400000 ms)
- **JWT Secret**: Configurable via `jwt.secret` property
