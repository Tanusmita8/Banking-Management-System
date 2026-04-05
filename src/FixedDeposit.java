package src;

import java.time.LocalDate;

public class FixedDeposit {
    private static int nextFdSeq = 1;

    private final String fdId;
    private final String accountNumber;
    private final double principal;
    private final double interestRate;   // annual %
    private final int    durationMonths;
    private final LocalDate startDate;
    private final LocalDate maturityDate;
    private boolean active;

    public static final double INTEREST_RATE     = 6.5; // fixed annual rate for all FDs
    public static final double PREMATURE_PENALTY = 1.0; // 1% rate deduction for early closure

    public FixedDeposit(String accountNumber, double principal, int durationMonths) {
        this.fdId           = String.format("FD%04d", nextFdSeq++);
        this.accountNumber  = accountNumber;
        this.principal      = principal;
        this.interestRate   = INTEREST_RATE;
        this.durationMonths = durationMonths;
        this.startDate      = LocalDate.now();
        this.maturityDate   = startDate.plusMonths(durationMonths);
        this.active         = true;
    }

    public String getFdId()            { return fdId; }
    public String getAccountNumber()   { return accountNumber; }
    public double getPrincipal()       { return principal; }
    public boolean isActive()          { return active; }
    public LocalDate getStartDate()    { return startDate; }
    public LocalDate getMaturityDate() { return maturityDate; }

    public boolean isPrematureClosure() {
        return LocalDate.now().isBefore(maturityDate);
    }

    /** Simple interest for the full contracted duration at full rate. */
    public double getInterestAmount() {
        return principal * (interestRate / 100.0) * (durationMonths / 12.0);
    }

    public double getMaturityAmount() {
        return principal + getInterestAmount();
    }

    /**
     * Interest for premature closure: calculated on actual months held,
     * at (interestRate - PREMATURE_PENALTY)%.
     */
    public double getPrematureInterest(int monthsHeld) {
        double effectiveRate = Math.max(0, interestRate - PREMATURE_PENALTY);
        return principal * (effectiveRate / 100.0) * (monthsHeld / 12.0);
    }

    public void close() { this.active = false; }

    @Override
    public String toString() {
        return String.format(
            "FD ID: %-8s | A/C: %-8s | Principal: %10.2f | Rate: %5.2f%% | Duration: %3d mo" +
            " | Interest: %8.2f | Maturity Amt: %10.2f | Matures: %s | Status: %s",
            fdId, accountNumber, principal, interestRate, durationMonths,
            getInterestAmount(), getMaturityAmount(), maturityDate, active ? "Active" : "Closed");
    }
}
