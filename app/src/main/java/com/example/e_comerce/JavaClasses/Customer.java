package com.example.e_comerce.JavaClasses;

public class Customer extends User {
    public  int id;
    public date dateOfBirth;


    public Customer(String UserName,String email, String Password, date dateOfBirth) {
        super(UserName, Password,email);
        this.dateOfBirth = dateOfBirth;
    }





}
