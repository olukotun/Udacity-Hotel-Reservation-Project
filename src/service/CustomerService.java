package service;

import model.Customer;

import java.util.*;

public class CustomerService {
    private final static CustomerService instance = new CustomerService();
    private final Map<String, Customer> customerMap = new HashMap<>();
    private CustomerService(){

    }
    public static CustomerService getInstance(){
        return instance;
    }

    public void addCustomer(String email, String firstName, String lastName){
        customerMap.put(email, new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail){
        return customerMap.get(customerEmail);
    }
    public Collection<Customer> getAllCustomer(){
        return customerMap.values();
    }
}
