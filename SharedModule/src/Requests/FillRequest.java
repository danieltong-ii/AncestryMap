package Requests;

/**
 * A request to populate the server's database with generated data for the specified user name.
 */

public class FillRequest {
    /**
     *  User specified user name
     */
    private String username;
    /**
     * User specified generations (optional)
     */
    private int generations;

    /**
     * Constructor used if user gives a specific generations number
     * @param username : username
     * @param generations : generations
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    /**
     * Constructor used if user does not give a specific num of generations
     * @param username
     */

    public FillRequest(String username) {
        int DEFAULT = 4;
        this.username = username;
        this.generations = DEFAULT;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
