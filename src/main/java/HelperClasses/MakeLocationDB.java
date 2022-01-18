package HelperClasses;

import TempDatabase.LocationData;
import com.google.gson.Gson;

import java.io.*;

/**
 * The class enables the server to fill the database with multiple generations of person and event data
 * This class is called by both the RegisterService and the FillService
 */
public class MakeLocationDB {

    public LocationData generate() throws IOException {

        Gson gs = new Gson();
        Reader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new FileReader("json/locations.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader != null) {
            final BufferedReader bufferedReader = new BufferedReader(reader);
            String str = bufferedReader.readLine();
            if (str != null) {
                sb.append(str);
                while ((str = bufferedReader.readLine()) != null) {
                    sb.append('\n');
                    sb.append(str);
                }
            }
        }
        String locationJsonString = sb.toString();

        return gs.fromJson(locationJsonString, LocationData.class);
    }


}
