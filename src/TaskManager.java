import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static int taskId = 1;
    private static int epicId = 1;
    private static int subtaskId = 1;


    //2a) Методы для получения списка всех задач подзадач и эпиков

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

    //2b) Методы для удаления всех задач, подзадач и эпиков

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        //Здесть возможно нужно добавить удаление всех подзадач из всех эпиков
    }

    //2c) Методы получения задачи, подзадачи и эпика по идентификатору

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    //2d) Методы для создания новых задач, подзадач и эпиков

    public void newTask(Task task) {
        tasks.put(taskId, task);
        taskId++; //Добавляя новую задачу, увеличиваем счетчик на 1, чтобы следующая задача имела другой id
    }

    public void newSubtask(Subtask subtask, int epicId) {
        subtasks.put(subtaskId, subtask);
        //Так как сабтаск не может существовать вне эпика, при создании нового сабтаска, сразу же определяем
        //к какому эпику он принадлежит и заносим в список сабтасков эпика
        epics.get(epicId).addSubtask(subtask);
        subtaskId++;
    }

    public void newEpic(Epic epic) {
        epics.put(epicId, epic);
        epicId++;
    }

    //2e) Методы для обновления задачи, подзадачи и эпика
    public void updateTask(Task task) {

    }

    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    //3a) Метод для получения списка всех задач определенного эпика
    public ArrayList<Subtask> getSubtasksInEpic(int epicId) {
        return epics.get(epicId).getSubtaskList();
    }
}
