package Results;

/**
 * Components needed for Login Response Body, this will be used to convert to JSON and sent back to the user
 */

public class LoginResult extends ParentResult{

    /**
     * Login Result's authToken
     */
    private String authtoken;
    /**
     * Login Result's Username
     */
    private String username;
    /**
     * Login Result's PersonID
     */
    private String personID;

    /**
     * Constructor for LoginResult
     * @param message : message
     * @param success : success
     */
    public LoginResult(String message, Boolean success) {
        super(message, success);
    }

    public LoginResult(String authToken, String username, String personID, Boolean success) {
        super(success);
        this.authtoken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
