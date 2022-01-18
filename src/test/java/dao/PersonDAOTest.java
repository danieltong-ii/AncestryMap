package dao;

import Model.Event;
import Model.Person;
import DataAccess.PersonDAO;
import DataAccess.Database;
import DataAccess.DataAccessException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    private Database db;
    private Person firstPerson;
    private Person secondPerson;
    private Person assocPerson;
    private PersonDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        firstPerson = new Person("1", "Gale", "Gale",
                "Piper", "m", "4", "5",
                "3");
        secondPerson = new Person("4", "Ben", "Ben",
                "Song", "m", "6", "7",
                "9");
        assocPerson = new Person("5", "Ben", "Daniel",
                "Long", "m", "8", "2",
                "45");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new PersonDAO(conn);
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
        eDao.insert(firstPerson);
        //So lets use a find method to get the event that we just put in back out
        Person compareTest = eDao.find(firstPerson.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(firstPerson);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(firstPerson));
    }

    @Test
    public void findPass() throws DataAccessException {

        String bestPersonID = "1";

        eDao.insert(firstPerson);
        Person compareTest = eDao.find(firstPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(firstPerson, compareTest);
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
    }

    @Test
    public void findFail() throws DataAccessException {
        String bestPersonID = "6";
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        assertNull(eDao.find(bestPersonID));
    }

    @Test
    public void clearPass() throws DataAccessException, SQLException {
        eDao.insert(firstPerson);
        eDao.insert(secondPerson);
        eDao.clear();

        Person compareTest = eDao.find(firstPerson.getPersonID());
        Person secondTest = eDao.find(secondPerson.getPersonID());
        assertNull(compareTest);
        assertNull(secondTest);
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
    }

    @Test
    public void deletePersonPass() throws DataAccessException {
        eDao.insert(secondPerson);
        eDao.insert(assocPerson);
        List<Event> eventList = new ArrayList<>();
        eventList = null;

        try {
            eDao.deletePerson("Ben");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertEquals(eventList, eDao.findPersonsAssociatedWithUser("Ben"));
    }

    @Test
    public void deletePersonFail() throws DataAccessException {
        eDao.insert(firstPerson);
        eDao.insert(secondPerson);
        eDao.insert(assocPerson);

        try {
            eDao.deletePerson("Ben");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertNull(eDao.findPersonsAssociatedWithUser("Ben"));
    }

    @Test
    public void findPersonsAssociatedWithUserPass() throws DataAccessException {
        eDao.insert(secondPerson);
        eDao.insert(assocPerson);
        List<Person> personList = new ArrayList<>();

        personList.add(secondPerson);
        personList.add(assocPerson);

        assertEquals(personList,eDao.findPersonsAssociatedWithUser("Ben"));
    }

    @Test
    public void findPersonsAssociatedWithUserFail() throws DataAccessException {
        eDao.insert(secondPerson);
        eDao.insert(assocPerson);
        List<Person> personList = new ArrayList<>();
        personList.add(secondPerson);
        personList.add(assocPerson);

        assertNotEquals(personList,eDao.findPersonsAssociatedWithUser("Gale"));
    }

}
