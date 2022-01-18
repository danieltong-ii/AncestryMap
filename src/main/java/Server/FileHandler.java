package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

public class FileHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("SERVER: /FILEHANDLER");
        String urlPath = exchange.getRequestURI().toString();

        if (urlPath.contains("main.css")) {
            String filename = "web/" + urlPath;

            exchange.sendResponseHeaders(HTTP_OK, 0);
            Path path = Paths.get(filename);
            Files.copy(path, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
        else if (urlPath.equals("/")) {
            String filename = "web/index.html";

            exchange.sendResponseHeaders(HTTP_OK, 0);
            Path path = Paths.get(filename);
            Files.copy(path, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
        else {
            String filename = "web/HTML/404.html";

            exchange.sendResponseHeaders(HTTP_NOT_FOUND, 0);
            Path path = Paths.get(filename);
            Files.copy(path, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
    }
}
