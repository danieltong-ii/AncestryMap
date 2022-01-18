package Requests;

import Model.Event;
import Model.Person;
import Model.User;
import java.util.List;

/**
 *
 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 */
public class LoadRequest {
    /**
     * The Array of Users to load the database with
     */
    List<User> users;
    /**
     * The Array of Persons to load the database with
     */
    List<Person> persons;
    /**
     * The Array of Events to load the database with
     */
    List<Event> events;

    /**
     * Constructor for LoadRequest
     * @param users : array of users
     * @param persons: array of persons
     * @param events: array of events
     */

    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
