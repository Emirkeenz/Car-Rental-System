package org.example.carrentalsystem;

public class UserSession {
    private static UserSession instance; // Singleton instance
    private static int userId;                  // Holds current user ID
    private static Role userRole;               // Holds user role

    private UserSession() {
        // Private constructor to prevent external instantiation
    }

    // Get the singleton instance
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getter and Setter for User ID
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for Role
    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    // Clear session when logging out
    public static void clearSession() {
        userId = -1;
        userRole = null;
    }
}
