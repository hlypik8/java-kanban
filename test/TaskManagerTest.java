import model.*;
import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager>{

    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected abstract T createManager();

    @BeforeEach
    void setUp(){
        manager = createManager();
        task = new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        epic = new Epic(200001, "Test epic", "Description");
        subtask = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);

        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtask(subtask);
    }

    // Общие тесты для всех реализаций TaskManager
    @Test
    void shouldAddNewTask() {
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void shouldAddNewEpic() {
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void shouldAddNewSubtask() {
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void shouldUpdateTask() {
        Task updatedTask = new Task(task.getId(), "Updated", "Updated", Status.DONE,
                LocalDateTime.now(), Duration.ZERO);
        manager.updateTask(updatedTask);
        assertEquals("Updated", manager.getTaskById(task.getId()).getName());
    }


}