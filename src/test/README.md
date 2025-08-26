# Online Banking Application - Test Suite

This directory contains comprehensive test cases for the Online Banking Application.

## Test Structure

### Model Tests (`src/test/java/com/banking/model/`)

- **AccountTest.java** - Tests for the base Account class functionality
- **SavingsAccountTest.java** - Tests for savings account features and interest calculations
- **CheckingAccountTest.java** - Tests for checking account overdraft functionality
- **TransactionTest.java** - Tests for transaction creation and formatting
- **UserTest.java** - Tests for user authentication and password validation
- **CustomerTest.java** - Tests for customer management and account relationships

### Service Tests (`src/test/java/com/banking/services/`)
- **AuthenticationServiceTest.java** - Tests for user registration, login, and security features
- **BankServiceTest.java** - Tests for banking operations and transfer functionality
- **DatabaseServiceTest.java** - Tests for database operations and connection management

### Integration Tests (`src/test/java/com/banking/`)
- **IntegrationTest.java** - End-to-end tests covering complete user workflows

## Running the Tests

### Prerequisites
- Java 11 or higher
- JUnit 5 (Jupiter)
- PostgreSQL (optional, for database tests)

### Using the Test Scripts

#### Windows
```batch
run-tests.bat
```

#### Unix/Linux/macOS
```bash
chmod +x run-tests.sh
./run-tests.sh
```

### Using IDE
1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Right-click on the `src/test` folder
3. Select "Run Tests" or "Run All Tests"

### Using Command Line
```bash
# Compile and run tests
javac -cp "src/main/java:src/test/java:lib/*" src/test/java/com/banking/**/*.java
java -cp "src/main/java:src/test/java:lib/*" org.junit.platform.console.ConsoleLauncher --class-path "src/main/java:src/test/java:lib/*" --scan-class-path
```

## Test Coverage

### Unit Tests
- **Account Operations**: Deposit, withdrawal, balance tracking
- **Interest Calculations**: Savings account interest application
- **Overdraft Protection**: Checking account overdraft limits
- **Transaction Recording**: Transaction creation and history
- **User Authentication**: Password validation and security
- **Customer Management**: Account relationships and data integrity

### Integration Tests
- **Complete User Workflows**: Registration → Login → Banking Operations
- **Multi-User Scenarios**: Multiple customers and accounts
- **Transfer Operations**: Inter-account transfers with various conditions
- **Data Persistence**: Export/import functionality
- **Error Handling**: Boundary conditions and error scenarios

## Test Features

### Security Testing
- Password strength validation
- Login attempt tracking
- Authentication failures
- Brute force protection

### Business Logic Testing
- Account type-specific operations
- Transfer validations
- Balance calculations
- Transaction history accuracy

### Data Integrity Testing
- Account balance consistency
- Transaction recording accuracy
- Customer-account relationships
- Data export/import validation

### Error Handling Testing
- Invalid input validation
- Boundary condition testing
- Database connection failures
- Resource cleanup verification

## Test Results

Tests are designed to validate:
- ✅ All core banking functionality
- ✅ Security features and password protection
- ✅ Account type-specific behaviors
- ✅ Transfer operations and validation
- ✅ Data persistence and integrity
- ✅ Error handling and edge cases
- ✅ Multi-user scenarios
- ✅ Integration between components

## Notes

- Database tests gracefully handle missing PostgreSQL installations
- Tests use realistic banking scenarios and data
- All tests include proper cleanup and resource management
- Integration tests cover complete user journeys
- Tests validate both positive and negative scenarios

## Troubleshooting

### Common Issues
1. **Classpath Problems**: Ensure all JAR files in `lib/` directory are included
2. **JUnit Version**: Make sure you have JUnit 5 (Jupiter) installed
3. **Database Connection**: Database tests will fail gracefully if PostgreSQL is not running
4. **Compilation Errors**: Ensure Java 11+ is being used

### Getting Help
If you encounter issues:
1. Check the Java version: `java -version`
2. Verify JUnit installation: `java -cp lib/* org.junit.platform.console.ConsoleLauncher --version`
3. Test individual test classes first
4. Check for missing dependencies in the `lib/` directory
