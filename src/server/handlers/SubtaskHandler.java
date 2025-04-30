package server.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import manager.exceptions.IntersectionException;
import manager.exceptions.NotFoundException;
import model.Subtask;
import server.Endpoint;

import java.io.IOException;
import java.io.InputStream;

public class SubtaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, httpExchange.getRequestMethod());
        switch (endpoint) {
            case Endpoint.GET -> getSubtasks(httpExchange);
            case Endpoint.GET_BY_ID -> getSubtaskById(httpExchange, getId(path));
            case Endpoint.POST -> postSubtask(httpExchange);
            case Endpoint.DELETE_BY_ID -> deleteSubtaskById(httpExchange, getId(path));
            default -> sendMethodNotAllowed(httpExchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] paths = requestPath.split("/");
        if (paths.length <= 3 && paths[1].equals("subtasks")) {
            switch (requestMethod) {
                case "GET":
                    if (paths.length == 2) {
                        return Endpoint.GET;
                    } else {
                        return Endpoint.GET_BY_ID;
                    }
                case "POST":
                    return Endpoint.POST;
                case "DELETE":
                    return Endpoint.DELETE_BY_ID;
                default:
                    return null;
            }
        }
        return null;
    }

    private void getSubtasks(HttpExchange httpExchange) throws IOException {
        String jsonSubtasks = gson.toJson(taskManager.getSubtasksList());
        sendText(httpExchange, jsonSubtasks, 200);
    }

    private void getSubtaskById(HttpExchange httpExchange, int id) throws IOException {
        try {
            String jsonSubtask = gson.toJson(taskManager.getSubtaskById(id));
            sendText(httpExchange, jsonSubtask, 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postSubtask(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        String subtaskStr = new String(stream.readAllBytes());

        if (subtaskStr.isEmpty() || subtaskStr.isBlank()) {
            sendBadRequest(httpExchange);
            return;
        }

        try {
            Subtask subtask = gson.fromJson(subtaskStr, Subtask.class);

            try {
                //Пытаемся обновить сабтаск (если он существует)
                taskManager.getSubtaskById(subtask.getId());
                taskManager.updateSubtask(subtask);
                sendText(httpExchange, "Сабтаск успешно обновлен", 201);
            } catch (NotFoundException e) {
                //Если сабтаск не найден, то добавляем как новый
                try {
                    taskManager.newSubtask(subtask);
                    sendText(httpExchange, "Сабтаск успешно добавлен", 201);
                } catch (IntersectionException ex) {
                    sendHasIntersections(httpExchange);//Ошибка пересечения при добавлении
                }
            } catch (IntersectionException e) {
                sendHasIntersections(httpExchange);//Ошибка пересечения при обновлении
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(httpExchange);
        }
    }

    private void deleteSubtaskById(HttpExchange httpExchange, int id) throws IOException {
        try {
            taskManager.getSubtaskById(id);
            taskManager.deleteSubtaskById(id);
            sendText(httpExchange, "Сабтаск успешно удален", 201);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }
}
