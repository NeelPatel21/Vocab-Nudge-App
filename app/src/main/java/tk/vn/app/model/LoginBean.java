package tk.vn.app.model;

/**
 * Created by neelp on 28-03-2018.
 */

public class LoginBean {

    private String userName;
    private String password;

    public LoginBean(){}

    public LoginBean(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}