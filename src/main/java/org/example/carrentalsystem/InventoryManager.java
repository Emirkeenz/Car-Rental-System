package org.example.carrentalsystem;
import java.util.Date;

public class InventoryManager extends User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected int id;
    protected Date dataOfBirth;
    protected int phoneNumber;

    public InventoryManager(String userName, String password , String firstName  , String lastName , int id, Date dataOfBirth, String email , int phoneNumber) {
        super(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.dataOfBirth = new Date();
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


}
