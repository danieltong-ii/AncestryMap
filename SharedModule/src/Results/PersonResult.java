package Results;

import Model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the result object for the /person request
 */
public class PersonResult extends ParentResult {

    /**
     * The Person Array that we will return in our JSon eventually
     */
    List<Person> data = new ArrayList<>();

    /**
     * Constructor for PersonResult class
     *
     * @param message : success or error message
     * @param success : success boolean
     */
    public PersonResult(String message, Boolean success, List<Person> data) {
        super(message, success);
        this.data = data;
    }

    public PersonResult(List<Person>data, Boolean success) {
        super(success);
        this.data = data;
    }

    public PersonResult(){

    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}

