package Results;

import Model.Event;

import java.util.List;

/**
 * Returns ALL events for ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */

public class EventResult extends ParentResult{
    /**
     * Array of Events that will be stored in EventResult object
     */
    List<Event> data;

    /**
     * Constructor for EventResult
     * @param message : message
     * @param success : success boolean
     */

    public EventResult(String message, Boolean success, List<Event> data) {
        super(message, success);
        this.data = data;
    }

    public EventResult(List<Event> data, Boolean success) {
        super(success);
        this.data = data;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
