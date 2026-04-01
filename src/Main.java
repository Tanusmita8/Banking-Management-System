package src;

import java.util.Scanner;

public class Main {

    private static Bank bank = new Bank();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Banking System!");

        while (true) {
            System.out.println("\n1. Create account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check balance");
            System.out.println("5. Transfer");
            System.out.println("6. List all accounts");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Initial deposit: ");
                    Account acc = bank.createAccount(name, Double.parseDouble(sc.nextLine().trim()));
                    System.out.println("Account created. Account number: " + acc.getAccountNumber());
                }
                case 2 -> {
                    System.out.print("Account number: ");
                    Account acc = bank.findAccount(sc.nextLine().trim());
                    if (acc == null) { System.out.println("Account not found."); break; }
                    System.out.print("Amount: ");
                    try { acc.deposit(Double.parseDouble(sc.nextLine().trim())); }
                    catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
                }
                case 3 -> {
                    System.out.print("Account number: ");
                    Account acc = bank.findAccount(sc.nextLine().trim());
                    if (acc == null) { System.out.println("Account not found."); break; }
                    System.out.print("Amount: ");
                    try { acc.withdraw(Double.parseDouble(sc.nextLine().trim())); }
                    catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
                }
                case 4 -> {
                    System.out.print("Account number: ");
                    Account acc = bank.findAccount(sc.nextLine().trim());
                    if (acc == null) { System.out.println("Account not found."); break; }
                    System.out.println(acc);
                }
                case 5 -> {
                    System.out.print("From account: ");
                    String from = sc.nextLine().trim();
                    System.out.print("To account: ");
                    String to = sc.nextLine().trim();
                    System.out.print("Amount: ");
                    try { bank.transfer(from, to, Double.parseDouble(sc.nextLine().trim())); }
                    catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
                }
                case 6 -> bank.listAllAccounts();
                case 7 -> { System.out.println("Goodbye!"); sc.close(); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}