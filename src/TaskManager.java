import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextTaskId = 1;
    private int nextSubtaskId = 1;
    private int nextEpicId = 1;

    //2a) Методы для получения списка всех задач подзадач и эпиков

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //2b) Методы для удаления всех задач, подзадач и эпиков

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear(); //Вместе со всеми эпиками удаляем все сабтаски
    }

    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) { //Удаляем все сабтаски из всех эпиков
            subtask.getEpic().removeSubtask(subtask);
        }
        subtasks.clear(); // Очищаем HashMap сабтасков
    }

    //2c) Методы получения задачи, подзадачи и эпика по id

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    //2d) Методы для создания новых задач, подзадач и эпиков

    public void newTask(Task task) {
        task.id = nextTaskId++;
        tasks.put(task.id, task);
    }


    public void newEpic(Epic epic) {
        epic.id = nextEpicId++;
        epics.put(epic.id, epic);
    }

    public void newSubtask(Subtask subtask) {
        //Так как сабтаск не может существовать вне эпика, то при создании нового сабтаска сразу же определяем
        //к какому эпику он принадлежит и заносим в список сабтасков эпика
        subtask.id = nextSubtaskId++;
        subtasks.put(subtask.id, subtask); //Заносим в список сабтасков
        Epic epic = epics.get(subtask.getEpic().id); // Получаем эпик
        epic.addSubtask(subtask); // Заносим сабтаск в список сабтасков эпика

    }

    //2e) Методы для обновления задачи, подзадачи и эпика
    public void updateTask(Task updatedTask) {
        Task existingTask = tasks.get(updatedTask.id);
        if (existingTask != null) { //Проверяем существет ли задача
            existingTask.update(updatedTask);// Обновляем задачу с сохранением id
        }
    }

    public void updateEpic(Epic updatedEpic) {
        Epic existingEpic = epics.get(updatedEpic.id);
        if (existingEpic != null) {
            existingEpic.update(updatedEpic);
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        Subtask existingSubtask = subtasks.get(updatedSubtask.id);
        if (existingSubtask != null) {
            existingSubtask.update(updatedSubtask);
        }
    }

    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id); //Удаляем элемент из HashMap epics
        if(epic != null){ //Удаляем из HashMap subtasks сабтаски, которые входили в эпик
            for (Subtask subtask : new ArrayList<>(epic.getSubtaskList())) {
                subtasks.remove(subtask.id);
            }
            epic.getSubtaskList().clear();
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);//даляем элемент из HashMap subtasks
        if(subtask != null ){
            Epic epic = subtask.getEpic(); //олучаем эпик в котором содержался удаленный сабтаск
            if(epic != null && epics.containsKey(epic.id)){
                epic.removeSubtask(subtask); // Удаляем сабтаск из списка сабтасков эпика
            }
        }
    }

    //3a) Метод для получения списка всех задач определенного эпика
    public ArrayList<Subtask> getSubtasksInEpic(int epicId) {
        return epics.get(epicId).getSubtaskList();
    }
}
