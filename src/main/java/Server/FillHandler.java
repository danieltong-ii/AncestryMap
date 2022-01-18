package Server;

import Requests.FillRequest;
import Results.FillResult;
import Service.FillService;
import HelperClasses.ReadWriteStringHelper;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * FillHandler handles the /user/fill path
 * Populates the server's database with generated data for the specified user name. The required "username" parameter must be a user already registered with the server. If there is any data in the database already associated with the given user name, it is deleted. The optional “generations” parameter lets the caller specify the number of generations of ancestors to be generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 * Required arguments: username
 * Other arguments: generations
 */

public class FillHandler implements HttpHandler {
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();
    FillRequest fRequest;
    FillService fService = new FillService();
    FillResult fResult;

    public String javaToJson(FillResult regRes) {
        Gson gson = new Gson();
        return gson.toJson(regRes);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /FILL_HANDLER");

        boolean success;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                String URI = exchange.getRequestURI().toString();
                fRequest = createFillRequest(URI);
                fResult = fService.fill(fRequest);

                // Result -> JSON
                success = fResult.getSuccess();
                String jsonStr = javaToJson(fResult);

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

    public FillRequest createFillRequest(String URI) {
        String username;
        int generationsRequested;
        int DEFAULT_NUM_OF_GENS = 4;
        String noPrefix = URI.substring(6);

        if (noPrefix.indexOf('/') != -1) {
            username = noPrefix.substring(0, noPrefix.indexOf('/'));
            generationsRequested = Integer.parseInt(noPrefix.substring(noPrefix.length()-1));
        }
        else {
            username = noPrefix;
            generationsRequested = DEFAULT_NUM_OF_GENS;
        }
        return new FillRequest(username, generationsRequested);
    }
}
