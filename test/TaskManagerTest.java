import model.*;
import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected abstract T createManager();

    @BeforeEach
    void setUp() {
        manager = createManager();
        task = new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.of(2025, 4, 13, 3, 52), Duration.ZERO);
        epic = new Epic(200001, "Test epic", "Description");
        subtask = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic,
                LocalDateTime.of(2025, 4, 14, 3, 52), Duration.ZERO);

        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtask(subtask);
    }

    // Общие тесты для всех реализаций TaskManager

    @Test
    void shouldReturnTasksList() {
        assertEquals(List.of(task), manager.getTasksList());
    }

    @Test
    void shouldReturnEpicsList() {
        assertEquals(List.of(epic), manager.getEpicsList());
    }

    @Test
    void shouldReturnSubtasksList() {
        assertEquals(List.of(subtask), manager.getSubtasksList());
    }

    @Test
    void shouldDeleteAllTasks() {
        manager.deleteAllTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void shouldDeleteAllEpics() {
        manager.deleteAllEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void shouldDeleteAllSubtasks() {
        manager.deleteAllSubtasks();
        assertTrue(manager.getSubtasksList().isEmpty());
    }

    @Test
    void shouldReturnTask() {
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void shouldReturnEpic() {
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void shouldReturnSubtask() {
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

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

    @Test
    void shouldUpdateEpic() {
        Epic updatedEpic = new Epic(epic.getId(), "Updated", "Updated");
        manager.updateEpic(updatedEpic);
        assertEquals("Updated", manager.getEpicById(epic.getId()).getName());
    }

    @Test
    void shouldUpdateSubtask() {
        Subtask updatedSubtask = new Subtask(subtask.getId(), "Updated", "Updated", Status.IN_PROGRESS, epic,
                LocalDateTime.now(), Duration.ZERO);
        manager.updateSubtask(updatedSubtask);
        assertEquals("Updated", manager.getSubtaskById(subtask.getId()).getName());
    }

    @Test
    void shouldReturnSubtasksListInEpic() {
        assertEquals(List.of(subtask), manager.getSubtasksInEpic(epic.getId()));
    }

    @Test
    public void idsShouldNotConflict() {
        assertEquals(100001, manager.getTaskById(task.getId()).getId());
    }

    @Test
    public void tasksShouldHaveSameFields() {
        assertEquals(new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO), manager.getTaskById(100001));
    }
}