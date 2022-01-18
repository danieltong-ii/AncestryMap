package Model;

/**
 *  This is the User class. We will use it to create User objects.
 */

public class User {

    /**
     * Username: Unique username (non-empty string)
     */
    private String username;
    /**
     * Password: User’s password (non-empty string)
     */
    private String password;
    /**
     * Email: User’s email address (non-empty string)
     */
    private String email;
    /**
     * First Name: User’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: User’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: User’s gender (string: “f” or “m”)
     */
    private String gender;
    /**
     * Unique Person ID assigned to this user’s generated Person object - see Family History Information section for details (non-empty string)
     */
    private String personID;

    /**
     * Public constructor for a User object
      * @param username : username
     * @param password : password
     * @param email : email
     * @param firstname : firstname
     * @param lastname : lastname
     * @param gender : gender
     * @param personId : personId
     */
    public User(String username, String password, String email, String firstname, String lastname, String gender, String personId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.personID = personId;
    }

    /**
     * Empty constructor
     */
    public User(){}

    /**
     * The overridden equals function allows us to test if two objects of this class are identical
     * @param o a generic object (due to inheriting from Object class
     * @return a boolean that determines whether or not the two User objects are equal
     */

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals (getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
