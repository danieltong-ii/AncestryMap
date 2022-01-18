package Service;
import DataAccess.*;
import Model.*;
import Requests.RegisterRequest;
import Results.RegisterResult;
import TempDatabase.LocationData;
import HelperClasses.MakeLocationDB;
import HelperClasses.RandomNumGenerator;
import HelperClasses.TreeGenerationAlgorithm;

import java.io.*;
import java.sql.Connection;
import java.util.UUID;

/**
 * Responsible for implementing user registration
 * Receives a registerRequest object from the handler and returns a registerResult
 */

public class RegisterService {
    private Database db = new Database();

    private LocationData locDB = new LocationData();

    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    private void generateLocationDatabase() {
        MakeLocationDB locDBMaker = new MakeLocationDB();
        try {
            locDB = locDBMaker.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Person newPerson(User user) {
        return new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender());
    }

    private Event newBirthEvent(User user) {
        generateLocationDatabase();
        String EventID = generateUniqueID();
        Location newLoc = locDB.getLocation();
        RandomNumGenerator rng = new RandomNumGenerator();
        int randomYear = rng.getRandomNumber(1920, 2008);

        return new Event(EventID, user.getUsername(), user.getPersonID(),
                newLoc.getLatitude(), newLoc.getLongitude(), newLoc.getCountry(),
                newLoc.getCity(), "birth", randomYear);
    }

    private User newUser(RegisterRequest rr){
        return new User(rr.getUsername(), rr.getPassword(), rr.getEmail(), rr.getFirstName(), rr.getLastName(), rr.getGender(), generateUniqueID());
    }


    /**
     * This function receives a RegisterRequest, handles it, and returns a registerResult object
     *
     * @param rr the registerRequest
     * @return the RegisterResult, which will be converted into json and sent back to the user
     */
    public RegisterResult register(RegisterRequest rr) throws DataAccessException {
        int GENERATIONS_REQUESTED = 4;
        TreeGenerationAlgorithm familyTreeBuilder;
        String PersonID;
        String username;

        try {
            Connection conn = db.getConnection();
            UserDAO uDao = new UserDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            familyTreeBuilder = new TreeGenerationAlgorithm();

            //Create a new user and insert him/her into the database
            User user = newUser(rr);
            Person person = newPerson(user);
            Event birth = newBirthEvent(user);

            uDao.insert(user);
            eDao.insert(birth);
            db.closeConnection(true);

            // Save variables for creating RegisterResultObject
            PersonID = person.getPersonID();
            username = user.getUsername();

            //Generate Ancestry using TreeGenerationAlgorithm object
            familyTreeBuilder.generateNamesDatabase();
            familyTreeBuilder.generateLocationDatabase();
            familyTreeBuilder.generateParents(person, user, birth.getYear(), GENERATIONS_REQUESTED);
            familyTreeBuilder.uploadToDatabase();

            //Create authToken for new user
            String tokenStr = createAuthToken(username);

            return new RegisterResult(tokenStr, username, PersonID, true);

        } catch (DataAccessException ex) {
            db.closeConnection(false);
            return new RegisterResult("Error: Request property missing or has invalid value", false);
        }
    }

    private String createAuthToken(String username) {
        AuthToken newToken = new AuthToken(username);
        newToken.generateAuthToken();

        db = new Database();
        Connection conn;
        try {
            conn = db.getConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(newToken);
            db.closeConnection(true);
            return newToken.getAuthtoken();

        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}