package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected Gson gson = GsonCreator.getGson();

    protected void sendText(HttpExchange httpExchange, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(code, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.getResponseBody().close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Не найдено", 404);
    }

    protected void sendHasIntersections(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Задачи пересекаются по времени", 406);
    }

    protected void sendMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Метод не найден", 405);
    }

    protected void sendBadRequest(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Некорректные данные", 400);
    }

    protected int getId(String path) throws IOException {
        String[] arrayPath = path.split("/");
        return Integer.parseInt(arrayPath[2]);
    }
}
