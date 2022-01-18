package Server;

import Requests.LoadRequest;
import Results.LoadResult;
import Service.LoadService;
import HelperClasses.ReadWriteStringHelper;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * LoadHandler handles the /user/load path
 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 */

public class LoadHandler implements HttpHandler {
    LoadResult lResult;
    LoadService lService = new LoadService();
    LoadRequest lRequest;
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();

    public LoadRequest jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, LoadRequest.class);
    }
    public String javaToJson(LoadResult loadRes) {
        Gson gson = new Gson();
        return gson.toJson(loadRes);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /LOAD_HANDLER");
        boolean success;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                //JSON -> Request
                String reqDataJson = rw_helper.readString(reqBody);
                lRequest = jsonToJava(reqDataJson);
                //Call Service
                lResult = lService.load(lRequest);
                // Result -> JSON
                success = lResult.getSuccess();
                String jsonStr = javaToJson(lResult);

                if (!success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }

                OutputStream respBody = exchange.getResponseBody();
                rw_helper.writeString(jsonStr, respBody);
                respBody.close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAVAILABLE, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
