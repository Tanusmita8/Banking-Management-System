package src;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Bank    bank = new Bank();
    private static Scanner sc   = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Banking System!");

        while (true) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:  handleCreateAccount(); break;
                    case 2:  handleDeposit();       break;
                    case 3:  handleWithdraw();      break;
                    case 4:  handleCheckBalance();  break;
                    case 5:  handleTransfer();      break;
                    case 6:  bank.listAllAccounts(); break;
                    case 7:  handleStatement();     break;
                    case 8:  handleCreateFD();      break;
                    case 9:  handleListFDs();       break;
                    case 10: handleCloseFD();       break;
                    case 11: handleAddPayee();      break;
                    case 12: handleListPayees();    break;
                    case 13: handleRemovePayee();   break;
                    case 14: handleTransferToPayee(); break;
                    case 15:
                        System.out.println("Goodbye!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------ Menu

    private static void printMenu() {
        System.out.println();
        System.out.println("===== Banking System =====\n");
        System.out.println(" 1.  Create account");
        System.out.println(" 2.  Deposit");
        System.out.println(" 3.  Withdraw");
        System.out.println(" 4.  Check balance");
        System.out.println(" 5.  Transfer");
        System.out.println(" 6.  List all accounts");
        System.out.println("\n------ Statement ------\n");
        System.out.println(" 7.  View account statement");
        System.out.println("\n------ Fixed Deposits ------\n");
        System.out.println(" 8.  Create FD");
        System.out.println(" 9.  List FDs");
        System.out.println("10.  Close FD (credit maturity amount)");
        System.out.println("\n------ Payees ------\n");
        System.out.println("11.  Add payee");
        System.out.println("12.  List payees");
        System.out.println("13.  Remove payee");
        System.out.println("14.  Transfer to payee");
        System.out.println("\n=============================\n");
        System.out.println("15.  Exit");
        System.out.println("\n=============================\n");
        System.out.print("Enter choice: ");
    }

    // ------------------------------------------------------------------ Account handlers

    private static void handleCreateAccount() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Initial deposit: ");
        double amount = parseDouble();
        Account acc = bank.createAccount(name, amount);
        System.out.println("Account created. Account number: " + acc.getAccountNumber());
    }

    private static void handleDeposit() {
        Account acc = promptAccount();
        System.out.print("Amount: ");
        acc.deposit(parseDouble());
    }

    private static void handleWithdraw() {
        Account acc = promptAccount();
        System.out.print("Amount: ");
        acc.withdraw(parseDouble());
    }

    private static void handleCheckBalance() {
        System.out.println(promptAccount());
    }

    private static void handleTransfer() {
        System.out.print("From account: ");
        String from = sc.nextLine().trim();
        System.out.print("To account: ");
        String to = sc.nextLine().trim();
        System.out.print("Amount: ");
        bank.transfer(from, to, parseDouble());
    }

    // ------------------------------------------------------------------ Statement handler

    private static void handleStatement() {
        System.out.print("Account number: ");
        String accNo = sc.nextLine().trim();
        bank.printStatement(accNo);
    }

    // ------------------------------------------------------------------ FD handlers

    private static void handleCreateFD() {
        System.out.print("Account number: ");
        String accNo = sc.nextLine().trim();
        System.out.print("FD amount: ");
        double amount = parseDouble();
        System.out.print("Duration (months): ");
        int months = parseInt();
        FixedDeposit fd = bank.createFD(accNo, amount, months);
        System.out.println("FD created successfully:");
        System.out.println(fd);
        System.out.printf("  Interest rate  : %.2f%% p.a. (fixed)%n", FixedDeposit.INTEREST_RATE);
        System.out.printf("  Interest earned: %.2f%n", fd.getInterestAmount());
        System.out.printf("  Maturity amount: %.2f%n", fd.getMaturityAmount());
    }

    private static void handleListFDs() {
        System.out.print("Account number: ");
        String accNo = sc.nextLine().trim();
        List<FixedDeposit> fds = bank.getFDs(accNo);
        if (fds.isEmpty()) {
            System.out.println("No FDs found.");
            return;
        }
        System.out.println("=".repeat(110));
        System.out.println("Fixed Deposits for account: " + accNo.toUpperCase());
        System.out.println("=".repeat(110));
        for (FixedDeposit fd : fds) System.out.println(fd);
        System.out.println("=".repeat(110));
    }

    private static void handleCloseFD() {
        System.out.print("FD ID to close: ");
        String fdId = sc.nextLine().trim();
        bank.closeFD(fdId);
    }

    // ------------------------------------------------------------------ Payee handlers

    private static void handleAddPayee() {
        System.out.print("Your account number: ");
        String ownerAccNo = sc.nextLine().trim();
        System.out.print("Payee name: ");
        String payeeName = sc.nextLine().trim();
        System.out.print("Payee account number: ");
        String payeeAccNo = sc.nextLine().trim();
        Payee payee = bank.addPayee(ownerAccNo, payeeName, payeeAccNo);
        System.out.println("Payee added: " + payee);
    }

    private static void handleListPayees() {
        System.out.print("Your account number: ");
        String ownerAccNo = sc.nextLine().trim();
        List<Payee> payees = bank.getPayees(ownerAccNo);
        if (payees.isEmpty()) {
            System.out.println("No payees registered.");
            return;
        }
        System.out.println("=".repeat(60));
        System.out.println("Payees for account: " + ownerAccNo.toUpperCase());
        System.out.println("=".repeat(60));
        for (Payee p : payees) System.out.println(p);
        System.out.println("=".repeat(60));
    }

    private static void handleRemovePayee() {
        System.out.print("Your account number: ");
        String ownerAccNo = sc.nextLine().trim();
        System.out.print("Payee ID to remove: ");
        String payeeId = sc.nextLine().trim();
        bank.removePayee(ownerAccNo, payeeId);
    }

    private static void handleTransferToPayee() {
        System.out.print("Your account number: ");
        String ownerAccNo = sc.nextLine().trim();
        List<Payee> payees = bank.getPayees(ownerAccNo);
        if (payees.isEmpty()) {
            System.out.println("No payees registered.");
            return;
        }
        System.out.println("Your payees:");
        for (Payee p : payees) System.out.println(p);
        System.out.print("Payee ID: ");
        String payeeId = sc.nextLine().trim();
        System.out.print("Amount: ");
        double amount = parseDouble();
        bank.transferToPayee(ownerAccNo, payeeId, amount);
    }

    // ------------------------------------------------------------------ Helpers

    private static Account promptAccount() {
        System.out.print("Account number: ");
        String accNo = sc.nextLine().trim();
        Account acc = bank.findAccount(accNo);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        return acc;
    }

    private static double parseDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number entered.");
        }
    }

    private static int parseInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer entered.");
        }
    }
}
