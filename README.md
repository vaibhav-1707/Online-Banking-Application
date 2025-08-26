# Online Banking Application

A comprehensive Java-based online banking system with GUI interface, email notifications, and database persistence.

## Features

### ✅ **Implemented Features**

1. **User Authentication**
   - User registration with password strength validation
   - Secure login with PBKDF2 password hashing
   - Session management with timeout

2. **Account Management**
   - Create savings and checking accounts
   - Deposit, withdraw, and transfer funds
   - Transaction history tracking
   - Account balance monitoring

3. **Email Notifications**
   - Welcome email on registration
   - Security alert on login
   - Transaction notifications (deposit, withdrawal, transfer)
   - **Monthly statements** (automated, sent on 1st of every month)

4. **Data Persistence**
   - PostgreSQL database integration
   - File-based fallback storage
   - Data import/export functionality

5. **User Interface**
   - Modern Swing-based GUI
   - Intuitive dashboard
   - Real-time account updates

### 🔧 **Technical Features**

- **Database**: PostgreSQL with automatic table creation
- **Email**: SMTP support (Gmail configured by default)
- **Security**: PBKDF2 password hashing with salt
- **Architecture**: MVC pattern with service layer
- **Persistence**: Hybrid approach (database + file backup)

## Prerequisites

### System Requirements

- **Java**: JDK 17 or higher
- **PostgreSQL**: 12 or higher
- **Memory**: Minimum 2GB RAM
- **OS**: Windows, macOS, or Linux

### Software Setup

#### 1. Install PostgreSQL

```bash
# Windows: Download from https://www.postgresql.org/download/windows/
# macOS: brew install postgresql
# Linux: sudo apt-get install postgresql postgresql-contrib
```bash

#### 2. Create Database

```sql
-- Connect to PostgreSQL as superuser
psql -U postgres

-- Create database
CREATE DATABASE bankdb;

-- Create user (optional)
CREATE USER banking_user WITH PASSWORD 'your_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE bankdb TO banking_user;

-- Exit
\q
```sql

#### 3. Configure Database Connection

Edit `resources/config.properties`:

```properties
# Database Configuration
db.host=localhost
db.port=5432
db.name=bankdb
db.username=postgres
db.password=admin

# Email Configuration
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.username=your-email@gmail.com
mail.password=your-app-password
```properties

## Installation & Setup

### 1. Clone/Download the Project

```bash
git clone <repository-url>
```bash

### 2. Verify Dependencies

Ensure these JAR files are in the `lib/` directory:

- `postgresql-42.7.3.jar` - PostgreSQL JDBC driver
- `javax.mail-1.6.2.jar` - JavaMail API
- `javax.activation-1.2.0.jar` - JavaBeans Activation Framework

### 3. Test Database Connection

```bash
# Run the database test
java -cp "lib/*:src/main/java" com.banking.services.DatabaseTest
```bash

### 4. Run the Application

```bash
# Run the main application
java -cp "lib/*:src/main/java" com.banking.ui.MainWindow
```bash

## Usage Guide

### First Time Setup

1. **Launch Application**: Run `MainWindow.java`
2. **Register Account**: Click "Register" and provide:
   - Username (unique)
   - Strong password (8+ chars, uppercase, lowercase, digit)
   - Valid email address
3. **Login**: Use your credentials to access the dashboard

### Banking Operations

1. **Create Account**: Choose account type (Savings/Checking)
2. **Deposit**: Add funds to any account
3. **Withdraw**: Remove funds (within balance limits)
4. **Transfer**: Move funds between your accounts
5. **View History**: Check transaction records
6. **Export Data**: Save transaction history to file

### Monthly Statements

- **Automatic**: Sent on 1st of every month at 9:00 AM
- **Manual**: Click "Monthly Statement" button to generate immediately
- **Content**: Account summary, monthly transactions, current balances

## Configuration

### Email Setup (Gmail)

1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account settings
   - Security → 2-Step Verification → App passwords
   - Generate password for "Mail"
3. Update `config.properties` with your email and app password

### Database Configuration

- **Host**: Usually `localhost` for local development
- **Port**: Default PostgreSQL port is `5432`
- **Database**: Must exist before running the application
- **Credentials**: Use PostgreSQL superuser or dedicated banking user

## Troubleshooting

### Common Issues

#### 1. Database Connection Failed

```text
Error: Connection refused
Solution: Ensure PostgreSQL is running and accessible
```text

```bash
# Check PostgreSQL status
sudo systemctl status postgresql  # Linux
brew services list | grep postgres  # macOS
```bash

#### 2. Email Not Sending

```text
Error: Authentication failed
Solution: Check email credentials and app password
```text

- Verify Gmail app password is correct
- Ensure 2FA is enabled on Gmail account
- Check firewall/antivirus blocking SMTP

#### 3. Application Won't Start

```text
Error: ClassNotFoundException
Solution: Check classpath and dependencies
```text

```bash
# Verify all JAR files are present
ls -la lib/
```bash

### Debug Mode

Enable debug logging by setting in `config.properties`:

```properties
mail.debug=true
logging.level=DEBUG
```properties

## Security Features

### Password Security

- **Hashing**: PBKDF2 with 100,000 iterations
- **Salt**: 16-byte random salt per user
- **Strength**: Enforces minimum requirements

### Session Security

- **Timeout**: 15-minute inactivity timeout
- **Lockout**: 5 failed login attempts trigger temporary lockout
- **Validation**: Input sanitization and validation

### Data Protection

- **Encryption**: Database connections use SSL (if configured)
- **Access Control**: User data isolation
- **Audit Trail**: Complete transaction logging

## Development

### Project Structure

```text
src/main/java/com/banking/
├── model/          # Data models (User, Customer, Account, Transaction)
├── services/       # Business logic (Auth, Bank, Email, Database)
└── ui/            # User interface (MainWindow, ConsoleUI)
```text

### Adding New Features

1. **Model**: Extend existing classes or create new ones
2. **Service**: Implement business logic in service layer
3. **UI**: Add interface elements in MainWindow
4. **Database**: Update DatabaseService for persistence

### Testing

```bash
# Test database connection
java -cp "lib/*:src/main/java" com.banking.services.DatabaseTest

# Test email service
java -cp "lib/*:src/main/java" com.banking.services.EmailService
```bash

## Support

### Getting Help

1. **Check Logs**: Application outputs detailed error messages
2. **Verify Configuration**: Ensure all settings are correct
3. **Test Components**: Use individual test classes
4. **Database Logs**: Check PostgreSQL logs for connection issues

### Known Limitations

- **User Model**: Password hash not directly accessible (design limitation)
- **Account Balance**: Cannot set balance directly (security feature)
- **Transaction History**: Limited to in-memory storage in current implementation

## License

This project is provided as-is for educational and development purposes.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

---

**Note**: This is a demonstration application. For production use, implement additional security measures, proper error handling, and comprehensive testing.
