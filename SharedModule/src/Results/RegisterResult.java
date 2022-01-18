package Results;

/**
 * Components needed for Register Response Body, this will be used to convert to JSON
 */

public class RegisterResult extends ParentResult {

    /**
     * Register Result's AuthToken
     */
    String authtoken;
    /**
     * Register Result's Username
     */
    String username;
    /**
     * Register Result's PersonID
     */
    String personID;

    /**
     * Constructor for Success Response Body register result
     * @param success : success boolean
     * @param authToken : authToken
     * @param username : username
     * @param personID : personID
     */
    public RegisterResult(String authToken, String username, String personID, Boolean success) {
        super(success);
        this.authtoken = authToken;
        this.username = username;
        this.personID = personID;
    }

    /**
     * Constructor for Error Response Body register result
     * @param message
     * @param success
     */
    public RegisterResult(String message, Boolean success) {
        super(message, success);
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
