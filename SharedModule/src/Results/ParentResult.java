package Results;

/**
 * A super class for results classes to inherit from. Contains the message and success boolean
 */

public class ParentResult {
    /**
     * Message to return to the user
     */
    private String message;
    /**
     * Tells the user whether or not the operation requested was successful or not
     */
    private Boolean success;

    /**
     * Constructor for Parent Result
     * @param message a string that displays status
     * @param success a boolean that tells user is succeeded or not
     */
    public ParentResult(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public ParentResult(Boolean success) {
        this.success = success;
    }

    public ParentResult() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
