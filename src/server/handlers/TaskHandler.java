package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import manager.exceptions.IntersectionException;
import manager.exceptions.NotFoundException;
import model.Task;
import server.Endpoint;

import java.io.IOException;
import java.io.InputStream;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET -> getTasks(httpExchange);
            case GET_BY_ID -> getTask(httpExchange, getId(path));
            case POST -> postTask(httpExchange);
            case DELETE_BY_ID -> deleteTask(httpExchange, getId(path));
            default -> sendMethodNotAllowed(httpExchange);
        }

    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String paths[] = requestPath.split("/");
        if (paths.length <= 3 && paths[1].equals("tasks")) {
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

    private void getTasks(HttpExchange httpExchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getTasksList());
        sendText(httpExchange, jsonString, 200);
    }

    private void getTask(HttpExchange httpExchange, int id) throws IOException {
        try {
            Task jsonTask = taskManager.getTaskById(id);
            sendText(httpExchange, gson.toJson(jsonTask), 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    private void postTask(HttpExchange httpExchange) throws IOException {
        InputStream stream = httpExchange.getRequestBody();
        String taskString = new String(stream.readAllBytes());
        if (taskString.isEmpty() || taskString.isBlank()) {
            sendBadRequest(httpExchange);
            return;
        }
        Task task = gson.fromJson(taskString, Task.class);
        try {
            //Пытаемся обновить задачу (если она существует)
            taskManager.getTaskById(task.getId()); //Проверяем существование
            taskManager.updateTask(task); // Обновляем
            sendText(httpExchange, "Задача успешно обновлена", 201);
        } catch (NotFoundException e) {
            //Если задача не найдена, то добавляем как новую
            try {
                taskManager.newTask(task);
                sendText(httpExchange, "Задача успешно добавлена", 201);
            } catch (IntersectionException ex) {
                sendHasIntersections(httpExchange); //Ошибка пересечения при добавлении
            }
        } catch (IntersectionException e) {
            sendHasIntersections(httpExchange);//Ошибка пересечения при обновлении
        }
    }

    private void deleteTask(HttpExchange httpExchange, int id) throws IOException {
        try {
            taskManager.getTaskById(id); //Проверяем, что задача есть
            taskManager.deleteTaskById(id);
            sendText(httpExchange, "Задача успешно удалена", 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }
}
