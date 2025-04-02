import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryHistoryManagerTest {
    private TaskManager tm;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setup() {
        tm = Managers.getDefault();
        task = new Task(100001, "Test task", "Description", Status.NEW);
        epic = new Epic(200001, "Test epic", "Description");
        subtask = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic);
        tm.newTask(task);
        tm.newEpic(epic);
        tm.newSubtask(subtask);
    }


    //Тестировнаие добавления задач в историю
    @Test
    void shouldAddTask() {
        tm.getTaskById(task.getId());

        assertEquals(List.of(task), tm.getHistory());
    }

    @Test
    void shouldAddDifferentTaskTypes() {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());

        assertEquals(List.of(task, epic, subtask), tm.getHistory());
    }

    @Test
    void shouldNotAddDuplicateTask() {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getTaskById(task.getId());

        assertEquals(List.of(epic, task), tm.getHistory());
    }

    //Тестирование удаления задач
    @Test
    void shouldRemoveTask() {
        tm.getTaskById(task.getId());
        tm.deleteTaskById(task.getId());

        assertTrue(tm.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveMiddleNode() {
        tm.getEpicById(epic.getId());
        tm.getTaskById(task.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteTaskById(task.getId());

        assertEquals(List.of(epic, subtask), tm.getHistory());
    }

    //Тестирование порядка истории
    @Test
    void shouldKeepOrder() {
        tm.getTaskById(task.getId());
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());

        assertEquals(List.of(task, epic, subtask), tm.getHistory());
    }

    @Test
    void testEpicSubtaskIntegrity() {
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteSubtaskById(subtask.getId());

        assertFalse(tm.getHistory().contains(subtask));
    }

    @Test
    void shouldDeleteSubtask() {
        tm.getEpicById(epic.getId());
        tm.getSubtaskById(subtask.getId());
        tm.deleteEpicById(epic.getId());

        assertTrue(tm.getHistory().isEmpty());
    }
}
