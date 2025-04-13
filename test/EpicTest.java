import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void shouldEqualsForEpicsWithSameId() {
        Epic epic1 = new Epic(200001, "Test1", "Test1 test1");
        Epic epic2 = new Epic(200001, "Test2", "Test2 test2");

        assertEquals(epic2, epic1);
    }

    @Test
    public void shouldUpdateEpicStatus() {
        Epic epic = new Epic(200001, "Test", "Test test");

        Subtask subtask1 = new Subtask(300001, "Test1", "Test1 test1", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);
        epic.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(300002, "Test2", "Test2 test2", Status.DONE, epic,
                LocalDateTime.now(), Duration.ZERO);
        epic.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldRemoveSubtask() {
        Epic epic = new Epic(200001, "Test", "Test test");

        Subtask subtask1 = new Subtask(300001, "Test1", "Test1 test1", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);
        epic.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(300002, "Test2", "Test2 test2", Status.DONE, epic,
                LocalDateTime.now(), Duration.ZERO);
        epic.addSubtask(subtask2);
        epic.removeSubtask(subtask2);

        assertEquals(1, epic.getSubtaskList().size());
    }

    @Test
    public void shouldSetRightStartEndTimeAndDuration() {
        Epic epic = new Epic(200001, "Test epic", "Description");
        Subtask subtask1 = new Subtask(300001, "Test subtask", "Description", Status.NEW, epic,
                LocalDateTime.of(2025, 4, 13, 3, 52), Duration.ZERO);
        Subtask subtask2 = new Subtask(300002, "Test subtask", "Description", Status.NEW, epic,
                LocalDateTime.of(2025, 4, 14, 3, 52), Duration.ofDays(2));
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(epic.getStartTime(), subtask1.getStartTime());
        assertEquals(epic.getEndTime(), subtask2.getEndTime());
        assertEquals(epic.getDuration(), subtask1.getDuration().plus(subtask2.getDuration()));
    }
}