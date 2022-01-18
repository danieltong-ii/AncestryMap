package Requests;

/**
 * A request to get one person from the db
 */

public class PersonRequest {

    /**
     * The Id of the person the user requested
     */
    private String personID;
    /**
     * The user's authtoken
     */
    private String authToken;

    /**
     * Constructor for Person Request with a specific personID attached
     * @param personID : user's personID
     * @param authToken : user's authtoken

     */
    public PersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
