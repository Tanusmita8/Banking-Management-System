
# Banking Management System

A Java-based banking management system built using core OOP concepts and Java Collections.

## OOP Concepts Used

- **Encapsulation** – Account fields (`balance`, `accountNumber`, etc.) are private and accessed via getters.
- **Abstraction** – Banking logic is hidden inside `Account`, `Bank`, `FixedDeposit`, and `Payee` classes; `Main` just handles user input.
- **Inheritance** – Can be extended in the future (e.g. SavingsAccount, LoanAccount).

## Java Collections Used

| Collection | Where | Purpose |
|---|---|---|
| `LinkedHashMap<String, Account>` | `Bank` | O(1) account lookup, preserves insertion order for listing |
| `ArrayList<Transaction>` | `Account` | Chronological transaction history per account |
| `HashMap<String, List<FixedDeposit>>` | `Bank` | FDs grouped by account number |
| `HashMap<String, List<Payee>>` | `Bank` | Payees grouped by owner account number |

## Project Structure

```
BankingManagementSystem/
├── src/
│   ├── Account.java       # Account model — deposit, withdraw, transaction history
│   ├── Bank.java          # Core banking logic — accounts, FDs, payees, transfers
│   ├── FixedDeposit.java  # FD model — interest calculation, premature closure
│   ├── Payee.java         # Payee model — saved beneficiaries per account
│   ├── Transaction.java   # Transaction record — type, amount, balance, timestamp
│   └── Main.java          # Entry point with interactive menu
└── out/                   # Compiled .class files
```

## Features

### Account Management
- Create a bank account
- Deposit money
- Withdraw money
- Check account balance
- Transfer funds between accounts
- List all accounts

### Statement
- View full transaction history for any account
- Each entry shows date/time, type (CREDIT/DEBIT), amount, and running balance

### Fixed Deposits (FD)
- Create an FD by locking funds from your account
- Fixed interest rate: **6.5% p.a.** (simple interest)
- Interest amount and maturity amount shown at creation
- List all FDs for an account (active and closed)
- Close an FD:
  - **At maturity** — full interest credited back
  - **Premature closure** — 1% penalty applied; interest recalculated at 5.5% for actual months held only

### Payees
- Add a saved beneficiary (payee) to your account
- List all registered payees
- Remove a payee
- Transfer directly to a payee using their Payee ID (no need to type account number each time)

## FD Interest Calculation

**At maturity:**
```
Interest = Principal × 6.5% × (Duration in months / 12)
```

**Premature closure:**
```
Effective rate = 6.5% − 1% penalty = 5.5%
Interest       = Principal × 5.5% × (Months actually held / 12)
```

**Example — ₹5000 for 12 months:**

| Closed at | Rate | Interest | Amount returned |
|---|---|---|---|
| 0 months  | 5.5% | ₹0.00    | ₹5000.00 |
| 1 month   | 5.5% | ₹22.92   | ₹5022.92 |
| 6 months  | 5.5% | ₹137.50  | ₹5137.50 |
| 12 months (matured) | 6.5% | ₹325.00 | ₹5325.00 |

## Menu

```
===== Banking System =====

 1.  Create account
 2.  Deposit
 3.  Withdraw
 4.  Check balance
 5.  Transfer
 6.  List all accounts

------ Statement ------
 7.  View account statement

------ Fixed Deposits ------
 8.  Create FD
 9.  List FDs
10.  Close FD (credit maturity amount)

------ Payees ------
11.  Add payee
12.  List payees
13.  Remove payee
14.  Transfer to payee

=============================
15.  Exit
```

## How to Run

**Compile (requires Java 17+):**
```bash
javac -d out src/*.java
```

**Run:**
```bash
java -cp out src.Main
```

## Requirements

- Java 17 or higher
