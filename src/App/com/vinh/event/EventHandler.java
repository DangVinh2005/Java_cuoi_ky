package App.com.vinh.event;

import Server.com.vinh.model.Model_Login;
import Server.com.vinh.model.Model_User_Account;
import Server.com.vinh.service.ServiceUser;

import java.sql.SQLException;

public class EventHandler {

    private ServiceUser service;

    public EventHandler() {
        this.service = new ServiceUser();
    }

    public void login(Model_Login data) {
        System.out.println("Sending login request to server...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Model_User_Account account = service.login(data);
                    if (account != null) {
                        System.out.println("Login successful for user: " + account.getUserName());
                        if (account.isAdmin()) {
                            System.out.println("User is an admin.");
                            // Xử lý logic cho quản trị viên ở đây
                        } else {
                            System.out.println("User is a regular user.");
                            // Xử lý logic cho người dùng thông thường ở đây
                        }
                    } else {
                        System.out.println("Login failed: Invalid credentials or user not active.");
                    }
                } catch (SQLException e) {
                    System.out.println("Server Error: " + e.getMessage());
                }
            }
        }).start();
    }
}
