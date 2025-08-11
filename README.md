# Online Banking Application — Step-by-Step Practice Guide

This guide lets you rebuild the application from scratch without looking at any source code. Follow it step by step and write the code yourself. The app has both a console UI and a Swing GUI, with in‑memory data structures (no database).

Use this as a checklist: finish each section before moving on.

---

## 0) Prerequisites

- Install JDK 17+ and ensure `java` and `javac` are on PATH
- Optional: Git, IntelliJ/VS Code

---

## 1) Project Structure

Create a Maven/Gradle‑like layout (even if you compile with `javac`):

- `src/main/java/com/banking/` — main application entry point
- `src/main/java/com/banking/model/` — domain classes
- `src/main/java/com/banking/services/` — business services
- `src/main/java/com/banking/ui/` — user interfaces (console + Swing)

Acceptance: directories exist and compile with an empty `App` placeholder.

---

## 2) Domain Model (Design First, Then Implement)

Goal: model users, customers, accounts, and transactions.

2.1) User

- Fields: username, password (plain for practice)
- Behavior: authenticate(inputPassword) → boolean
- Acceptance: returns true only if password matches

2.2) Transaction

- Fields: id (UUID), timestamp, type ("deposit"/"withdrawal"), amount, resultingBalance
- Behavior: description string including timestamp, type, ₹ amount, and ₹ resultingBalance
- Formatting: use Indian Rupee currency formatting (₹) and avoid scientific notation

2.3) Account (abstract)

- Fields: accountNumber, balance, transactionList
- Behavior:
  - getAccountNumber(), getBalance(), getTransactionList()
  - deposit(amount): amount>0 → increase balance and add transaction
  - withdraw(amount): amount>0 and ≤ balance → decrease balance and add transaction
- Acceptance: balances update correctly and each operation appends one transaction

2.4) SavingsAccount (extends Account)

- Field: interestRate (e.g., 0.05 for 5%)
- Behavior: applyInterest() → computes interest as balance*rate and deposits it

2.5) CheckingAccount (extends Account)

- Field: overdraftLimit (positive number)
- Behavior: override withdraw to allow balance down to −overdraftLimit; add transaction if allowed

2.6) Customer

- Fields: name, email, user, accounts (list)
- Behavior: getUser(), getAccounts(), addAccount(Account)
- Acceptance: can attach multiple accounts to one customer

Finish Criteria for Section 2:

- All classes compile and basic manual tests with temporary objects behave as expected

---

## 3) Services (Business Logic)

3.1) AuthenticationService

- In‑memory map of username → User
- registerUser(username, password): returns false if username exists; otherwise create User and return true
- login(username, password): returns User on success; null otherwise

3.2) BankService

- In‑memory list of Customer
- addCustomer(Customer)
- findCustomerByUsername(username) → Customer or null
- getAccountsForCustomer(username) → list of Account (empty list if no customer)

Finish Criteria for Section 3:

- You can register a user, create a matching Customer, and retrieve their accounts

---

## 4) Console UI (Practice Loop + Input Handling)

Create `ConsoleUI` with a simple loop:

- If no user is logged in: show menu [Register, Login, Exit]
  - Register: prompts username/password → AuthenticationService.registerUser; also create a Customer in BankService
  - Login: prompts username/password → AuthenticationService.login → sets loggedInUser
- If logged in: show menu [View Accounts, Deposit, Withdraw, View Transaction History, Logout]
  - View Accounts: list account number + ₹ balance
  - Deposit/Withdraw: ask for account number and amount, perform operation, show new balance
  - View History: show each transaction description
  - Logout: clear loggedInUser

Finish Criteria for Section 4:

- All flows work end‑to‑end via console; transactions appear and balances update

---

## 5) GUI (Swing) — `MainWindow`

Use a JFrame with a `CardLayout` to switch between:

5.1) Auth Card

- Fields: username (JTextField), password (JPasswordField)
- Buttons: Login, Register
- Behavior:
  - Register: call AuthenticationService.registerUser; if success, ensure a matching Customer exists in BankService
  - Login: validate via AuthenticationService.login; on success set currentUsername and switch to Dashboard card

5.2) Dashboard Card

- Components:
  - Accounts list (JList) showing: `<accountNumber> | Balance: ₹<amount>`
  - Buttons: Create Account, Deposit, Withdraw, View History, Refresh, Logout
  - Status label for user feedback
- Behaviors:
  - Create Account: dialog to choose Savings vs Checking; prompt for account number; then prompt rate/overdraft as needed; add to current customer
  - Deposit/Withdraw: prefer using the selected account in the list; if none selected, prompt for account number; then prompt for amount; update and refresh the list
  - View History: open a read‑only dialog showing each transaction description
  - Refresh: reload accounts list
  - Logout: clear state and return to Auth card
- Currency: display all money as ₹ in human‑readable format (no scientific notation)

Finish Criteria for Section 5:

- You can perform every operation from the GUI that exists in the console UI

---

## 6) Build & Run (No Source Code Shown)

From project root:

Compile everything:

```sh
javac -d out -sourcepath src/main/java \
  src/main/java/com/banking/App.java \
  src/main/java/com/banking/ui/*.java \
  src/main/java/com/banking/services/*.java \
  src/main/java/com/banking/model/*.java
```

Run GUI:

```sh
java -cp out com.banking.App
```

Optional: run console UI instead by creating a console `main` that starts your `ConsoleUI` loop.

---

## 7) Manual Test Scenarios (Checklist)

- Register a new user → login succeeds → a Customer exists
- Create Savings account, deposit, apply interest, view history
- Create Checking account, withdraw into overdraft (within limit), view history
- Deposit then withdraw exact balance → balance returns to zero; two transactions recorded
- Logout → cannot access dashboard; login again restores access and data

---

## 8) Enhancements (When You Want More Practice)

- Input validation: negative amounts, numeric parsing, duplicate account numbers
- Persist data: JSON or simple file save/load
- Interest scheduler: apply interest monthly for all savings accounts
- UI polish: disable buttons when not applicable; improve layout; keyboard shortcuts
- Unit tests for services and model behavior

---

## 9) Version Control (Optional)

Initialize Git, commit, and push to a remote repository (GitHub). Avoid committing build outputs (`out/`, `target/`, `*.class`).

---

## 10) What You Should Be Able To Explain Afterward

- Why `Account` is abstract and what varies in its subclasses
- How transactions record every balance change
- How services isolate business logic from UIs
- How `CardLayout` swaps between auth and dashboard views in Swing
- How and why to format money with a locale‑aware currency formatter

---

Happy practicing! Build it step by step without copying code. Use this document as your single source of truth for requirements and acceptance criteria.
