import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void shouldReturnPrioritizedTasksInRightSequence() {
        assertEquals(manager.getPrioritizedTasks(), List.of(task, subtask));
    }

    @Test
    void shouldReactIfTasksOverlapping() {
        //Проверим 3 случая: когда новая задача кончается после того, как начинается уже существующая,
        //когда новая задача начинается после начала существующей и кончается до конца существующей,
        //когда новая задача начинается до конца существующей
        manager.deleteAllTasks(); //Удалим заранее подготовленные задачи
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        Task task = new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.of(2025, 4, 13, 21, 0), Duration.ofHours(2));
        manager.newTask(task);

        Task taskIncident1 = new Task(100002, "Test of incident 1",
                "This task ends before the existing one ends.", Status.NEW,
                LocalDateTime.of(2025, 4, 13, 20, 50), Duration.ofHours(1));

        Task taskIncident2 = new Task(100003, "Test of incident 2",
                "This task starts after the beginning of the existing one" +
                        " and ends before the end of the existing one",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 21, 10), Duration.ofHours(1));

        Task taskIncident3 = new Task(100004, "Test of incident 3",
                "This task starts before the end of the existing one",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 22, 50), Duration.ofHours(1));

        Task taskWhichDoesNotOverlaps = new Task(100005, "Test of task which does not overlaps",
                "Should be ok",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 23, 10), Duration.ofHours(1));

        assertTrue(manager.intersectionCheck(taskIncident1));
        assertTrue(manager.intersectionCheck(taskIncident2));
        assertTrue(manager.intersectionCheck(taskIncident3));
        assertFalse(manager.intersectionCheck(taskWhichDoesNotOverlaps));
    }

    @Test
    void shouldNotAddTasksWhichThrowsIntersectionException() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        Task normalTask = new Task(100001, "Test task", "Description", Status.NEW,
                LocalDateTime.of(2025, 4, 13, 21, 0), Duration.ofHours(2));

        Task taskIntersectionTypeOne = new Task(100002, "Test of incident 1",
                "This task ends before the existing one ends.", Status.NEW,
                LocalDateTime.of(2025, 4, 13, 20, 50), Duration.ofHours(1));

        Task taskIntersectionTypeTwo = new Task(100003, "Test of incident 2",
                "This task starts after the beginning of the existing one" +
                        " and ends before the end of the existing one",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 21, 10),
                Duration.ofHours(1));

        Task taskIntersectionTypeThree = new Task(100004, "Test of incident 3",
                "This task starts before the end of the existing one",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 22, 50),
                Duration.ofHours(1));

        Task taskWhichDoesNotIntersects = new Task(100005, "Test of task which does not overlaps",
                "Should be ok",
                Status.NEW, LocalDateTime.of(2025, 4, 13, 23, 10),
                Duration.ofHours(1));


        manager.newTask(normalTask);
        manager.newTask(taskIntersectionTypeOne);
        manager.newTask(taskIntersectionTypeTwo);
        manager.newTask(taskIntersectionTypeThree);

        assertEquals(1, manager.getTasksList().size());

        manager.newTask(taskWhichDoesNotIntersects);

        assertEquals(2, manager.getTasksList().size());

    }
}