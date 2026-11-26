# Payment Management System

A backend payment-workflow simulation built using Java 17, Spring Boot, Spring Security, Spring Data JPA (Hibernate), MySQL, and REST APIs.

> **Note:** This is a learning and portfolio project. It simulates payment workflows and does not connect to banks, payment gateways, or real-money payment networks.

## Features

- **User Registration:** Register users with BCrypt password hashing.
- **Authentication:** HTTP Basic authentication using Spring Security.
- **Role-Based Authorization:** Separate permissions for `USER` and `ADMIN` roles.
- **Account Management:** Create and view accounts belonging to the authenticated user.
- **Transaction Limits:** Configure transaction limits based on username, account ID, or payment type.
- **Payment Initiation:** Initiate simulated transfers between accounts.
- **Approval Workflow:** Automatically send payments requiring approval to `PENDING_APPROVAL`.
- **Admin Payment Management:** View all payments and approve or reject pending transactions.
- **Balance Management:** Update account balances after successful transfers.
- **Validation and Exception Handling:** Validate incoming requests and return centralized API error responses.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot 3 | Application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database persistence |
| Hibernate | ORM |
| MySQL 8 | Relational database |
| Maven | Build and dependency management |
| Postman | API testing |

## Architecture

The application follows a layered architecture:

**Controller → Service → Repository → MySQL**

- **Controller Layer:** Handles REST API requests and responses.
- **Service Layer:** Implements business logic, account ownership checks, transaction limits, and approval workflows.
- **Repository Layer:** Handles database operations using Spring Data JPA.
- **Entity Layer:** Represents users, accounts, transaction limits, and payments.
- **Security Layer:** Handles authentication and role-based access control.

## Payment Workflow

1. A registered user initiates a payment.
2. The system validates account ownership, account status, and available balance.
3. Applicable transaction limit rules are checked.
4. If the payment exceeds a configured maximum limit, the request is rejected.
5. If the payment reaches an approval threshold, it is marked `PENDING_APPROVAL`.
6. Otherwise, the payment is completed immediately and account balances are updated.
7. An administrator can approve or reject pending payments.
8. Approved payments update account balances, while rejected payments do not.

### Payment Statuses

| Status | Description |
|---|---|
| `PENDING_APPROVAL` | Waiting for administrator review |
| `COMPLETED` | Payment successfully processed |
| `REJECTED` | Payment rejected by administrator |

## Prerequisites

- JDK 17 or later
- Maven 3.9 or later
- MySQL 8 or later
- Postman (for API testing)

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/TVHARSHAVARDHAN/payment-management-system.git
cd payment-management-system
```

### 2. Create the database

Run the following SQL command in MySQL Workbench:

```sql
CREATE DATABASE payment_management;
```

### 3. Configure environment variables

Configure your database credentials and administrator password before starting the application.

**Windows PowerShell:**

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/payment_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"

$env:APP_ADMIN_USERNAME="admin"
$env:APP_ADMIN_PASSWORD="YOUR_STRONG_ADMIN_PASSWORD"
```

**macOS/Linux:**

```bash
export DB_URL="jdbc:mysql://localhost:3306/payment_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="YOUR_MYSQL_PASSWORD"

export APP_ADMIN_USERNAME="admin"
export APP_ADMIN_PASSWORD="YOUR_STRONG_ADMIN_PASSWORD"
```

Do not commit database credentials or administrator passwords to GitHub.

### 4. Build and run the application

From the directory containing `pom.xml`, execute:

```bash
mvn clean test
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8081
```

The port can be changed using the `server.port` property in `application.properties`.

The initial administrator is configured through environment variables. The default username is `admin`; `APP_ADMIN_PASSWORD` must be configured before starting the application.

## API Documentation

Protected endpoints require:

**Authorization → Basic Auth**

Enter the appropriate username and password in Postman.

Use `Content-Type: application/json` for JSON request bodies.

### 1. Register a user

**POST** `/api/auth/register`

Request:

```json
{
  "username": "harshith",
  "password": "Harshith@123"
}
```

### 2. Create a bank account

**POST** `/api/accounts`

Authorization: Registered user.

Request:

```json
{
  "accountNumber": "1000000001",
  "accountType": "SAVINGS",
  "openingBalance": 50000
}
```

### 3. View user accounts

**GET** `/api/accounts`

Returns accounts belonging to the authenticated user.

### 4. Create a transaction limit

**POST** `/api/admin/limits`

Authorization: ADMIN.

Example request:

```json
{
  "scope": "USER",
  "scopeValue": "harshith",
  "maxAmount": 10000,
  "approvalThreshold": 5000
}
```

Supported limit scopes:

- `USER` — Apply limits to a username.
- `ACCOUNT` — Apply limits to a source account ID.
- `TYPE` — Apply limits to a payment type, such as `TRANSFER`.

### 5. View transaction limits

**GET** `/api/admin/limits`

Authorization: ADMIN.

### 6. Initiate a payment

**POST** `/api/payments`

Authorization: Registered user.

Example request:

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 3000,
  "paymentType": "TRANSFER"
}
```

The payment is processed according to the configured transaction limits and approval thresholds.

### 7. View personal payment history

**GET** `/api/payments`

Returns payments initiated by the authenticated user.

### 8. View all payments

**GET** `/api/admin/payments`

Authorization: ADMIN.

Returns payment records across users, including their statuses and review information.

### 9. Approve a pending payment

**POST** `/api/admin/payments/{id}/approve`

Authorization: ADMIN.

Example:

```text
POST /api/admin/payments/2/approve
```

### 10. Reject a pending payment

**POST** `/api/admin/payments/{id}/reject`

Authorization: ADMIN.

Example:

```text
POST /api/admin/payments/3/reject
```

## API Summary

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/accounts` | USER / ADMIN |
| GET | `/api/accounts` | USER / ADMIN |
| POST | `/api/admin/limits` | ADMIN |
| GET | `/api/admin/limits` | ADMIN |
| POST | `/api/payments` | USER / ADMIN |
| GET | `/api/payments` | USER / ADMIN |
| GET | `/api/admin/payments` | ADMIN |
| POST | `/api/admin/payments/{id}/approve` | ADMIN |
| POST | `/api/admin/payments/{id}/reject` | ADMIN |

## Testing

The application was manually tested using Postman.

Verified scenarios include:

- User registration and account creation.
- Successful immediate payment processing.
- Payments requiring administrator approval.
- Administrator approval and rejection.
- Transaction maximum-limit enforcement.
- User access restrictions on administrator endpoints.
- Administrator access to all payment records.

Automated tests can be executed using:

```bash
mvn clean test
```

## Security and Transaction Management

- Passwords are hashed using BCrypt.
- Spring Security protects authenticated endpoints.
- Role-based authorization restricts administrator operations.
- Account ownership is checked before initiating payments.
- `@Transactional` is used to coordinate payment and balance updates.
- Environment variables are used for sensitive configuration.

## Limitations and Future Improvements

This project is intended for learning and demonstration, not production banking use.

Potential improvements include:

- JWT-based authentication and refresh tokens.
- Multi-factor authentication.
- Idempotency keys to prevent duplicate payment requests.
- Database locking and concurrency handling.
- Audit logging and transaction history enhancements.
- Database migrations using Flyway or Liquibase.
- More integration and automated tests.
- Fraud detection and real payment-provider integration.

## Disclaimer

This application is a simulated payment-management backend. It does not perform actual financial transactions or connect to banking infrastructure.