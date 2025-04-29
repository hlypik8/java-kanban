import manager.FileBackedTaskManager;
import manager.exceptions.IntersectionException;
import manager.exceptions.NotFoundException;
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


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException, IntersectionException { //Сделал так, потому что компилятор жалуется на необработанное и необъявленное
        tempFile = File.createTempFile("Test", ".csv");//исключение
        super.setUp();
    }

    @Override
    protected FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldSaveAndLoadEmptyManager() throws IntersectionException {
        manager.save();
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasksList().isEmpty());
        assertTrue(loadedManager.getEpicsList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    void shouldUpdateTaskFromFile() throws IntersectionException, NotFoundException {
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
    void shouldDeleteTask() throws IntersectionException, NotFoundException {
        manager.getTaskById(task.getId());//Здесь получем таск по id, чтобы он добавился в историю,
        //иначе выбрасывается NullPointerException потому что в методе deleteTaskById() задача удаляется также и из
        // истории, а если её нет, то вылетает исключение. Нужно добавить обработку этого случая
        manager.deleteTaskById(task.getId());

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(0, loaded.getTasksList().size());
    }

    @Test
    void shouldKeepRightFormat() throws IOException {
        List<String> lines = Files.readAllLines(tempFile.toPath(), StandardCharsets.UTF_8);

        assertEquals("id,type,name,status,description,startTime,duration,epic", lines.getFirst());
        assertEquals("100001,TASK,Test task,NEW,Description,2025-04-13T03:52,PT0S", lines.get(1));
        assertEquals("200001,EPIC,Test epic,NEW,Description,2025-04-14T03:52,PT0S", lines.get(2));
        assertEquals("300001,SUBTASK,Test subtask,NEW,Description,2025-04-14T03:52,PT0S,200001", lines.get(3));
    }

}
