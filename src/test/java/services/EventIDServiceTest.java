package services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Results.EventIDResult;
import Service.EventIDService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class EventIDServiceTest {
    Database db = new Database();
    EventIDService EIService = new EventIDService();
    EventRequest eRequest;
    EventIDResult eResult;
    EventIDResult expectedResult;
    Event firstEvent;
    AuthToken aToken;
    EventDAO eDao;
    AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();

        Connection conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
        eDao = new EventDAO(conn);
        aToken = new AuthToken("12345", "Sam");
        firstEvent = new Event("Biking_123A", "Sam", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        eRequest = new EventRequest("Biking_123A", "12345");
        expectedResult = new EventIDResult("Sam","Biking_123A", "Gale123A", 35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016, true);
        
        db.clearTables();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void getEventPass() throws DataAccessException {
        eDao.insert(firstEvent);
        aDao.insert(aToken);
        eResult = EIService.getEvent(eRequest);
        assertEquals(expectedResult.getEventID(),eResult.getEventID());
        assertEquals(expectedResult.getEventType(),eResult.getEventType());
        assertEquals(expectedResult.getCity(),eResult.getCity());
        assertEquals(expectedResult.getLatitude(),eResult.getLatitude());
        assertEquals(expectedResult.getAssociatedUsername(),eResult.getAssociatedUsername());
        assertEquals(expectedResult.getPersonID(),eResult.getPersonID());
        assertEquals(expectedResult.getYear(),eResult.getYear());
    }

    @Test
    public void getEventFail() throws DataAccessException {
        eDao.insert(firstEvent);
        AuthToken wrongToken = new AuthToken("12333", "michael");
        aDao.insert(wrongToken);

        eResult = EIService.getEvent(eRequest);
        assertNotEquals(expectedResult.getEventID(),eResult.getEventID());
        assertNotEquals(expectedResult.getEventType(),eResult.getEventType());
        assertNotEquals(expectedResult.getCity(),eResult.getCity());
        assertNotEquals(expectedResult.getLatitude(),eResult.getLatitude());
        assertNotEquals(expectedResult.getAssociatedUsername(),eResult.getAssociatedUsername());
        assertNotEquals(expectedResult.getPersonID(),eResult.getPersonID());
        assertNotEquals(expectedResult.getYear(),eResult.getYear());
    }
}
