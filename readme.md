# FxDealsWarehouse

A Spring Boot application for managing Foreign Exchange (FX) deals in a data warehouse

## Features

- Accepts and persists FX deal details into a PostgreSQL database
- Validates deal request structure and currency codes
- Prevents duplicate deal imports using unique Deal IDs
- No rollback policy - all valid imported rows are persisted
- Comprehensive error handling and logging
- Unit testing with good coverage
- Dockerized deployment

## Technologies Used

- **Java 21**: Core programming language
- **Spring Boot 3.4.2**: Application framework
- **PostgreSQL**: Database
- **Maven**: Build tool
- **Docker & Docker Compose**: Containerization
- **Lombok**: Boilerplate code reduction
- **MapStruct**: Object mapping
- **JUnit 5**: Testing framework
- **SLF4J**: Logging
- **Jakarta Validation**: Bean validation

## Installation & Setup

1. **Prerequisites**
   - Docker and Docker Compose
   - Maven
   - Java 21
   - Git

2. **Clone the Repository**
```bash
git clone https://github.com/yourusername/FxDealsWarehouse.git
cd FxDealsWarehouse
```

3. **Build and Run**
```bash
make up
```
This starts the application and PostgreSQL database using Docker Compose.

## Makefile Commands

- `make help`: Show available commands
- `make up`: Start Docker containers in detached mode
- `make down`: Stop and remove Docker containers
- `make test`: Run Maven tests
- `make clean`: Clean Maven project and remove target directory

## API Endpoints

### Insert Deal
- **Endpoint**: `POST /api/v1/deals`
- **URL**: `http://localhost:8081/api/v1/deals`
- **Request JSON Example**:
```json
{
  "id": "867GFD564DS",
  "fromCurrency": "USD",
  "toCurrency": "SGD",
  "dealAmount": 1000.50
}
```
- **Response** (HTTP 201 Created):
```json
{
    "id": "867GFD564DS",
    "fromCurrency": "USD",
    "toCurrency": "SGD",
    "dealTimestamp": "2025-02-22T02:08:22.857055824",
    "dealAmount": 1000.50
}
```

## Request Validation

- **Fields Validated**:
    - `id`: Must not be blank
    - `fromCurrency`: Must be a valid 3-letter ISO code (e.g., "USD")
    - `toCurrency`: Must be a valid 3-letter ISO code (e.g., "SGD")
    - `dealAmount`: Must be positive and not null
- **Validation Mechanism**:
    - Uses Jakarta Validation annotations (`@NotBlank`, `@NotNull`, `@Positive`)
    - Custom currency validation against a CSV file of valid codes
    - Throws specific exceptions for invalid data (`InvalidCurrencyException`, `CurrencyNotAvailableException`)

## Database Interaction

- **Database**: PostgreSQL
- **Entity**: `Deal` table with columns:
    - `id` (String, Primary Key)
    - `from_currency` (String, 3 chars)
    - `to_currency` (String, 3 chars)
    - `deal_timestamp` (LocalDateTime)
    - `deal_amount` (BigDecimal)
- **JPA**: Spring Data JPA with auditing for timestamp
- **Duplicate Prevention**: Unique constraint on `id` column

## Testing

- **Framework**: JUnit 5 with Spring Test
- **Coverage**:
    - Unit tests for service layer (`DefaultDealServiceTest`)
    - Currency validation tests (`CsvCurrencyHolderTest`, `CsvCurrencyValidatorTest`)
    - File reading tests (`CsvFileReaderTest`)
- **Location**: `src/test/java`
- **Run**: `make test`

## Dockerization

- **Dockerfile**: Multi-stage build
    - Maven stage: Builds the application
    - Runtime stage: Runs the JAR with Eclipse Temurin JDK 21
- **Docker Compose**:
    - `app`: Spring Boot application on port 8081
    - `db`: PostgreSQL on port 5433
    - Volumes for persistent data and Maven cache

## Project Structure

```
FxDealsWarehouse/
├── docker-compose.yml
├── Dockerfile
├── Makefile
├── pom.xml
├── readme.md
├── src/
│   ├── main/
│   │   ├── java/com/progressoft/FxDealsWarehouse/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── currencystore/   # Currency validation logic
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # JPA entities
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── mapper/         # Object mappers
│   │   │   ├── repository/     # JPA repositories
│   │   │   └── service/        # Business logic
│   │   └── resources/          # Configuration files, currencies.csv
│   └── test/                   # Unit tests
└── target/                     # Build artifacts
```

## Error Handling

- Custom exceptions for specific cases (duplicate deals, invalid currencies)
- Global exception handler returning structured `ErrorResponse`
- Logging with SLF4J for debugging and monitoring
