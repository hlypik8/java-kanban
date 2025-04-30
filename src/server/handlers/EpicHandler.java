package server.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import manager.exceptions.NotFoundException;
import model.Epic;
import server.Endpoint;

import java.io.IOException;
import java.io.InputStream;

public class EpicHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, httpExchange.getRequestMethod());
        switch (endpoint) {
            case Endpoint.GET -> getEpics(httpExchange);
            case Endpoint.GET_BY_ID -> getEpicById(httpExchange, getId(path));
            case Endpoint.GET_SUBTASKS_BY_EPIC_ID -> getSubtaskInEpic(httpExchange, getId(path));
            case Endpoint.POST -> postEpic(httpExchange);
            case Endpoint.DELETE_BY_ID -> deleteEpic(httpExchange, getId(path));
            default -> sendMethodNotAllowed(httpExchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] paths = requestPath.split("/");
        if (paths.length <= 4 && paths[1].equals("epics")) {
            switch (requestMethod) {
                case "GET":
                    if (paths.length == 2) {
                        return Endpoint.GET;
                    } else if (paths.length == 3) {
                        return Endpoint.GET_BY_ID;
                    } else {
                        return Endpoint.GET_SUBTASKS_BY_EPIC_ID;
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

    private void getEpics(HttpExchange httpExchange) throws IOException {
        String jsonEpics = gson.toJson(taskManager.getEpicsList());
        sendText(httpExchange, jsonEpics, 200);
    }

    private void getEpicById(HttpExchange httpExchange, int id) throws IOException {
        try {
            String jsonEpic = gson.toJson(taskManager.getEpicById(id));
            sendText(httpExchange, jsonEpic, 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    private void getSubtaskInEpic(HttpExchange httpExchange, int id) throws IOException {
        try {
            taskManager.getEpicById(id);//проверяем есть ли эпик
            sendText(httpExchange, gson.toJson(taskManager.getSubtasksInEpic(id)), 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postEpic(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        String epicStr = new String(stream.readAllBytes());
        if (epicStr.isEmpty() || epicStr.isBlank()) {
            sendBadRequest(httpExchange);
            return;
        }
        try {
            Epic epic = gson.fromJson(epicStr, Epic.class);
            try {
                taskManager.getEpicById(epic.getId());
                taskManager.updateEpic(epic);
                sendText(httpExchange, "Эпик успешно обновлен", 201);
            } catch (NotFoundException e) {
                taskManager.newEpic(epic);
                sendText(httpExchange, "Эпик успешно добавлен", 201);
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(httpExchange);
        }
    }

    private void deleteEpic(HttpExchange httpExchange, int id) throws IOException {
        try {
            taskManager.getEpicById(id);
            taskManager.deleteEpicById(id);
            sendText(httpExchange, "Эпик успешно удален", 201);
        } catch (NotFoundException e) {
            sendBadRequest(httpExchange);
        }
    }
}