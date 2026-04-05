package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime dateTime;
    private final String description;

    public Transaction(String type, double amount, double balanceAfter, String description) {
        this.type        = type;
        this.amount      = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.dateTime    = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%-17s | %-10s | %10.2f | Balance: %10.2f | %s",
                dateTime.format(FMT), type, amount, balanceAfter, description);
    }
}
