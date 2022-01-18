package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Results.EventIDResult;

import java.sql.Connection;

/**
 * Fulfills the EventID type requests and returns a EventIDResult object
 */

public class EventIDService {

    /**
     * This function handles the eventrequest from the handler
     * @param e eventRequest object
     * @return EventIDResult object that will be converted into a json string eventually
     */
    public EventIDResult getEvent(EventRequest e) {
        Database db = new Database();
        Connection conn;
        AuthToken aToken;
        Event event;

        try {
            conn = db.getConnection();
            EventDAO eDao = new EventDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aToken = aDao.find(e.getAuthToken());
            event = eDao.find(e.getEventID());

            if ((event == null) || (aToken == null)) {
                db.closeConnection(false);
                return new EventIDResult("Error: Event or Token not found", false);
            }

            if (aToken.getUsername().equals(event.getUsername())) {
                db.closeConnection(false);
                return new EventIDResult(event.getUsername(), event.getEventID(), event.getPersonID(),
                event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(),
                event.getYear(), true);
            }
            else {
                db.closeConnection(false);
                return new EventIDResult("Error: EventID does not match AuthToken's assocUser", false);
            }
        } catch (DataAccessException dataAccessException) {
            dataAccessException.printStackTrace();
            try {
                db.closeConnection(false);
                return new EventIDResult("Error: EventIDService failed; DataAccess exception", false);
            } catch (DataAccessException accessException) {
                accessException.printStackTrace();
            }
        }
        return null;
    }
}
