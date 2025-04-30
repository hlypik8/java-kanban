package serverTest;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.handlers.GsonCreator;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = GsonCreator.getGson();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTasks_ReturnsEmptyList() throws Exception {

        HttpRequest request = HttpTestClient.get("/tasks");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void createTask_Returns201() throws Exception {
        Task task = new Task(100001, "Test", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        String json = gson.toJson(task);

        HttpRequest request = HttpTestClient.post("/tasks", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertFalse(taskManager.getTasksList().isEmpty());
    }

    @Test
    void getTaskById_Returns200() throws Exception {
        Task task = new Task(100001, "Test", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        taskManager.newTask(task);

        HttpRequest request = HttpTestClient.get("/tasks/" + task.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test"));
    }

    @Test
    void getTaskById_Returns404() throws Exception {

        HttpRequest request = HttpTestClient.get("/tasks/999");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteTask_Returns200() throws Exception {
        Task task = new Task(100001, "Test", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        taskManager.newTask(task);

        HttpRequest request = HttpTestClient.delete("/tasks/" + task.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getTasksList().isEmpty());
    }

    @Test
    void postTask_Returns400ForInvalidData() throws Exception {

        HttpRequest request = HttpTestClient.post("/tasks", "invalid json");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }
}