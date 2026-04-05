package src;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    // LinkedHashMap preserves insertion order for listing while giving O(1) lookup
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private int nextAccountNumber = 1001;

    // FDs grouped by account number: HashMap<accountNo, list-of-FDs>
    private final Map<String, List<FixedDeposit>> fdsByAccount = new HashMap<>();

    // Payees grouped by owner account: HashMap<ownerAccountNo, list-of-Payees>
    private final Map<String, List<Payee>> payeesByAccount = new HashMap<>();

    // ------------------------------------------------------------------ Accounts

    public Account createAccount(String holderName, double initialBalance) {
        String accNo = "ACC" + nextAccountNumber++;
        Account acc = new Account(accNo, holderName, initialBalance);
        accounts.put(accNo.toUpperCase(), acc);
        fdsByAccount.put(accNo.toUpperCase(), new ArrayList<>());
        payeesByAccount.put(accNo.toUpperCase(), new ArrayList<>());
        return acc;
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber.toUpperCase());
    }

    public void transfer(String fromAccNo, String toAccNo, double amount) {
        Account from = findAccount(fromAccNo);
        Account to   = findAccount(toAccNo);
        if (from == null) throw new IllegalArgumentException("Source account not found.");
        if (to   == null) throw new IllegalArgumentException("Destination account not found.");
        from.withdraw(amount, "Transfer to " + to.getAccountNumber());
        to.deposit(amount,   "Transfer from " + from.getAccountNumber());
    }

    public void listAllAccounts() {
        if (accounts.isEmpty()) { System.out.println("No accounts found."); return; }
        for (Account acc : accounts.values()) {
            System.out.println(acc);
            System.out.println("---");
        }
    }

    // ------------------------------------------------------------------ Statement

    public void printStatement(String accountNumber) {
        Account acc = findAccount(accountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        acc.printStatement();
    }

    // ------------------------------------------------------------------ Fixed Deposits

    public FixedDeposit createFD(String accountNumber, double amount, int durationMonths) {
        Account acc = findAccount(accountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        if (amount <= 0)         throw new IllegalArgumentException("FD amount must be positive.");
        if (durationMonths <= 0) throw new IllegalArgumentException("Duration must be at least 1 month.");
        if (acc.getBalance() < amount)
            throw new IllegalArgumentException("Insufficient balance to create FD.");

        acc.withdraw(amount, "FD created");
        FixedDeposit fd = new FixedDeposit(accountNumber.toUpperCase(), amount, durationMonths);
        fdsByAccount.get(accountNumber.toUpperCase()).add(fd);
        return fd;
    }

    /** Lists all FDs for the given account (active + closed). */
    public List<FixedDeposit> getFDs(String accountNumber) {
        Account acc = findAccount(accountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        return Collections.unmodifiableList(fdsByAccount.get(accountNumber.toUpperCase()));
    }

    /**
     * Closes an FD by fdId.
     * - If closed on/after maturity date: credits full maturity amount.
     * - If closed before maturity date: premature closure — interest recalculated
     *   at (rate - 1%) for actual months held; penalty amount is shown.
     */
    public void closeFD(String fdId) {
        for (List<FixedDeposit> fds : fdsByAccount.values()) {
            for (FixedDeposit fd : fds) {
                if (fd.getFdId().equalsIgnoreCase(fdId)) {
                    if (!fd.isActive()) throw new IllegalArgumentException("FD is already closed.");

                    fd.close();
                    Account acc = findAccount(fd.getAccountNumber());

                    if (fd.isPrematureClosure()) {
                        // Months actually held (minimum 0)
                        long daysHeld   = ChronoUnit.DAYS.between(fd.getStartDate(), LocalDate.now());
                        int  monthsHeld = (int) (daysHeld / 30);

                        double interest   = fd.getPrematureInterest(monthsHeld);
                        double payout     = fd.getPrincipal() + interest;
                        double penalty    = fd.getInterestAmount() - interest; // interest lost

                        acc.deposit(payout, "FD premature closure: " + fd.getFdId());

                        System.out.println("WARNING: FD closed before maturity (premature closure).");
                        System.out.printf("  Months held          : %d%n", monthsHeld);
                        System.out.printf("  Effective rate       : %.2f%% (%.2f%% - %.2f%% penalty)%n",
                                FixedDeposit.INTEREST_RATE - FixedDeposit.PREMATURE_PENALTY,
                                FixedDeposit.INTEREST_RATE, FixedDeposit.PREMATURE_PENALTY);
                        System.out.printf("  Interest earned      : %.2f%n", interest);
                        System.out.printf("  Interest forfeited   : %.2f%n", penalty);
                        System.out.printf("  Amount credited      : %.2f to %s%n", payout, fd.getAccountNumber());
                    } else {
                        double maturity = fd.getMaturityAmount();
                        acc.deposit(maturity, "FD matured: " + fd.getFdId());
                        System.out.printf("FD %s matured. Amount %.2f credited to %s.%n",
                                fd.getFdId(), maturity, fd.getAccountNumber());
                    }
                    return;
                }
            }
        }
        throw new IllegalArgumentException("FD not found: " + fdId);
    }

    // ------------------------------------------------------------------ Payees

    public Payee addPayee(String ownerAccountNumber, String payeeName, String payeeAccountNumber) {
        Account owner = findAccount(ownerAccountNumber);
        if (owner == null) throw new IllegalArgumentException("Owner account not found.");
        if (findAccount(payeeAccountNumber) == null)
            throw new IllegalArgumentException("Payee account not found.");
        if (ownerAccountNumber.equalsIgnoreCase(payeeAccountNumber))
            throw new IllegalArgumentException("Cannot add your own account as a payee.");

        List<Payee> payees = payeesByAccount.get(ownerAccountNumber.toUpperCase());
        for (Payee p : payees)
            if (p.getAccountNumber().equalsIgnoreCase(payeeAccountNumber))
                throw new IllegalArgumentException("Payee already exists.");

        Payee payee = new Payee(payeeName, payeeAccountNumber.toUpperCase());
        payees.add(payee);
        return payee;
    }

    public List<Payee> getPayees(String ownerAccountNumber) {
        Account acc = findAccount(ownerAccountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        return Collections.unmodifiableList(payeesByAccount.get(ownerAccountNumber.toUpperCase()));
    }

    public void removePayee(String ownerAccountNumber, String payeeId) {
        Account acc = findAccount(ownerAccountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        List<Payee> payees = payeesByAccount.get(ownerAccountNumber.toUpperCase());
        boolean removed = payees.removeIf(p -> p.getPayeeId().equalsIgnoreCase(payeeId));
        if (!removed) throw new IllegalArgumentException("Payee not found: " + payeeId);
        System.out.println("Payee " + payeeId + " removed.");
    }

    /** Transfer from ownerAccount to one of its registered payees. */
    public void transferToPayee(String ownerAccountNumber, String payeeId, double amount) {
        Account acc = findAccount(ownerAccountNumber);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        List<Payee> payees = payeesByAccount.get(ownerAccountNumber.toUpperCase());
        for (Payee p : payees) {
            if (p.getPayeeId().equalsIgnoreCase(payeeId)) {
                transfer(ownerAccountNumber, p.getAccountNumber(), amount);
                return;
            }
        }
        throw new IllegalArgumentException("Payee not found: " + payeeId);
    }
}
