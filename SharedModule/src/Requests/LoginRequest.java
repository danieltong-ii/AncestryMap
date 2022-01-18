package Requests;

/**
 * A user's request to login to the app.
 */

public class LoginRequest {

    /**
     * User's username
     */
    private String username;
    /**
     * User's password
     */
    private String password;

    /**
     * Constructor for LoginRequest
     * @param Username : login's username
     * @param Password : login's password
     */
    public LoginRequest(String Username, String Password) {
        this.username = Username;
        this.password = Password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
