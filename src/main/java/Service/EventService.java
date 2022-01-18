package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Results.EventResult;

import java.sql.Connection;
import java.util.List;

/**
 * Fulfills the Event requests and returns a EventResult object
 */

public class EventService {

    /**
     * Interprets the request and returns a EventResult object
     * @param eRequest : the EventRequest object
     * @return EventResult object that we need to convert
     */
    public EventResult getEvents(EventRequest eRequest) {
        Database db = new Database();
        List<Event> eventList;

        try {
            Connection conn = db.getConnection();
            EventDAO eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            AuthToken token = aDao.find(eRequest.getAuthToken());
            if (token == null) {
                db.closeConnection(false);
                return new EventResult("Error: no events found associated with this token", false, null);
            }
            String assocUser = token.getUsername();
            eventList = eDao.findEventsAssociatedWithUser(assocUser);

            if (!eventList.isEmpty()) {
                db.closeConnection(false);
                return new EventResult(eventList, true);
            }
            else {
                db.closeConnection(false);
                return new EventResult("Error: no events found associated with this token", false, null);
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new EventResult("Error: no events found associated with this token", false, null);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }
}