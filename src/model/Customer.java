package model;

import java.util.regex.Pattern;

public class Customer {
//^ mean starting () mean capture input . mean anyCharacter + mean one or more @ mean followed by that !@!#!@#222222@324234323.com
    private final String emailRegex = "^(.+)@(.+).com$";
    private  final Pattern pattern = Pattern.compile(emailRegex);
    private String lastName;
    private String firstName;
    private String email;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Customer(String lastName, String firstName, String email) {
       if(!pattern.matcher(email).matches()){
           throw new IllegalArgumentException("Error, Invalid email");
       }
        // todo: validate email using regex
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
