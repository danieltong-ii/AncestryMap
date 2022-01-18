package Service;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.LoadResult;

import java.sql.Connection;

/**
 * Description: Clears all data from the database (just like the /clear API),
 * and then loads the posted user, person, and event data into the database.
 */

public class LoadService {

    /**
     * Fulfills the loadRequest by clearing all data from the database and loading the data uploaded by the user
     * @param l gets the LoadRequest from the LoadHandler
     * @return the java response object from LoadResult class that we need to next convert into Json String
     */
    public LoadResult load(LoadRequest l) {
        Database db = new Database();
        try {
            Connection conn = db.getConnection();
            db.clearTables();
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            for (User user : l.getUsers()) {
                uDao.insert(user);
            }
            for (Person person : l.getPersons()) {
                pDao.insert(person);
            }
            for (Event event : l.getEvents()) {
                eDao.insert(event);
            }
            db.closeConnection(true);

            String SUCCESS_MSG_TEMPLATE = "Successfully added %d users, %d persons, and %d events to the database.";
            String actual_msg = String.format(SUCCESS_MSG_TEMPLATE,l.getUsers().size(),l.getPersons().size(),l.getEvents().size());
            return new LoadResult(actual_msg, true);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
            e.printStackTrace();
            return new LoadResult("Error: Could not complete load service", true);
        }
    }
}
