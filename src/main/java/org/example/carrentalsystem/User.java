package org.example.carrentalsystem;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected int userId;
    protected String userName;
    protected String password;
    protected Role role;
    private List<Car> rents; // Список машин в аренде

    public User(int userId, String userName, String password, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.rents = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Car> getRents() {
        return rents;
    }

    public void setRents(List<Car> rents) {
        this.rents = rents;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", rents=" + rents +
                '}';
    }
}

