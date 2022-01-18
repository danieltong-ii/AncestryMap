package DataAccess;

import Model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods for accessing person data.
 */

public class PersonDAO {
    /**
     * A connection is the session between java application and database
     */
    private final Connection conn;
    /**
     * Public constructor for personDAO
     * @param conn connection of test database
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * This method inserts a specific person in the database
     * @param person
     * @throws DataAccessException
     */

    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, "
                + "Gender, FatherID, MotherID, SpouseID) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * This method finds a specific person in the database
     * @param personID user specifies which personID as a string
     * @return
     * @throws DataAccessException
     */

    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding person");
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
     * Returns an ArrayList of Persons that are associated with the username in the argument.
     * @param username : user specified username, based on the authToken given
     * @return arraylist of Persons
     * @throws DataAccessException
     */
    public List<Person> findPersonsAssociatedWithUser(String username) throws DataAccessException {
        List<Person> personList = new ArrayList<>();

        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            rs.next();
            while (!rs.isClosed()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("spouseID"));
                personList.add(person);
                rs.next();
            }
            if (personList.isEmpty()) {
                return null;
            }
            return personList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding person");
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


    /**
     * This method removes ALL Persons associated with a user from the database
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM Persons";
            stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting persons");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void deletePerson(String assocUsername) throws SQLException {
        String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,assocUsername);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting that person");
        }
    }
}
