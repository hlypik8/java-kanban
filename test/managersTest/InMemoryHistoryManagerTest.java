package managersTest;

import manager.exceptions.IntersectionException;
import manager.Managers;
import manager.TaskManager;
import manager.exceptions.NotFoundException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryHistoryManagerTest {
    private TaskManager tm;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setup() throws IntersectionException {
        tm = Managers.getDefault();
        task = new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        epic = new Epic(200001, "Test epic", "Description");
        subtask = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);
        tm.newTask(task);
        tm.newEpic(epic);
        tm.newSubtask(subtask);
    }


    //Тестирование добавления задач в историю
    @Test
    void shouldAddTask() throws NotFoundException {
        tm.getTaskById(task.getId());

        assertEquals(List.of(task), tm.getHistory());
    }

    @Test
    void shouldAddDifferentTaskTypes() throws NotFoundException {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());

        assertEquals(List.of(task, epic, subtask), tm.getHistory());
    }

    @Test
    void shouldNotAddDuplicateTask() throws NotFoundException {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getTaskById(task.getId());

        assertEquals(List.of(epic, task), tm.getHistory());
    }

    //Тестирование удаления задач
    @Test
    void shouldRemoveTask() throws NotFoundException {
        tm.getTaskById(task.getId());
        tm.deleteTaskById(task.getId());

        assertTrue(tm.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveMiddleNode() throws NotFoundException {
        tm.getEpicById(epic.getId());
        tm.getTaskById(task.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteTaskById(task.getId());

        assertEquals(List.of(epic, subtask), tm.getHistory());
    }

    //Тестирование порядка истории
    @Test
    void shouldKeepOrder() throws NotFoundException {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());

        assertEquals(List.of(task, epic, subtask), tm.getHistory());
    }

    @Test
    void testEpicSubtaskIntegrity() throws NotFoundException {
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteSubtaskById(subtask.getId());

        assertFalse(tm.getHistory().contains(subtask));
    }

    @Test
    void shouldDeleteSubtask() throws NotFoundException {
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteEpicById(epic.getId());

        assertTrue(tm.getHistory().isEmpty());
    }
}
