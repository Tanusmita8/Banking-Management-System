# Banking Management System

A simple Java-based banking management system built using core OOP concepts.

## OOP Concepts Used

- **Encapsulation** – Account fields (`balance`, `accountNumber`, etc.) are private and accessed via getters.
- **Abstraction** – Banking logic is hidden inside `Account` and `Bank` classes; `Main` just handles user input.
- **Inheritance** – Can be extended in the future (e.g. SavingsAccount, LoanAccount).

## Project Structure

```
BankingManagementSystem/
├── src/
│   ├── Account.java   # Account model with deposit/withdraw logic
│   ├── Bank.java      # Manages accounts and transfers
│   └── Main.java      # Entry point with interactive menu
└── out/               # Compiled .class files
```

## Features

- Create a bank account
- Deposit money
- Withdraw money
- Check account balance
- Transfer funds between accounts
- List all accounts

## How to Run

**Compile:**
```bash
javac -d out src/*.java
```

**Run:**
```bash
java -cp out src.Main
```

## Requirements

- Java 11 or higher
