package Requests;

/**
 * An Java object version of the json HTTP request the server received
 */

public class EventRequest {

    /**
     * EventID
     */
    private String eventID;
    /**
     * String with authtoken
     */
    private String authToken;

    /**
     * Constructor for EventRequest
     * @param eventId EventID
     * @param authToken AuthToken
     */
    public EventRequest(String eventId, String authToken) {
        this.eventID = eventId;
        this.authToken = authToken;
    }

    /**
     * Constructor for EventRequest (No specific EventID given, return ALL events)
     * @param authToken
     */
    public EventRequest(String authToken) {
        this.eventID = null;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
