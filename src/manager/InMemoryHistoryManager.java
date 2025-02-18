package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task){
        history.add(task);
    }

    //Реализация метода получения истории просмотра
    @Override
    public List<Task> getHistory(){
        return history;
    }
}
