package services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Results.EventIDResult;
import Service.EventIDService;
import Service.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class EventServiceTest {
    Database db = new Database();
    EventService EService = new EventService();
    EventRequest eRequest;
    List<Event> eResult = new ArrayList<>();
    EventIDResult expectedResult;
    Event firstEvent;
    Event secondEvent;
    Event thirdEvent;
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
        secondEvent = new Event("GYM_BRO", "Sam", "chad123A",
                35.4f, 120.1f, "America", "Provo",
                "Gym", 2000);
        thirdEvent = new Event("Climbing_323", "Gale", "Gale123A",
                25.9f, 340.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        eRequest = new EventRequest("12345");
        expectedResult = new EventIDResult("Sam","Biking_123A", "Gale123A", 35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016, true);

        db.clearTables();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void getEventsPass() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();

        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.insert(thirdEvent);
        aDao.insert(aToken);

        eventList.add(firstEvent);
        eventList.add(secondEvent);

        eResult = EService.getEvents(eRequest).getData();

        assertEquals(eventList,eResult);
    }

    @Test
    public void getEventsFail() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();

        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.insert(thirdEvent);

        AuthToken wrongToken = new AuthToken("12333", "michael");
        aDao.insert(wrongToken);

        eventList.add(firstEvent);
        eventList.add(secondEvent);

        eResult = EService.getEvents(eRequest).getData();

        assertNotEquals(eventList,eResult);
    }
}
