# 🏦 Online Banking Application

A complete Java-based banking system with a modern graphical user interface, secure authentication, and email notifications.

## 📋 Table of Contents
- [What is This?](#-what-is-this)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Installation Guide](#-installation-guide)
- [How to Run](#-how-to-run)
- [How to Use](#-how-to-use)
- [Email Setup](#-email-setup)
- [Troubleshooting](#-troubleshooting)
- [File Structure](#-file-structure)
- [Technical Details](#-technical-details)
- [Contributing](#-contributing)
- [Support](#-support)
- [License](#-license)
- [Acknowledgments](#-acknowledgments)
- [Quick Start Summary](#-quick-start-summary)

## 🎯 What is This?

This is a **complete banking application** that lets you:
- Create and manage bank accounts (Savings & Checking)
- Deposit and withdraw money
- Transfer money between accounts
- View transaction history
- Get monthly statements via email
- Secure login and registration system

Think of it as a **mini-bank** that runs on your computer!

## ✨ Features

### 🔐 Security Features
- **User Authentication**: Secure login/registration system
- **Password Protection**: All accounts are password-protected
- **Session Management**: Automatic logout for security

### 💰 Banking Features
- **Multiple Account Types**: Savings and Checking accounts
- **Interest Calculation**: Savings accounts earn interest automatically
- **Transaction History**: Complete record of all money movements
- **Account Management**: Create, view, and manage multiple accounts

### 📧 Communication Features
- **Email Notifications**: Get emails for transactions and statements
- **Monthly Statements**: Automatic monthly account summaries
- **Transaction Alerts**: Email confirmations for all activities

### 🖥️ User Interface
- **Modern GUI**: Clean, easy-to-use interface
- **Responsive Design**: Works on different screen sizes
- **Intuitive Navigation**: Easy to find what you need

## 🖼️ Screenshots

<img width="1913" height="1014" alt="image" src="https://github.com/user-attachments/assets/9222c6b2-aa28-4990-b983-e0377d6e30c4" />
<img width="1915" height="1015" alt="image" src="https://github.com/user-attachments/assets/0a17f7d8-c5d6-4265-ad66-fb3dd0bbe032" />
<img width="1917" height="1018" alt="image" src="https://github.com/user-attachments/assets/dbd5b66a-0755-41c6-87a6-e1cba27ab0f7" />
<img width="1918" height="1017" alt="image" src="https://github.com/user-attachments/assets/0a40cd29-d373-41ec-93fb-cc1dbe3fb5a8" />



## 🚀 Installation Guide

### ⚠️ SECURITY SETUP (IMPORTANT!)

**Before running the application, you MUST set up your configuration securely:**

1. **Copy the template file:**
   ```bash
   cp resources/config.properties.template resources/config.properties
   ```

2. **Edit `resources/config.properties` with your real credentials:**
   - Replace `your_username_here` with your database username
   - Replace `your_password_here` with your database password
   - Replace `your_email@gmail.com` with your Gmail address
   - Replace `your_app_password_here` with your Gmail app password

3. **NEVER commit this file to Git** - it's already in `.gitignore`

### Prerequisites (What You Need First)

#### 1. Java Installation
**What is Java?** Java is a programming language that this app needs to run.

**How to Install:**
1. Go to [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
2. Download "Java SE Development Kit" for Windows
3. Run the installer and follow the instructions
4. **Important**: Restart your computer after installation

**Check if Java is installed:**
- Press `Win + R`, type `cmd`, press Enter
- Type `java -version` and press Enter
- If you see version information, Java is installed ✅
- If you get an error, Java is not installed ❌

#### 2. Gmail Account (For Email Features)
**Why do you need this?** To send email notifications and monthly statements.

**Requirements:**
- A Gmail account
- 2-factor authentication enabled
- An "App Password" generated

**How to set up Gmail App Password:**
1. Go to [myaccount.google.com](https://myaccount.google.com)
2. Click "Security" on the left
3. Enable "2-Step Verification" if not already enabled
4. Go back to "Security" and click "App passwords"
5. Select "Mail" and click "Generate"
6. Copy the 16-character password (remove spaces)

### Download the Application

1. **Clone or Download** this repository
2. **Extract** the ZIP file to a folder (e.g., `C:\BankingApp`)
3. **Open** the folder in File Explorer

## 🏃‍♂️ How to Run

### Method 1: Simple Double-Click (Recommended)
1. **Double-click** `fix-this.bat`
2. **Wait** for compilation to complete
3. **The banking app will start automatically**

### Method 2: Command Line
1. **Press** `Win + R`
2. **Type** `cmd` and press Enter
3. **Navigate** to the app folder:
   ```cmd
   cd "C:\BankingApp"
   ```
4. **Run** the app:
   ```cmd
   fix-this.bat
   ```

### What Happens When You Run It?
1. **Compilation**: Java converts your code into runnable files
2. **Database Setup**: Creates local storage for your data
3. **Email Service**: Connects to Gmail (if configured)
4. **GUI Launch**: Opens the banking application window

## 📱 How to Use

### First Time Setup

#### 1. Registration
- **Click** "Register" button
- **Enter** your details:
  - Full Name
  - Email Address
  - Username (for login)
  - Password (for login)
- **Click** "Register" to create your account

#### 2. First Login
- **Enter** your username and password
- **Click** "Login"
- **Welcome** to your banking dashboard!

### Using the Banking Features

#### Creating Accounts
1. **Click** "Create Account"
2. **Choose** account type:
   - **Savings Account**: Earns interest, good for long-term savings
   - **Checking Account**: For daily transactions, no interest
3. **Enter** initial deposit amount
4. **Click** "Create"

#### Making Transactions
1. **Select** an account from the list
2. **Click** the action you want:
   - **Deposit**: Add money to your account
   - **Withdraw**: Take money out of your account
   - **Transfer**: Move money between accounts

#### Viewing Information
- **Account Details**: See balance, account number, type
- **Transaction History**: View all deposits, withdrawals, transfers
- **Monthly Statement**: Get email summary of your account activity

## 📧 Email Setup

### Why Email is Important
- **Transaction Confirmations**: Get emails when you deposit/withdraw
- **Monthly Statements**: Automatic account summaries
- **Security Alerts**: Notifications about account activity

### Configuration Steps

#### 1. Update Configuration File
1. **Open** `resources/config.properties` in Notepad
2. **Find** this line:
   ```properties
   mail.password=YOUR_GMAIL_APP_PASSWORD_HERE
   ```
3. **Replace** `YOUR_GMAIL_APP_PASSWORD_HERE` with your actual Gmail app password
4. **Save** the file

#### 2. Test Email Service
1. **Run** the banking app
2. **Check** the console output for email connection messages
3. **Look** for "Email service configured successfully"

### Email Troubleshooting
- **"Authentication failed"**: Check your Gmail app password
- **"Connection refused"**: Check your internet connection
- **"Username not accepted"**: Verify your Gmail address

## 🔧 Troubleshooting

### Common Problems and Solutions

#### Problem: "Java not found"
**Solution:**
- Install Java (see Installation Guide above)
- Restart your computer after installation

#### Problem: "Compilation failed"
**Solution:**
- Make sure all files are in the correct folders
- Check that Java is properly installed
- Try running `fix-this.bat` again

#### Problem: "Email not working"
**Solution:**
- Verify Gmail app password in `config.properties`
- Check that 2-factor authentication is enabled
- Ensure internet connection is working

#### Problem: "App won't start"
**Solution:**
- Check console output for error messages
- Verify all required files are present
- Try running from Command Prompt instead of PowerShell

#### Problem: "Database connection failed"
**Solution:**
- This is normal if PostgreSQL isn't installed
- The app will use file-based storage instead
- No action needed - it's working as designed

### Getting Help
1. **Check** the console output for error messages
2. **Look** at the troubleshooting section above
3. **Verify** all prerequisites are installed
4. **Try** running the app again

## 📁 File Structure

```
OnlineBankingApplication/
├── 📁 lib/                          # Java libraries (don't touch)
├── 📁 resources/                    # Configuration files
│   └── 📄 config.properties        # Email and database settings
├── 📁 src/main/java/com/banking/   # Main application code
│   ├── 📁 model/                   # Data models (Account, Customer, etc.)
│   ├── 📁 services/                # Business logic (Email, Database, etc.)
│   └── 📁 ui/                      # User interface (GUI windows)
├── 📁 src/test/java/               # Test files (for developers)
├── 📄 fix-this.bat                 # Main launcher script
├── 📄 build.bat                    # Alternative build script
├── 📄 build.sh                     # Linux/Mac build script
└── 📄 README.md                    # This file
```

### What Each Folder Does

- **`lib/`**: Contains Java libraries (don't modify)
- **`resources/`**: Configuration files you can edit
- **`src/main/java/`**: The actual banking application code
- **`src/test/java/`**: Test files (not needed for normal use)

## 🛠️ Technical Details

### Technology Stack
- **Language**: Java 8 or higher
- **GUI Framework**: Java Swing
- **Email**: JavaMail API with SMTP
- **Database**: PostgreSQL (optional) or file-based storage
- **Build Tool**: Manual compilation with javac

### System Requirements
- **Operating System**: Windows 10/11, macOS, or Linux
- **Java Version**: Java 8 or higher
- **Memory**: Minimum 512MB RAM
- **Storage**: 100MB free space
- **Internet**: Required for email features

### Performance
- **Startup Time**: 5-10 seconds (first time), 2-3 seconds (subsequent)
- **Memory Usage**: 100-200MB RAM
- **Storage**: Grows with transaction history

## 🤝 Contributing

### How to Help Improve This App

#### For Non-Technical Users
- **Report Bugs**: Tell us when something doesn't work
- **Suggest Features**: What would make banking easier?
- **Test the App**: Try different scenarios and report issues

#### For Developers
- **Fork** the repository
- **Create** a feature branch
- **Make** your changes
- **Test** thoroughly
- **Submit** a pull request

### Development Setup
1. **Clone** the repository
2. **Install** Java Development Kit (JDK)
3. **Open** in your preferred IDE (Eclipse, IntelliJ, VS Code)
4. **Run** `fix-this.bat` to test

## 📞 Support

### Getting Help
- **Check** this README first
- **Look** at the troubleshooting section
- **Search** existing issues on GitHub
- **Create** a new issue if needed

### Contact Information
- **GitHub Issues**: [Create an issue here](https://github.com/vaibhav-1707/Online-Banking-Application/issues)
- **Repository**: [https://github.com/vaibhav-1707/Online-Banking-Application](https://github.com/vaibhav-1707/Online-Banking-Application)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- **Java Community**: For the excellent Java platform
- **Open Source Libraries**: For the email and database functionality
- **Contributors**: Everyone who helps improve this application

---

## 🚀 Quick Start Summary

1. **Install Java** from Oracle's website
2. **Download** this application
3. **Double-click** `fix-this.bat`
4. **Register** your first account
5. **Start banking!**

**Need help?** Check the troubleshooting section above or create an issue on GitHub.

---

*Last updated: January 2025*
*Version: 1.0.0*
