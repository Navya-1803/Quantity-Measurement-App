# Quantity Measurement App

A Java Spring Boot application to compare, convert and do arithmetic on physical
quantities: **length, weight, volume and temperature**. The project was built step by
step through use cases UC1 to UC17, starting from a simple `Feet` equality check and
ending with a REST API backed by Spring Data JPA.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Language |
| Spring Boot 4.0.1 | Application framework |
| Spring Web MVC | REST API |
| Spring Data JPA / Hibernate | Persistence |
| H2 / MySQL | Development and production databases |
| Jakarta Validation | Request validation |
| Lombok | Removes boilerplate in models |
| Springdoc OpenAPI | Swagger UI and API docs |
| Spring Boot Actuator | Health and metrics |
| Jackson XML | XML responses |
| JUnit 5, Mockito, MockMvc | Testing |
| SLF4J / Logback | Logging |
| Maven | Build |

## Supported Units

| Category | Units | Base unit | Arithmetic |
|---|---|---|---|
| Length | FEET, INCHES, YARDS, CENTIMETERS | FEET | Yes |
| Weight | KILOGRAM, GRAM, POUND | KILOGRAM | Yes |
| Volume | LITRE, MILLILITRE, GALLON | LITRE | Yes |
| Temperature | CELSIUS, FAHRENHEIT, KELVIN | CELSIUS | No (compare and convert only) |

## Operations

- **Compare** - `1 FEET == 12 INCHES` -> `true`
- **Convert** - `100 CELSIUS -> 212 FAHRENHEIT`
- **Add** - `1 FEET + 12 INCHES = 2 FEET` (result in the first unit or a chosen target unit)
- **Subtract** - `10 FEET - 6 INCHES = 9.5 FEET`
- **Divide** - `24 INCHES / 2 FEET = 1.0`

Add and subtract results are rounded to two decimal places. Quantities of different
categories (for example FEET and KILOGRAM) can never be compared or combined.

## Project Structure

```
src/main/java/com/app/quantitymeasurement
├── QuantityMeasurementApplication.java   Spring Boot entry point
├── config        OpenAPI configuration
├── controller    REST controller (/api/v1/quantities)
├── exception     custom exception, error response, global exception handler
├── model         DTOs, JPA entity, operation types
├── repository    Spring Data JPA repository
├── service       business logic
└── unit          IMeasurable, unit enums, generic Quantity, ArithmeticOperation
```

## Use Case Progression

| UC | Feature |
|---|---|
| UC1 | Feet measurement equality |
| UC2 | Feet and inches equality |
| UC3 | Generic `QuantityLength` class (DRY) |
| UC4 | Yards and centimeters |
| UC5 | Unit to unit conversion |
| UC6 | Addition of two lengths |
| UC7 | Addition with a target unit |
| UC8 | `LengthUnit` as a standalone enum with conversion responsibility |
| UC9 | Weight measurement |
| UC10 | Generic `Quantity<U extends IMeasurable>` |
| UC11 | Volume measurement |
| UC12 | Subtraction and division |
| UC13 | Centralized arithmetic logic with `ArithmeticOperation` |
| UC14 | Temperature with selective arithmetic support |
| UC15 | N-Tier architecture (controller, service, repository, entity) |
| UC16 | JDBC persistence with a connection pool |
| UC17 | Spring Boot REST services with Spring Data JPA |

## Design Principles and Patterns

- **DRY** - one generic `Quantity` class and one arithmetic helper for every category
- **Open/Closed** - new categories need only a new enum implementing `IMeasurable`
- **Single Responsibility** - units convert, the service calculates, the controller maps HTTP
- **Strategy** - `ArithmeticOperation` constants carry their own computation as lambdas
- **Facade** - the controller hides the service behind simple endpoints
- **Dependency Injection** - constructor injection of the repository and service
- **DTO** - request and response objects separate the API from the database entity

## Running the Application

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

| URL | Description |
|---|---|
| `/swagger-ui.html` | Swagger UI |
| `/api-docs` | OpenAPI specification |
| `/h2-console` | H2 console (JDBC URL `jdbc:h2:mem:quantitymeasurementdb`, user `sa`) |
| `/actuator/health` | Health check |

To use MySQL instead of H2, run with the `prod` profile and set `DB_USERNAME` /
`DB_PASSWORD`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## REST API

Base URL: `/api/v1/quantities`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/compare` | Compare two quantities |
| POST | `/convert` | Convert the first quantity to the unit of the second |
| POST | `/add` | Add two quantities (optional `targetQuantityDTO`) |
| POST | `/subtract` | Subtract two quantities (optional `targetQuantityDTO`) |
| POST | `/divide` | Divide two quantities |
| GET | `/history/operation/{operation}` | History of an operation, e.g. `ADD` |
| GET | `/history/type/{measurementType}` | History of a category, e.g. `LengthUnit` |
| GET | `/count/{operation}` | Number of successful operations |
| GET | `/history/errored` | Failed operations |

Example:

```bash
curl -X POST http://localhost:8080/api/v1/quantities/add \
  -H "Content-Type: application/json" \
  -d '{"thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
       "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}}'
```

```json
{"thisValue":1.0,"thisUnit":"FEET","thisMeasurementType":"LengthUnit","thatValue":12.0,
 "thatUnit":"INCHES","thatMeasurementType":"LengthUnit","operation":"add","resultString":null,
 "resultValue":2.0,"resultUnit":"FEET","resultMeasurementType":"LengthUnit",
 "errorMessage":null,"error":false}
```

Send `Accept: application/xml` to get the response as XML.

Errors return a consistent body, for example an invalid unit:

```json
{"timestamp":"...","status":400,"error":"Quantity Measurement Error",
 "message":"Unit must be valid for the specified measurement type","path":"/api/v1/quantities/add"}
```

## Running the Tests

```bash
./mvnw test
```

Every class has its own JUnit test class: unit tests for the measurement logic,
Mockito tests for the service, MockMvc tests for the controller and Spring Boot
integration tests against the H2 database.

## Author

Navya Raj
