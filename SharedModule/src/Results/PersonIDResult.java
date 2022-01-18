package Results;

/**
 * Result of personRequest (java object)
 */
public class PersonIDResult extends ParentResult{
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person ID: Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * First Name: Person’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: Person’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: Person’s gender (string: “f” or “m”)
     */
    private String gender;
    /**
     * Father ID: Person ID of person’s father (possibly null)
     */
    private String fatherID;
    /**
     * Mother ID: Person ID of person’s mother (possibly null)
     */
    private String motherID;
    /**
     * Spouse ID: Person ID of person’s spouse (possibly null)
     */
    private String spouseID;

    /**
     * Constructor for PersonResult
     * @param associatedUsername : associatedUsername
     * @param personID : personID
     * @param firstName : firstName
     * @param lastName : lastName
     * @param gender : gender
     * @param fatherID : fatherID
     * @param motherID : motherID
     * @param spouseID : spouseID
     */
    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, Boolean success) {
        super(success);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName, String gender, Boolean success) {
        super(success);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, Boolean success) {
        super(success);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
    }


    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String spouseID, Boolean success) {
        super(success);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.spouseID = spouseID;
    }


    public PersonIDResult(String message, Boolean success) {
        super(message, success);
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
