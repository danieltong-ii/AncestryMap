package Server;

import Requests.LoginRequest;
import Results.LoginResult;
import Service.LoginService;
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
 * LoginHandler handles the /user/login path
 */

public class LoginHandler implements HttpHandler {
    LoginRequest lRequest;
    LoginService lService = new LoginService();
    LoginResult lResult;
    ReadWriteStringHelper rw_helper = new ReadWriteStringHelper();

    public LoginRequest jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, LoginRequest.class);
    }
    public String javaToJson(LoginResult logRes) {
        Gson gson = new Gson();
        return gson.toJson(logRes);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /LOGIN_HANDLER");

        boolean success;

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                //JSON -> Request
                String reqDataJson = rw_helper.readString(reqBody);
                lRequest = jsonToJava(reqDataJson);
                //Call Service
                lResult = lService.login(lRequest);
                success = lResult.getSuccess();
                // Result -> JSON
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
