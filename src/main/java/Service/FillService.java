package Service;

import DataAccess.*;
import Model.*;
import Requests.FillRequest;
import Results.FillResult;
import TempDatabase.LocationData;
import HelperClasses.MakeLocationDB;
import HelperClasses.RandomNumGenerator;
import HelperClasses.TreeGenerationAlgorithm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 *  Populates the server's database with generated data for the specified user name.
 *  The required "username" parameter must be a user already registered with the server.
 *  If there is any data in the database already associated with the given user name, it is deleted.
 *  The optional “generations” parameter lets the caller specify the number of generations of ancestors to be generated,
 *  and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 */
public class FillService {
    MakeLocationDB makeLocationdb = new MakeLocationDB();
    /**
     * Populates the server's database with generated data for the specified user name.
     * @param r gets the fillRequest from the Fillhandler
     * @return the java response object from FillResult class that we need to next convert into Json String
     */
    public FillResult fill(FillRequest r){
        Database db = new Database();
        User user;
        TreeGenerationAlgorithm familyTreeBuilder = new TreeGenerationAlgorithm();

        try {
            // Find the user specified by the fill request
            Connection conn = db.getConnection();
            UserDAO uDao = new UserDAO(conn);
            user = uDao.find(r.getUsername());

            //See if user object is null
            if(user == null) {
                db.closeConnection(false);
                return new FillResult("Error: User not found", false);
            }

            db.closeConnection(false);
            // If the user was found, recycle (delete person object and events) them
            recycleUser(user);
            // Create a new birth event for the user; create a new connection; upload to database
            conn = db.getConnection();
            EventDAO eDao = new EventDAO(conn);
            Event birth = createBirthEvent(user);
            eDao.insert(birth);
            db.closeConnection(true);

            //Create new Person; uploaded in TreeGenerationAlgo
            Person person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender());

            //Generate Ancestry
            familyTreeBuilder.generateNamesDatabase();
            familyTreeBuilder.generateLocationDatabase();
            familyTreeBuilder.generateParents(person, user, birth.getYear(), r.getGenerations());

            return new FillResult("Successfully added " + familyTreeBuilder.getPersonListSize() +
                    " persons and " + familyTreeBuilder.getEventListSize() + " events to the database", true);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
            e.printStackTrace();
            return new FillResult("Error: Your fill request has failed", false);
        }
    }

    /**
     * Create a new birth event for the user, given the user
     * @param user : user specified in the fill request
     * @return a new birth event
     */
    private Event createBirthEvent(User user) {
        //Create new birthEvent
        try {
            LocationData locDB = makeLocationdb.generate();
            String EventID = UUID.randomUUID().toString();
            Location newLoc = locDB.getLocation();
            RandomNumGenerator rng = new RandomNumGenerator();
            int randomYear = rng.getRandomNumber(1920, 2008);

            return new Event(EventID, user.getUsername(), user.getPersonID(),
                    newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getCountry(),
                    newLoc.getCity(), "birth", randomYear);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete a user's Person and all of their Events
     * @param user : the user we want to recycle
     */
    private void recycleUser(User user) {
        Database db = new Database();
        Connection conn;
        try {
            conn = db.getConnection();
            EventDAO eDao = new EventDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);

            pDao.deletePerson(user.getUsername());
            eDao.deleteEvents(user.getUsername());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
