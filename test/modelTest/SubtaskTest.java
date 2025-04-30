package modelTest;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {


    @Test
    public void shouldEqualsForSubtasksWithSameId() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic,
                LocalDateTime.now(), Duration.ZERO);

        assertEquals(subtask2, subtask1);
    }

    @Test
    public void shouldUpdateSubtaskStatus() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic,
                LocalDateTime.now(), Duration.ZERO);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic,
                LocalDateTime.now(), Duration.ZERO);

        subtask1.update(subtask2);

        assertEquals(new Subtask(1, "Test1", "Test1 test1", Status.DONE, epic,
                LocalDateTime.now(), Duration.ZERO), subtask1);
    }
}