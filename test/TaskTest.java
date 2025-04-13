import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldEqualsForTasksWithSameId() {
        Task task1 = new Task(1, "Test1", "Test1 test1", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        Task task2 = new Task(1, "Test2", "Test2 test2", Status.DONE,
                LocalDateTime.now(), Duration.ZERO);

        assertEquals(task2, task1);
    }

    @Test
    public void shouldUpdateTaskStatus() {
        Task task1 = new Task(1, "Test1", "Test1 test1", Status.NEW,
                LocalDateTime.now(), Duration.ZERO);
        Task task2 = new Task(2, "Test2", "Test2 test2", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ZERO);

        task1.update(task2);

        assertEquals(new Task(1, "Test2", "Test2 test2", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ZERO), task1);
    }

}