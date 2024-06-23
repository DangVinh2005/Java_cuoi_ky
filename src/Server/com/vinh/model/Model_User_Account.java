package Server.com.vinh.model;

public class Model_User_Account {

    private int userID;
    private String userName;
    private String gender;
    private String image;
    private boolean status;
    private boolean isAdmin;  // Thêm trường này

    public Model_User_Account(int userID, String userName, String gender, String image, boolean status, boolean isAdmin) {
        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
        this.image = image;
        this.status = status;
        this.isAdmin = isAdmin;
    }

    public Model_User_Account(int userID, String userName, String gender, String image, boolean status) {
        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
        this.image = image;
        this.status = status;
        this.isAdmin = false; // Mặc định không phải admin
    }

    public Model_User_Account() {
    }

    // Getters and setters for all fields

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAdmin() { // Thêm getter cho isAdmin
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) { // Thêm setter cho isAdmin
        this.isAdmin = isAdmin;
    }
}
