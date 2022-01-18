package Server;

import DataAccess.DataAccessException;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Service.RegisterService;
import HelperClasses.ReadWriteStringHelper;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Responsible for communicating with the client. A handler receives a request and unpacks it.
 * It also packages and sends to client.
 * RequestHandler deserializes into Java Object
 */
public class RegisterHandler implements HttpHandler {
    RegisterRequest rRequest;
    RegisterService rService = new RegisterService();
    RegisterResult rResult;
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();

    /**
     * Converts Json String from Request body into a RegisterRequest java object
     * @param jsonString : the request body
     * @return RegisterRequest Java Object
     */
    public RegisterRequest jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, RegisterRequest.class);
    }
    public String javaToJson(RegisterResult regRes) {
        Gson gson = new Gson();
        return gson.toJson(regRes);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /REGISTER_HANDLER");

        boolean success;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                //JSON -> Request
                String reqDataJson = rw_helper.readString(reqBody);
                rRequest = jsonToJava(reqDataJson);
                //Call Service
                rResult = rService.register(rRequest);
                success = rResult.getSuccess();
                // Result -> JSON
                String jsonStr = javaToJson(rResult);

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
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
