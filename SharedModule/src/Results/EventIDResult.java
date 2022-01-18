package Results;

/**
 * Result shows the response to the EventRequest, given a specific EventID
 */

public class EventIDResult extends ParentResult{

    /**
     * The Event's ID
     */
    private String eventID;
    /**
     * The Event's associated user
     */
    private String associatedUsername;
    /**
     * The Event's personID
     */
    private String personID;
    /**
     * The Event's latitude
     */
    private float latitude;
    /**
     * The Event's longitude
     */
    private float longitude;
    /**
     * The Event's country
     */
    private String country;
    /**
     * The Event's city
     */
    private String city;
    /**
     * The Event's eventType
     */
    private String eventType;
    /**
     * The Event's year
     */
    private int year;


    /**
     * Constructor for EventResult
     * @param success : success
     * @param eventID : eventID
     * @param associatedUsername : associatedUsername
     * @param personID : personID
     * @param latitude : latitude
     * @param longitude : longitude
     * @param country : country
     * @param city : city
     * @param eventType : eventType
     * @param year : year
     */

    public EventIDResult(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year, Boolean success) {
        super(success);
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public EventIDResult(String message, Boolean success) {
        super(message, success);
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
