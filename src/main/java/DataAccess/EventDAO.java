package DataAccess;

import Model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods for accessing event data.
 */

public class EventDAO {
    /**
     * A connection is the session between java application and database
     */
    private final Connection conn;

    /**
     * Public constructor for eventDAO
     * @param conn connection of test database
     */

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new event into the database
     * sql = the SQL statement we want to send to the DBMS
     * prepareStatement precompiles the SQL statement
     * @param event
     * @throws DataAccessException : an error
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * A method used to find events in the database given the event's unique eventID
     * @param eventID
     * @return The function returns the event found
     * @throws DataAccessException
     */

    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);

            rs = stmt.executeQuery();

            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     *  This function deletes ALL events in our database
     * @throws SQLException
     */

    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM Events";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting persons");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public List<Event> findEventsAssociatedWithUser(String username) throws DataAccessException {
        List<Event> eventList = new ArrayList<>();

        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            rs.next();
            while (!rs.isClosed()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventList.add(event);
                rs.next();
            }
            if (eventList.isEmpty()) {
                return null;
            }
            return eventList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteEvents(String assocUsername) throws SQLException {
        String sql = "DELETE FROM Events WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,assocUsername);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting those user's events");
        }
    }
}
