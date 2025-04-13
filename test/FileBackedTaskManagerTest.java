import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("Test", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasksList().isEmpty());
        assertTrue(loadedManager.getEpicsList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task(100001, "Original", "Original", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        manager.newTask(task);

        Task updatedTask = new Task(100001, "Updated", "Updated", Status.DONE,
                LocalDateTime.now(), Duration.ZERO);
        manager.updateTask(updatedTask);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        Task loadedTask = loaded.getTaskById(task.getId());

        assertEquals("Updated", loadedTask.getName());
        assertEquals(Status.DONE, loadedTask.getStatus());
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task(100001, "Task to delete", "Delete", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        manager.newTask(task);
        manager.getTaskById(task.getId());//Здесь получем таск по id, чтобы он добавился в историю,
        //иначе выбрасывается NullPointerException потому что в методе deleteTaskById() задача удаляется также и из
        // истории, а если её нет, то вылетает исключение. Нужно добавить обработку этого случая
        manager.deleteTaskById(task.getId());

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(0, loaded.getTasksList().size());
    }

    @Test
    void shouldKeepRightFormat() throws IOException {
        Task task = new Task(100001, "Test", "Desc", Status.NEW,
                LocalDateTime.of(2025, 4,13,3,52), Duration.ZERO);
        Epic epic = new Epic(200001, "Test", "Desc");
        Subtask subtask = new Subtask(300001, "Test", "Desc", Status.NEW, epic,
                LocalDateTime.of(2025, 4,13,3,52), Duration.ZERO);
        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtask(subtask);

        List<String> lines = Files.readAllLines(tempFile.toPath(), StandardCharsets.UTF_8);

        assertEquals("id,type,name,status,description,startTime,duration,epic", lines.getFirst());
        assertEquals("100001,TASK,Test,NEW,Desc,2025-04-13T03:52,PT0S", lines.get(1));
        assertEquals("200001,EPIC,Test,NEW,Desc,2025-04-13T03:52,PT0S", lines.get(2));
        assertEquals("300001,SUBTASK,Test,NEW,Desc,2025-04-13T03:52,PT0S,200001", lines.get(3));
    }

}
