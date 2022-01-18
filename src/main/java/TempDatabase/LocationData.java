package TempDatabase;
import Model.Location;

public class LocationData {
    /**
     * Array of locations that were given by the project's json file.
     */
    Location[] data;

    public LocationData(Location[] data) {
        this.data = data;
    }

    public LocationData() {
    }

    public Location getLocation() {
        int sizeOfLocList = data.length;
        int randomNum = getRandomNumber(0,sizeOfLocList);
        return data[randomNum];
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
