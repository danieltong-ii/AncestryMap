package Server;

import Requests.EventRequest;
import Results.EventIDResult;
import Results.EventResult;
import Service.EventIDService;
import Service.EventService;
import HelperClasses.ReadWriteStringHelper;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();


    public String javaToJson_Event(EventResult eResult) {
        Gson gson = new Gson();
        return gson.toJson(eResult);
    }

    public String javaToJson_EventID(EventIDResult eIDResult) {
        Gson gson = new Gson();
        return gson.toJson(eIDResult);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventRequest eRequest;
        EventService eService = new EventService();
        EventIDService eIDService = new EventIDService();
        EventResult eResult = null;
        EventIDResult eIDResult = null;

        System.out.println("SERVER: /EVENT_HANDLER");
        String authToken;
        boolean success;
        String whichTest;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    authToken = reqHeaders.getFirst("Authorization");

                    String URI = exchange.getRequestURI().toString();
                    eRequest = createEventRequest(URI,authToken);

                    String jsonStr;
                    if (eRequest.getEventID().equals("ALL")) {
                        whichTest = "ALL";
                        eResult = eService.getEvents(eRequest);
                        jsonStr = javaToJson_Event(eResult);
                    }
                    else {
                        whichTest = "ONE";
                        eIDResult = eIDService.getEvent(eRequest);
                        jsonStr = javaToJson_EventID(eIDResult);
                    }
                    if (whichTest.equals("ALL")) {
                        success = eResult.getSuccess();
                    }
                    else {
                        success = eIDResult.getSuccess();
                    }
                    if (!success) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    // Result -> JSON
                    OutputStream respBody = exchange.getResponseBody();
                    rw_helper.writeString(jsonStr, respBody);
                    respBody.close();
                }
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAVAILABLE, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    public EventRequest createEventRequest(String URI, String authToken) {
        String noPrefix = URI.substring(6);

        //Check if there is a "/" as in /Event
        if (noPrefix.indexOf('/') == 0) {
            String eventID = noPrefix.substring(1);
            return new EventRequest(eventID, authToken);
        }
        else {
            return new EventRequest("ALL",authToken);
        }
    }
}
