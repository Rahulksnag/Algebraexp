# Algebraic Expression Evaluator

This application allows you to store, retrieve, and evaluate algebraic equations. It supports parsing equations into postfix notation, constructing expression trees, and evaluating the results with given variable values.

## Features

1. **Postfix Tree Implementation**:
   - Parse the input equation into postfix notation.
   - Construct a postfix (expression) tree where operators are parent nodes, and operands are child nodes.

2. **Validation and Error Handling**:
   - Validate equations for correct syntax and operators.
   - Handle missing or invalid variable values gracefully during evaluation.

3. **Testing**:
   - Unit tests for storing, retrieving, and evaluating equations.
   - Test cases for edge scenarios like complex equations, missing operators, or invalid input.

## Setup Instructions

### Prerequisites

- Java JDK 22
- Maven
- An IDE or text editor (IntelliJ IDEA recommended)

### Clone the Repository

```bash
git clone https://github.com/yourusername/Algebraexp.git
cd Algebraexp
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Endpoints

### Store an Equation

- **URL:** `/api/equations/store`
- **Method:** POST
- **Request Body:**
  ```json
  {
    "equation": "3x + 2y - 4z"
  }
  ```
- **Response:**
  ```json
  {
    "message": "Equation stored successfully",
    "equationId": 1
  }
  ```

### Get All Equations

- **URL:** `/api/equations`
- **Method:** GET
- **Response:**
  ```json
  {
    "equations": [
      {
        "equationId": 1,
        "equation": "3x + 2y - 4z"
      }
    ]
  }
  ```

### Get a Specific Equation

- **URL:** `/api/equations/{equationId}`
- **Method:** GET
- **Response:**
  ```json
  {
    "equationId": 1,
    "equation": "3x + 2y - 4z"
  }
  ```

### Evaluate an Equation

- **URL:** `/api/equations/{equationId}/evaluate`
- **Method:** POST
- **Request Body:**
  ```json
  {
    "variables": {
      "x": 1,
      "y": 1,
      "z": 1
    }
  }
  ```
- **Response:**
  ```json
  {
    "equationId": 1,
    "equation": "3x + 2y - 4z",
    "variables": {
      "x": 1,
      "y": 1,
      "z": 1
    },
    "result": 1
  }
  ```

## Running Tests

### Unit Tests

To run the unit tests, use the following command:

```bash
mvn test
```

### Test Coverage

The unit tests cover the following scenarios:
- Storing an equation
- Retrieving all stored equations
- Retrieving a specific equation by ID
- Evaluating an equation with given variable values
- Handling invalid equations and missing variables

## Documentation

### Application Overview

This application provides an API for storing, retrieving, and evaluating algebraic equations. It uses Spring Boot for the backend and provides various endpoints for interacting with the equations.

### Setup Instructions

1. Clone the repository
2. Build the project using Maven
3. Run the application
4. Use the provided API endpoints to interact with the application

### Test Execution Steps

1. Run the unit tests using `mvn test`
2. Verify the test results for coverage and correctness

## Thank You !!!
