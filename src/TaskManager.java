import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int taskId = 0;
    private int epicId = 0;
    private int subtaskId = 0;

    public ArrayList<Task> getTasksList() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int key : tasks.keySet()) {
            taskList.add(tasks.get(key));
        }
        return taskList;
    }

    public ArrayList<Epic> getEpicsList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int key : epics.keySet()) {
            epicList.add(epics.get(key));
        }
        return epicList;
    }

    public ArrayList<Subtask> getSubtasksList() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (int key : subtasks.keySet()) {
            subtasksList.add(subtasks.get(key));
        }
        return subtasksList;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

}
