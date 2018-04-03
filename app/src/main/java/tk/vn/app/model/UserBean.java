package tk.vn.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by neelp on 28-03-2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String emailId;
    private int credits;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}