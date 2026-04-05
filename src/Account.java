package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private String accountNumber;
    private String holderName;
    private double balance;

    // Statement: ordered list of all transactions on this account
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName    = holderName;
        this.balance       = initialBalance;
        transactions.add(new Transaction("OPEN", initialBalance, initialBalance, "Account opened"));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName()    { return holderName; }
    public double getBalance()       { return balance; }

    public void deposit(double amount) {
        deposit(amount, "Deposit");
    }

    public void deposit(double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        balance += amount;
        transactions.add(new Transaction("CREDIT", amount, balance, description));
        System.out.printf("Deposited: %.2f | New balance: %.2f%n", amount, balance);
    }

    public void withdraw(double amount) {
        withdraw(amount, "Withdrawal");
    }

    public void withdraw(double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
        transactions.add(new Transaction("DEBIT", amount, balance, description));
        System.out.printf("Withdrawn: %.2f | New balance: %.2f%n", amount, balance);
    }

    /** Returns an unmodifiable view of the transaction list (chronological order). */
    public List<Transaction> getStatement() {
        return Collections.unmodifiableList(transactions);
    }

    public void printStatement() {
        System.out.println("=".repeat(90));
        System.out.printf("Account Statement — %s (%s)%n", accountNumber, holderName);
        System.out.println("=".repeat(90));
        System.out.printf("%-17s | %-10s | %10s | %-22s | %s%n",
                "Date & Time", "Type", "Amount", "Balance After", "Description");
        System.out.println("-".repeat(90));
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : transactions) System.out.println(t);
        }
        System.out.println("=".repeat(90));
    }

    @Override
    public String toString() {
        return String.format("Account No: %s%nHolder   : %s%nBalance  : %.2f",
                accountNumber, holderName, balance);
    }
}
