package services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;

import Service.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class ClearServiceTest {
    private Database db;
    private User user;
    private AuthToken token;
    private Event event;
    private Person person;
    private UserDAO uDao;
    private PersonDAO pDao;
    private EventDAO eDao;
    private AuthTokenDAO aDao;
    ClearService CR;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        user = new User("daniel", "password", "danieltong.h@gmail.com",
                "Daniel", "Tong", "m", "1");
        token = new AuthToken("890", "Bon");
        event = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        person = new Person("1", "Gale", "Gale",
                "Piper", "m", "4", "5",
                "3");

        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();

        uDao = new UserDAO(conn);
        aDao = new AuthTokenDAO(conn);
        eDao = new EventDAO(conn);
        pDao = new PersonDAO(conn);

        CR = new ClearService();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();

        uDao.insert(user);
        aDao.insert(token);
        eDao.insert(event);
        pDao.insert(person);
        //Then we pass that connection to the EventDAO so it can access the database
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void ClearPass() throws DataAccessException {
        User uuser = null;
        AuthToken ttoken = null;
        Event eevent = null;
        Person pperson = null;
        try {
            CR.Clear();
            assertEquals(uuser,uDao.find("Daniel"));
            assertEquals(ttoken,aDao.find("Bon"));
            assertEquals(eevent,eDao.find("Gale"));
            assertEquals(pperson,pDao.find("Gale"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    public void ClearFail() throws DataAccessException {
        try {
            CR.Clear();
            assertNull(uDao.find("Daniel"));
            assertNull(aDao.find("Bon"));
            assertNull(eDao.find("Gale"));
            assertNull(pDao.find("Gale"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
