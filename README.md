# 🏦 Online Banking Application

A **Java-based** banking system simulation implementing core banking features such as user registration, login, account management, deposits, withdrawals, and transaction history.
The application offers both a **console UI** and a modern **Swing GUI** desktop interface, utilizing in-memory data structures (no database required).

---

![Banking Illustration](https://user-images.githubusercontent.com/yourusername/assets/banking-illustration.png)
*Illustration: Secure and simple banking experience.*

---

## 🚀 Features

| Feature                         | Description                                  |
| ------------------------------- | -------------------------------------------- |
| 🔐 **User Authentication**      | Secure username/password register & login    |
| 💰 **Account Types**            | Savings (interest) & Checking (overdraft)    |
| 🏦 **Account Management**       | Create and manage multiple accounts          |
| 💳 **Transactional Operations** | Deposit and withdrawals with validation      |
| 📜 **Transaction History**      | Detailed logs of deposits & withdrawals      |
| 🖥️ **Swing GUI**               | Intuitive and responsive graphical interface |
| ⌨️ **Console UI**               | Terminal-based interface for quick access    |
| ⚙️ **In-memory Data Storage**   | No database needed — easy to test & extend   |

---

## 📁 Project Structure

```text
OnlineBankingApplication/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── banking/
│   │   │           ├── App.java        # Main entry point (GUI launcher)
│   │   │           ├── model/          # Data models (User, Account, etc.)
│   │   │           ├── services/       # Business logic & services
│   │   │           └── ui/             # Console and Swing UI classes
│   └── resources/                      # Resources (if any)
├── out/                                 # Compiled classes folder
├── README.md
└── .gitignore
```

---

## 💻 Build & Run Instructions

### Prerequisites

* Java Development Kit (JDK) **17+**
* Recommended IDE: **IntelliJ IDEA**, **Eclipse**, or **VS Code**
* Git (to clone the repo)

### Clone the Repository

```bash
git clone https://github.com/vaibhav-1707/Online-Banking-Application.git
cd Online-Banking-Application
```

### Compile the Project

```bash
javac -d out -sourcepath src/main/java \
src/main/java/com/banking/App.java \
src/main/java/com/banking/ui/*.java \
src/main/java/com/banking/services/*.java \
src/main/java/com/banking/model/*.java
```

### Run the Swing GUI Application (Recommended)

```bash
java -cp out com.banking.App
```

### Run the Console UI (Optional)

```bash
java -cp out com.banking.ui.ConsoleUI
```

---

## 🎯 Quick Start with the Swing GUI

1. **Register** a new user (unique username & password)
2. **Login** with your credentials
3. **Create** Savings or Checking accounts from the dashboard
4. **Deposit** or **Withdraw** funds
5. **View** transaction history anytime
6. **Logout** securely when done

---

## 🛠 Development Guide — Step-by-Step Practice

This project is ideal for learning Java OOP, Swing UI, and service-layer design by building incrementally:

1. **Set up prerequisites** — JDK 17+, Git, IDE
2. **Project structure** — Organize into `model/`, `services/`, and `ui/` packages
3. **Domain model** — Implement `User`, `Transaction`, abstract `Account`, `SavingsAccount`, `CheckingAccount`, and `Customer`
4. **Service layer** — Build `AuthenticationService` & `BankService`
5. **Console UI** — Implement looping menus for all operations
6. **Swing UI** — Create `MainWindow` with `CardLayout` for login/dashboard views
7. **Testing** — Register/login, create accounts, perform deposits/withdrawals, verify transaction logs
8. **Enhancements** — Add validations, persistent storage, scheduled interest, better UI/UX

---

## 📈 Roadmap & Future Enhancements

* 🔄 Transfers between accounts
* 🗄️ Persistent storage (JSON, DB)
* 🔑 Secure password hashing
* 🌐 Web frontend / mobile client integration
* 🎨 Improved UI themes & responsiveness
* 🧪 Unit testing for all services and models

---

## 🤝 Contribution

Contributions are welcome!
Fork the repo, create a feature branch, and open a pull request.
Report bugs or suggest features via **[GitHub Issues](https://github.com/vaibhav-1707/Online-Banking-Application/issues)**.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📬 Contact

For questions or support:
📧 **[vaibhavgautam1707@gmail.com](mailto:vaibhavgautam1707@gmail.com)**

---

## 🙌 Thank You for Exploring the Online Banking Application

Build, test, and learn step by step — happy coding! 🚀

---
