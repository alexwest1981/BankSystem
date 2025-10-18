package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        bank.loadFromFile();

        Scanner scanner =  new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Bankmeny ---");
            System.out.println("1. Registrera kund");
            System.out.println("2. Visa alla kunder");
            System.out.println("3. Skapa konto för kund");
            System.out.println("4. Avsluta");
            System.out.print("Välj ett alternativ: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Ange kund-id: ");
                    String custId = scanner.nextLine().trim();
                    System.out.print("Ange kundens namn: ");
                    String custName = scanner.nextLine().trim();
                    System.out.print("Ange personnummer: ");
                    String personalNumber = scanner.nextLine().trim();
                    System.out.print("Ange adress: ");
                    String address = scanner.nextLine().trim();
                    System.out.print("Ange email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("Ange telefonnummer: ");
                    String phone = scanner.nextLine().trim();

                    if (bank.findCustomerById(custId) != null) {
                        System.out.println("Kund-id finns redan!");
                    } else  {
                        // Använder nu metoden som tar alla fält
                        bank.registerCustomer(custId, custName, personalNumber, address, email, phone);
                        bank.saveToFile();
                        System.out.println("Kund registrerad.");
                    }
                    break;
                case "2":
                    bank.printAllCustomers();
                    break;
                case "3":
                    System.out.print("Ange kund-id för konto: ");
                    String customerId = scanner.nextLine().trim();
                    Customer customer = bank.findCustomerById(customerId);
                    if (customer == null) {
                        System.out.println("Ingen kund med det id:t");
                        break;
                    }

                    System.out.print("Ange nytt kontonummer: ");
                    String accNum = scanner.nextLine().trim();
                    if (bank.accountNumberExists(accNum)) {
                        System.out.println("Kontonummer finns redan, välj annat.");
                        break;
                    }
                    System.out.print("Ange räntesats (t.ex. 0.01 för 1%): ");
                    double rate;
                    try {
                        rate = Double.parseDouble(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Felaktigt format på räntesatsen.");
                        break;
                    }

                    System.out.print("Ange clearingnummer: ");
                    String clearingNumber = scanner.nextLine().trim();

                    customer.openAccount(accNum, rate, clearingNumber);
                    bank.saveToFile();
                    System.out.println("Konto skapat.");
                    break;
                case "4":
                    System.out.println("Avslutar...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Felaktigt val.");
            }
        }
    }
}
