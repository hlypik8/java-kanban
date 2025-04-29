package manager.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import manager.server.Endpoint;

import java.io.IOException;

public class PrioritizedTaskHandler extends BaseHttpHandler {

    TaskManager taskManager;

    public PrioritizedTaskHandler(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoints(path, httpExchange.getRequestMethod());
        if (endpoint != null) {
            getPrioritized(httpExchange);
        } else {
            sendMethodNotAllowed(httpExchange);
        }
    }

    private Endpoint getEndpoints(String requestPath, String requestMethod) {
        String[] paths = requestPath.split("/");
        if (paths.length <= 2 && paths[1].equals("prioritized")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET;
            }
            return null;
        }
        return null;
    }

    private void getPrioritized(HttpExchange httpExchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(httpExchange, jsonString, 200);
    }
}
