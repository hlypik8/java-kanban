package server;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import manager.exceptions.IntersectionException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));
    }

    public static void main(String[] args) throws IntersectionException, IOException {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(100001, "Test task1", "Test tas1 desc", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);

        Task task2 = new Task(100002, "Test task2", "Test tas2 desc", Status.IN_PROGRESS,
                LocalDateTime.now().plusHours(1), Duration.ZERO);

        taskManager.newTask(task1);
        taskManager.newTask(task2);

        Epic epic1 = new Epic(200001, "Test epic1", "Test epic1 desc");
        taskManager.newEpic(epic1);

        Subtask subtask1 = new Subtask(300001, "Test subtask1", "Test subtask1 desc",
                Status.NEW, epic1, LocalDateTime.now().plusHours(2), Duration.ZERO);
        Subtask subtask2 = new Subtask(300002, "Test subtask2", "Test subtask2 desc",
                Status.IN_PROGRESS, epic1, LocalDateTime.now().plusHours(3), Duration.ZERO);
        taskManager.newSubtask(subtask1);
        taskManager.newSubtask(subtask2);

        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }

    public void start() {
        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server stopped");
    }

}
