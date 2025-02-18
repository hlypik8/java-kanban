import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {


    @Test
    public void shouldEqualsForSubtasksWithSameId() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic);

        assertEquals(subtask2, subtask1);
    }

    @Test
    public void shouldUpdateSubtaskStatus() {
        Epic epic = new Epic(1, "Test", "Test test");

        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, epic);

        subtask1.update(subtask2);

        assertEquals(new Subtask(1, "Test1", "Test1 test1", Status.DONE, epic), subtask1);
    }

    /* Здесть та же проблема, что и с проверкой добавления эпика как сабтаска
    @Test
    public void shouldNotTakeSubtaskAsEpic(){
        Epic epic = new Epic(1, "Test", "Test test");
        Subtask subtask1 = new Subtask(1, "Test1", "Test1 test1", Status.NEW, epic);
        Subtask subtask2 = new Subtask(1, "Test2", "Test2 test2", Status.DONE, subtask1);

    }*/
}