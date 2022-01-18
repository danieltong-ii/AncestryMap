package dao;

import Model.Event;
import DataAccess.EventDAO;
import DataAccess.Database;
import DataAccess.DataAccessException;

import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event firstEvent;
    private Event secondEvent;
    private Event sameUser;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        firstEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("GYM_BRO", "Chad", "chad123A",
                35.4f, 120.1f, "America", "Provo",
                "Gym", 2000);
        sameUser = new Event("Climbing_323", "Gale", "Gale123A",
                25.9f, 340.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(firstEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(firstEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(firstEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(firstEvent));
    }

    @Test
    public void findPass() throws DataAccessException {

        eDao.insert(firstEvent);
        eDao.insert(secondEvent);

        Event compareTest = eDao.find(secondEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(secondEvent, compareTest);
    }
    @Test
    public void findFail() throws DataAccessException {

        eDao.insert(firstEvent);
        eDao.insert(secondEvent);

        Event compareTest = eDao.find("SWIM_123");
        assertNull(compareTest);
    }

    @Test
    public void findEventsAssociatedWithUserPass() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(sameUser);
        List<Event> eventList = new ArrayList<>();

        eventList.add(firstEvent);
        eventList.add(sameUser);

        assertEquals(eventList,eDao.findEventsAssociatedWithUser("Gale"));
    }

    @Test
    public void findEventsAssociatedWithUserFail() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(sameUser);
        List<Event> eventList = new ArrayList<>();
        eventList.add(firstEvent);
        eventList.add(sameUser);

        assertNotEquals(eventList,eDao.findEventsAssociatedWithUser("Chad"));
    }


    @Test
    public void deleteEventsPass() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.insert(sameUser);

        try {
            eDao.deleteEvents("Chad");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertNotNull(eDao.findEventsAssociatedWithUser("Gale"));
    }

    @Test
    public void deleteEventsFail() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(sameUser);
        List<Event> eventList = new ArrayList<>();
        eventList.add(firstEvent);
        eventList.add(sameUser);

        assertNotEquals(eventList,eDao.findEventsAssociatedWithUser("Chad"));
    }

    @Test
    public void clearPass() throws DataAccessException, SQLException {
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.clear();
        Event compareTest = eDao.find(firstEvent.getEventID());
        Event secondTest = eDao.find(secondEvent.getEventID());
        assertNull(compareTest);
        assertNull(secondTest);
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
    }

}
