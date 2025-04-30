package serverTest;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.exceptions.IntersectionException;
import model.Epic;
import model.Status;
import model.Subtask;
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

class SubtaskHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = GsonCreator.getGson();
    private Epic testEpic;
    private Subtask testSubtask;

    @BeforeEach
    void setUp() throws IOException, IntersectionException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();

        // Создаем тестовый эпик и подзадачу
        testEpic = new Epic(200001, "Test Epic", "Description");
        taskManager.newEpic(testEpic);

        testSubtask = new Subtask(300001, "Test Subtask", "Desc", Status.NEW, testEpic,
                LocalDateTime.now(), Duration.ofHours(1));
        taskManager.newSubtask(testSubtask);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    // GET /subtasks
    @Test
    void getSubtasks_ReturnsSubtasksList() throws Exception {

        HttpRequest request = HttpTestClient.get("/subtasks");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Subtask"));
    }

    // GET /subtasks/{id}
    @Test
    void getSubtaskById_ReturnsSubtask() throws Exception {

        HttpRequest request = HttpTestClient.get("/subtasks/" + testSubtask.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Subtask"));
    }

    @Test
    void getSubtaskById_Returns404ForInvalidId() throws Exception {

        HttpRequest request = HttpTestClient.get("/subtasks/999");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // POST /subtasks
    @Test
    void postSubtask_CreatesNewSubtask() throws Exception {
        Subtask newSubtask = new Subtask(300002, "New Subtask", "New Desc", Status.NEW, testEpic,
                LocalDateTime.now().plusHours(1), Duration.ZERO);
        String json = gson.toJson(newSubtask);

        HttpRequest request = HttpTestClient.post("/subtasks", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(2, taskManager.getSubtasksList().size());
    }

    @Test
    void postSubtask_UpdatesExistingSubtask() throws Exception {
        Subtask newSubtask = new Subtask(300001, "Updated Subtask", "Updated Desc",
                Status.NEW, testEpic, LocalDateTime.now(), Duration.ZERO);
        String json = gson.toJson(newSubtask);

        HttpRequest request = HttpTestClient.post("/subtasks", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Updated Subtask", taskManager.getSubtaskById(testSubtask.getId()).getName());
    }

    @Test
    void postSubtask_Returns406ForIntersection() throws Exception {
        // Создаем подзадачу с пересекающимся временем
        Subtask overlappingSubtask = new Subtask(300002, "Overlap", "Desc", Status.NEW, testEpic,
                LocalDateTime.now().plusMinutes(30), Duration.ofHours(1));
        String json = gson.toJson(overlappingSubtask);

        HttpRequest request = HttpTestClient.post("/subtasks", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    void postSubtask_Returns400ForInvalidData() throws Exception {

        HttpRequest request = HttpTestClient.post("/subtasks", "invalid Json");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    // DELETE /subtasks/{id}
    @Test
    void deleteSubtask_DeletesSubtask() throws Exception {

        HttpRequest request = HttpTestClient.delete("/subtasks/" + testSubtask.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getSubtasksList().isEmpty());
    }

    @Test
    void deleteSubtask_Returns404ForInvalidId() throws Exception {

        HttpRequest request = HttpTestClient.delete("/subtasks/999");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}