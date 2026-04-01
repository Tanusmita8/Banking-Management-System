package src;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts = new ArrayList<>();
    private int nextAccountNumber = 1001;

    public Account createAccount(String holderName, double initialBalance) {
        Account acc = new Account("ACC" + nextAccountNumber++, holderName, initialBalance);
        accounts.add(acc);
        return acc;
    }

    public Account findAccount(String accountNumber) {
        for (Account acc : accounts)
            if (acc.getAccountNumber().equalsIgnoreCase(accountNumber)) return acc;
        return null;
    }

    public void transfer(String fromAccNo, String toAccNo, double amount) {
        Account from = findAccount(fromAccNo);
        Account to   = findAccount(toAccNo);
        if (from == null) throw new IllegalArgumentException("Source account not found.");
        if (to   == null) throw new IllegalArgumentException("Destination account not found.");
        from.withdraw(amount);
        to.deposit(amount);
    }

    public void listAllAccounts() {
        if (accounts.isEmpty()) { System.out.println("No accounts found."); return; }
        for (Account acc : accounts) {
            System.out.println(acc);
            System.out.println("---");
        }
    }
}