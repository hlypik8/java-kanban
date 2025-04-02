import static org.junit.jupiter.api.Assertions.*;

import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    @Test
    public void shouldAddNewTask() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(100001, "Test task", "Description", Status.NEW);
        tm.newTask(task);
        assertEquals(tm.getTaskById(100001), task);
    }

    @Test
    public void shouldAddNewEpic() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(200001, "Test epic", "Description");
        tm.newEpic(epic);
        assertEquals(tm.getEpicById(200001), epic);
    }

    @Test
    public void shouldAddNewSubtask() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(200001, "Test epic", "Description");
        tm.newEpic(epic);
        Subtask subtask = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic);
        tm.newSubtask(subtask);

        assertEquals(tm.getSubtaskById(300001), subtask);
    }

    @Test
    public void idsShouldNotConflict() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(100001, "Test", "Description", Status.NEW);
        tm.newTask(task);

        assertEquals(100001, tm.getTaskById(100001).getId());
    }

    @Test
    public void tasksShouldHaveSameFields() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(100001, "Test", "Description", Status.NEW);
        tm.newTask(task);

        assertEquals(new Task(100001, "Test", "Description", Status.NEW), tm.getTaskById(100001));
    }
}