package Model;

/**
 * Model class for locations. Used for creating the array of locations used to generate
 * new persons.
 */
public class Location {
    /**
     * Latitude of the city
     */
    Float latitude;
    /**
     * Longitude of the city
     */
    Float longitude;
    /**
     * Name of the city
     */
    String city;
    /**
     * Country of the city
     */
    String country;

    /**
     * Constructor for a location
     * @param latitude : latitude
     * @param longitude : longitude
     * @param city : city
     * @param country : country
     */
    public Location(Float latitude, Float longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
