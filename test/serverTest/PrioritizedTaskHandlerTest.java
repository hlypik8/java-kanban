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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedTaskHandlerTest {
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

    // GET /prioritized (успешный сценарий)
    @Test
    void getPrioritized_ReturnsTasksInOrder() throws Exception {
        // Создаем задачи с разным временем начала
        Task task1 = new Task(100001, "Task 1", "Desc", Status.NEW,
                LocalDateTime.parse("2024-01-01T10:00"), Duration.ofHours(1));
        Task task2 = new Task(100002, "Task 2", "Desc", Status.NEW,
                LocalDateTime.parse("2024-01-01T09:00"), Duration.ofHours(1));

        taskManager.newTask(task1);
        taskManager.newTask(task2);

        HttpRequest request = HttpTestClient.get("/prioritized");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritized = gson.fromJson(response.body(), List.class);

        assertEquals(200, response.statusCode());
        assertEquals(2, prioritized.size());
        assertTrue(response.body().contains("Task 2")); // Должна быть первой
    }

    // GET /prioritized (пустой список)
    @Test
    void getPrioritized_ReturnsEmptyList() throws Exception {

        HttpRequest request = HttpTestClient.get("/prioritized");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    // GET /prioritized с неверным методом (POST)
    @Test
    void getPrioritized_Returns405ForInvalidMethod() throws Exception {

        HttpRequest request = HttpTestClient.post("/prioritized", "");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }

    // GET /prioritized/invalid (неверный путь)
    @Test
    void getPrioritized_Returns405ForInvalidPath() throws Exception {

        HttpRequest request = HttpTestClient.get("/prioritized/invalid");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }
}