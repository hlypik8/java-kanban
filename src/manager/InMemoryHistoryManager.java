//Класс InMemoryHistoryManager для управления историей просмотре задач
package manager;

import model.Task;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new LinkedList<>();



    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.add(task);
    }

    //Реализация метода получения истории просмотра
    @Override
    public List<Task> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
