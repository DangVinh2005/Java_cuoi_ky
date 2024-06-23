package Server.com.vinh.service;

import Server.com.vinh.connection.DatabaseConnection;
import Server.com.vinh.model.Model_Client;
import Server.com.vinh.model.Model_Login;
import Server.com.vinh.model.Model_Message;
import Server.com.vinh.model.Model_Register;
import Server.com.vinh.model.Model_User_Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ServiceUser {

    private final Connection con;

    public ServiceUser() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public Model_Message register(Model_Register data) {
        Model_Message message = new Model_Message();
        try {
            PreparedStatement p = con.prepareStatement(CHECK_USER);
            p.setString(1, data.getUserName());
            ResultSet r = p.executeQuery();
            if (r.first()) {
                message.setAction(false);
                message.setMessage("User Already Exists");
            } else {
                message.setAction(true);
            }
            r.close();
            p.close();
            if (message.isAction()) {
                con.setAutoCommit(false);
                p = con.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                p.setString(1, data.getUserName());
                String encodedPassword = Base64.getEncoder().encodeToString(data.getPassword().getBytes());
                p.setString(2, encodedPassword);
                p.execute();
                r = p.getGeneratedKeys();
                r.first();
                int userID = r.getInt(1);
                r.close();
                p.close();
                p = con.prepareStatement(INSERT_USER_ACCOUNT);
                p.setInt(1, userID);
                p.setString(2, data.getUserName());
                p.execute();
                p.close();
                con.commit();
                con.setAutoCommit(true);
                message.setAction(true);
                message.setMessage("Ok");
                message.setData(new Model_User_Account(userID, data.getUserName(), "", "", true));
            }
        } catch (SQLException e) {
            message.setAction(false);
            message.setMessage("Server Error");
            try {
                if (!con.getAutoCommit()) {
                    con.rollback();
                    con.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return message;
    }

    public Model_User_Account login(Model_Login login) throws SQLException {
        Model_User_Account data = null;
        PreparedStatement p = con.prepareStatement(LOGIN);
        p.setString(1, login.getUserName());
        ResultSet r = p.executeQuery();
        if (r.first()) {
            int userID = r.getInt(1);
            String userName = r.getString(2);
            String gender = r.getString(3);
            String image = r.getString(4);
            String encodedPassword = r.getString(5);
            String inputPassword = Base64.getEncoder().encodeToString(login.getPassword().getBytes());
            if (encodedPassword.equals(inputPassword)) {
                data = new Model_User_Account(userID, userName, gender, image, true);
            }
        }
        r.close();
        p.close();
        return data;
    }

    public List<Model_User_Account> getUser(int exitUser) throws SQLException {
        List<Model_User_Account> list = new ArrayList<>();
        PreparedStatement p = con.prepareStatement(SELECT_USER_ACCOUNT);
        p.setInt(1, exitUser);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            int userID = r.getInt(1);
            String userName = r.getString(2);
            String gender = r.getString(3);
            String image = r.getString(4);
            list.add(new Model_User_Account(userID, userName, gender, image, checkUserStatus(userID)));
        }
        r.close();
        p.close();
        return list;
    }

    private boolean checkUserStatus(int userID) {
        List<Model_Client> clients = Service.getInstance(null).getListClient();
        for (Model_Client c : clients) {
            if (c.getUser().getUserID() == userID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(int userID) throws SQLException {
        String query = "SELECT Status FROM user WHERE UserID = ?";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, userID);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    int status = r.getInt("Status");
                    return status == 0;
                }
            }
        }
        return false;
    }

    public List<Model_User_Account> getAllAccounts() throws SQLException {
        List<Model_User_Account> accounts = new ArrayList<>();
        String query = "SELECT UserID, UserName, Gender, ImageString FROM user_account";
        PreparedStatement p = con.prepareStatement(query);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            int userID = r.getInt("UserID");
            String userName = r.getString("UserName");
            String gender = r.getString("Gender");
            String imageString = r.getString("ImageString");
            accounts.add(new Model_User_Account(userID, userName, gender, imageString, checkUserStatus(userID)));
        }
        r.close();
        p.close();
        return accounts;
    }

    private final String LOGIN = "SELECT UserID, user_account.UserName, Gender, ImageString, Password FROM `user` JOIN user_account USING (UserID) WHERE `user`.UserName = ? AND user_account.`Status` = '1'";
    private final String SELECT_USER_ACCOUNT = "SELECT UserID, UserName, Gender, ImageString FROM user_account WHERE user_account.`Status` = '1' AND UserID <> ?";
    private final String INSERT_USER = "INSERT INTO user (UserName, `Password`) VALUES (?, ?)";
    private final String INSERT_USER_ACCOUNT = "INSERT INTO user_account (UserID, UserName) VALUES (?, ?)";
    private final String CHECK_USER = "SELECT UserID FROM user WHERE UserName = ? LIMIT 1";
}
