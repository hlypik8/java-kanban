package serverTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HttpTestClient {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + PORT;

    public static HttpRequest get(String path) throws Exception {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();
    }

    public static HttpRequest post(String path, String body) throws Exception {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
    }

    public static HttpRequest delete(String path) throws Exception {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
    }
}
