import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void shouldEqualsForEpicsWithSameId() {
        Epic epic1 = new Epic(1, "Test1", "Test1 test1");
        Epic epic2 = new Epic(1, "Test2", "Test2 test2");

        assertEquals(epic2, epic1);
    }

    @Test
    public void shouldUpdateEpicStatus() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic);
        epic.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic);
        epic.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldRemoveSubtask() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic);
        epic.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic);
        epic.addSubtask(subtask2);
        epic.removeSubtask(subtask2);

        assertEquals(1, epic.getSubtaskList().size());
    }
}