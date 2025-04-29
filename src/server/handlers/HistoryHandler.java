package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import server.Endpoint;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, httpExchange.getRequestMethod());
        if (endpoint != null) {
            getHistory(httpExchange);
        } else {
            sendMethodNotAllowed(httpExchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] paths = requestPath.split("/");
        if (paths.length <= 2 && paths[1].equals("history")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET;
            }
            return null;
        }
        return null;
    }

    private void getHistory(HttpExchange httpExchange) throws IOException {
        String jsonHistory = gson.toJson(taskManager.getHistory());
        sendText(httpExchange, jsonHistory, 200);
    }
}
