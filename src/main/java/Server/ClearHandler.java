package Server;

import HelperClasses.ReadWriteStringHelper;
import Results.ClearResult;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * Handles Clear POST requests.
 * Description: Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */

public class ClearHandler implements HttpHandler {
    ClearService clearService = new ClearService();
    ClearResult cr = null;
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /CLEAR_HANDLER");
        boolean success;

        try {
            cr = clearService.Clear();
            String jsonStr = javaToJson(cr);
            success = cr.getSuccess();

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }

            OutputStream respBody = exchange.getResponseBody();
            rw_helper.writeString(jsonStr, respBody);
            respBody.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            cr = new ClearResult("Error: Clear failed", false);
        }
    }

    public String javaToJson(ClearResult clearRes) {
        Gson gson = new Gson();
        return gson.toJson(clearRes);
    }
}
