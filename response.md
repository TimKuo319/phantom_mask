# Response

## A. Required Information

### A.1. Requirement Completion Rate

- [x] List all pharmacies open at a specific time and on a day of the week if requested.
  - Implemented at `GET /api/1.0/pharmacy/open` API.
- [x] List all masks sold by a given pharmacy, sorted by mask name or price.
  - Implemented at `GET /api/1.0/pharmacy/{pharmacyId}/masks` API.
- [x] List all pharmacies with more or less than x mask products within a price range.
  - Implemented at `GET /api/1.0/pharmacy` API.
- [x] The top x users by total transaction amount of masks within a date range.
  - Implemented at `GET /api/1.0/user/top-users` API.
- [x] The total number of masks and dollar value of transactions within a date range.
  - Implemented at `GET /api/1.0/analytics/masks/transaction-stats` API.
- [x] Search for pharmacies or masks by name, ranked by relevance to the search term.
  - Implemented at `GET /api/1.0/search` API.
- [x] Process a user purchases a mask from a pharmacy, and handle all relevant data changes in an atomic transaction.
  - Implemented at `POST /api/1.0/purchase/masks` API.

### A.2. API Document

Please refer to the comprehensive API documentation at [doc/api-documentation.md](doc/api-documentation.md).

The documentation includes:
- Complete endpoint descriptions with parameters
- Request/response examples in JSON format
- Parameter tables with type and description information
- Proper HTTP status codes

### A.3. Import Data Commands

The project includes Spring Batch to run ETL process to import the raw JSON data into the database. ETL process runs automatically on application startup


#### Using Docker Compose 

```bash

# Start the application with database
docker-compose up -d

# Data files are mounted from ../data directory to /app/data in the container
```

The ETL process will:
1. Parse pharmacy opening hours into structured format
2. Extract mask quantities from product names
3. Create relational database records
4. Import these data into database

### A.4. API Server Testing Instructions

To test the API server locally, follow these steps:

#### Prerequisites
- Git
- Docker and Docker Compose

#### Setup Steps

  ```bash

# clone repository to local
  git clone https://github.com/TimKuo319/phantom_mask.git

# change directory to project directory
  cd phantom_mask

# start application using Docker compose 
docker compose up -d 
  ```

As for API usage, you can reference API docs in [doc/api-documentation.md](doc/api-documentation.md).



## B. Bonus Information

### B.1. Test Coverage Report

I implemented unit tests for utility classes including:
- `OpeningHourParserTests` - Tests for parsing various opening hour formats
- `QuantityParserTests` - Tests for extracting mask quantities from product names

### B.2. Dockerized

The project is containerized with multi-stage Docker builds:

- **Dockerfile**: Multi-stage build using Maven and Eclipse Temurin JRE
- **docker-compose.yml**: Complete setup with MySQL database and application
- **Health checks**: Database health monitoring before application startup
- **Volume mounting**: Automatic data file mounting for ETL process

Dockerfile and docker-compose.yaml is under directory `/phantom_mask`

Build and run commands:

```bash
# Build and start all services
docker-compose up -d

# View application logs
docker-compose logs -f app

# Stop all services
docker-compose down
```

The containerized setup includes:
- MySQL 8.0 database with persistent storage
- Spring Boot application with proper dependency management
- Automatic database schema migration with Flyway
- Environment-specific configuration management

## C. Other Information

### C.1. Architecture Overview

The project follows a layered architecture pattern:

- **Controller Layer**: REST API endpoints with proper HTTP status handling
- **Service Layer**: Business logic implementation with input validation
- **DAO Layer**: Data access using Spring JDBC with named parameters

### C.2. Database Design

MyERD ([KDAN-assignment](https://drawsql.app/teams/individual-117/diagrams/kdan))

The database schema includes:
- **stores**: Pharmacy information with cash balances
- **opening_hours**: Structured opening hours with cross-night support
- **masks**: Product catalog with pricing and quantities
- **users**: Customer information and balances
- **transactions**: Purchase history 
