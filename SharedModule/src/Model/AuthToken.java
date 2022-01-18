package Model;

import java.util.UUID;

/**
 * This is the Authentication Token class. It creates objects that represent an authorization token given to a user when they log in.
 */

public class AuthToken {

    /**
     * The username associated with the token
     */
    private String username;
    /**
     * The string that is a string representation of the token
     */
    private String authtoken;

    /**
     * Constructor for the token
     * @param token : the token string
     * @param username : username associated with token
     */
    public AuthToken(String token, String username) {
        this.username = username;
        this.authtoken = token;
    }

    /**
     * Secondary constructor for token. Allows for creating a token using just the username
     * @param username
     */
    public AuthToken(String username) {
        this.username = username;
    }

    public AuthToken() {

    }

    /**
     * generate a new authToken String for the AuthToken object
     */
    public void generateAuthToken() {
         this.authtoken = UUID.randomUUID().toString();
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


    /**
     * The overridden equals function allows us to test if two objects of this class are identical
     * @param o a generic object (due to inheriting from Object class)
     * @return a boolean that determines whether or not the two Token objects are equal
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }
}
