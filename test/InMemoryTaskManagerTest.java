import static org.junit.jupiter.api.Assertions.*;

import manager.Managers;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    @Test
    public void shouldAddNewTask() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(1, "Test task", "Description", Status.NEW);
        tm.newTask(task);
        assertEquals(tm.getTaskById(1), task);
    }

    @Test
    public void shouldAddNewEpic() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(1, "Test epic", "Description");
        tm.newEpic(epic);
        assertEquals(tm.getEpicById(1), epic);
    }

    @Test
    public void shouldAddNewSubtask() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(1, "Test epic", "Description");
        tm.newEpic(epic);
        Subtask subtask = new Subtask(1, "Test subtask", "Description", Status.NEW, epic);
        tm.newSubtask(subtask);

        assertEquals(tm.getSubtaskById(1), subtask);
    }

    @Test
    public void idsShouldNotConflict() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(1, "Test", "Description", Status.NEW);
        tm.newTask(task);

        assertEquals(1, tm.getTaskById(1).id);
    }

    @Test
    public void tasksShouldHaveSameFields() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(1, "Test", "Description", Status.NEW);
        tm.newTask(task);

        assertEquals(new Task(1, "Test", "Description", Status.NEW), tm.getTaskById(1));
    }

    @Test
    public void historyManagerShouldKeepNotUpdatedTaskVersion() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task(1, "Test", "Description", Status.NEW);
        Task task1 = new Task(1, "Test1", "Description1", Status.IN_PROGRESS);
        tm.newTask(task);
        tm.getTaskById(1);
        tm.updateTask(task1);
        tm.getTaskById(1);
        //Здесть сравнивем статусы так как метод equals() класса Task сравнивает задачи только по id
        assertNotEquals(tm.getHistory().get(0).status, tm.getHistory().get(1).status);
    }

    @Test
    public void historyManagerShouldKeepNotUpdatedEpicsVersion() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(1, "Test epic", "Description");
        Epic epic1 = new Epic(1, "Test1 epic1", "Description1");
        tm.newEpic(epic);
        tm.getEpicById(1);
        tm.updateEpic(epic1);
        tm.getEpicById(1);

        assertNotEquals(tm.getHistory().get(0).status, tm.getHistory().get(1).status);
    }

    @Test
    public void historyManagerShouldKeepNotUpdatedSubtasksVersion() {
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic(1, "Test epic", "Description");
        Subtask subtask = new Subtask(1, "Test", "Description", Status.NEW, epic);
        Subtask subtask1 = new Subtask(1, "Test1", "Description1", Status.DONE, epic);
        epic.addSubtask(subtask);
        tm.newEpic(epic);
        tm.newSubtask(subtask);
        tm.getSubtaskById(1);
        tm.updateSubtask(subtask1);
        tm.getSubtaskById(1);

        assertNotEquals(tm.getHistory().get(0).status, tm.getHistory().get(1).status);
    }

}