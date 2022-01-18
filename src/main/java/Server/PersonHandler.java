package Server;

import Requests.PersonRequest;
import Results.PersonIDResult;
import Results.PersonResult;
import Service.PersonIDService;
import Service.PersonService;
import HelperClasses.ReadWriteStringHelper;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();
    PersonRequest pRequest;
    PersonService pService = new PersonService();
    PersonIDService pIDService = new PersonIDService();
    PersonResult pResult;
    PersonIDResult pIDResult;

    public String javaToJson_Person(PersonResult pResult) {
        Gson gson = new Gson();
        return gson.toJson(pResult);
    }

    public String javaToJson_PersonID(PersonIDResult pIDResult) {
        Gson gson = new Gson();
        return gson.toJson(pIDResult);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /PERSON_HANDLER");
        String authToken;
        boolean success;
        String whichTest;
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    authToken = reqHeaders.getFirst("Authorization");

                        String URI = exchange.getRequestURI().toString();
                        pRequest = createPersonRequest(URI,authToken);

                        String jsonStr;
                        if (pRequest.getPersonID().equals("ALL")) {
                            whichTest = "ALL";
                            pResult = pService.getPersons(pRequest);
                            jsonStr = javaToJson_Person(pResult);
                        }
                        else {
                            whichTest = "ONE";
                            pIDResult = pIDService.getPerson(pRequest);
                            jsonStr = javaToJson_PersonID(pIDResult);
                        }

                        if (whichTest.equals("ALL")) {
                            success = pResult.getSuccess();
                        }
                        else {
                            success = pIDResult.getSuccess();
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

    public PersonRequest createPersonRequest(String URI, String authToken) {
        String noPrefix = URI.substring(7);

        if (noPrefix.indexOf('/') == 0) {
            String personID = noPrefix.substring(1);
            return new PersonRequest(personID, authToken);
        }
        else {
            return new PersonRequest("ALL",authToken);
        }
    }

}
