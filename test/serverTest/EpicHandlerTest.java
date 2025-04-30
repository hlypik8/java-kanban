package serverTest;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
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

class EpicHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = GsonCreator.getGson();
    private Epic testEpic;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();

        // Создаем тестовый эпик
        testEpic = new Epic(200001, "Test Epic", "Description");
        taskManager.newEpic(testEpic);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    // Тест GET /epics
    @Test
    void getEpics_ReturnsEpicsList() throws Exception {
        HttpRequest request = HttpTestClient.get("/epics");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Epic"));
    }

    // Тест GET /epics/{id}
    @Test
    void getEpicById_ReturnsEpic() throws Exception {

        HttpRequest request = HttpTestClient.get("/epics/" + testEpic.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Epic"));
    }

    @Test
    void getEpicById_Returns404ForInvalidId() throws Exception {

        HttpRequest request = HttpTestClient.get("/epics/999");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    // Тест GET /epics/{id}/subtasks
    @Test
    void getSubtasksInEpic_ReturnsSubtasks() throws Exception {
        // Добавляем подзадачу
        Subtask subtask = new Subtask(300001, "Subtask", "Desc", Status.NEW, testEpic
                , LocalDateTime.now(), Duration.ZERO);
        taskManager.newSubtask(subtask);

        HttpRequest request = HttpTestClient.get("/epics/" + testEpic.getId() + "/subtasks");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Subtask"));
    }

    // Тест POST /epics
    @Test
    void postEpic_CreatesNewEpic() throws Exception {
        Epic newEpic = new Epic(200002, "New Epic", "New Desc");
        String json = gson.toJson(newEpic);

        HttpRequest request = HttpTestClient.post("/epics", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(2, taskManager.getEpicsList().size());
    }

    @Test
    void postEpic_UpdatesExistingEpic() throws Exception {
        Epic newEpic = new Epic(200001, "New Epic", "New Desc");
        taskManager.updateEpic(newEpic);
        String json = gson.toJson(newEpic);

        HttpRequest request = HttpTestClient.post("/epics", json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("New Epic", taskManager.getEpicById(testEpic.getId()).getName());
    }

    @Test
    void postEpic_Returns400ForInvalidData() throws Exception {

        HttpRequest request = HttpTestClient.post("/epics", "{ invalid json }");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    // Тест DELETE /epics/{id}
    @Test
    void deleteEpic_DeletesEpic() throws Exception {

        HttpRequest request = HttpTestClient.delete("/epics/" + testEpic.getId());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getEpicsList().isEmpty());
    }

    @Test
    void deleteEpic_Returns400ForInvalidId() throws Exception {

        HttpRequest request = HttpTestClient.delete("/epics/999");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }
}
