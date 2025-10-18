package org.example;

public class CustomerService {
    private Bank bank = Bank.getInstance();

    public boolean registerCustomer(String id, String name, String pnr, String address, String email, String phone) {
        if (bank.findCustomerById(id) != null) return false;
        bank.registerCustomer(id, name, pnr, address, email, phone);
        bank.saveToFile();
        return true;
    }

    public Customer findCustomer(String id) {
        return bank.findCustomerById(id);
    }

    public boolean removeCustomer(String id) {
        boolean removed = bank.removeCustomerById(id);
        if (removed) bank.saveToFile();
        return removed;
    }
}