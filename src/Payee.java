package src;

public class Payee {
    private static int nextPayeeSeq = 1;

    private final String payeeId;
    private final String name;
    private final String accountNumber;

    public Payee(String name, String accountNumber) {
        this.payeeId       = String.format("PAY%04d", nextPayeeSeq++);
        this.name          = name;
        this.accountNumber = accountNumber;
    }

    public String getPayeeId()       { return payeeId; }
    public String getName()          { return name; }
    public String getAccountNumber() { return accountNumber; }

    @Override
    public String toString() {
        return String.format("Payee ID: %-8s | Name: %-20s | Account: %s",
                payeeId, name, accountNumber);
    }
}
