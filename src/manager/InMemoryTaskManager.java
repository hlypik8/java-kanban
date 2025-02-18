package manager;

import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextTaskId = 1;
    private int nextSubtaskId = 1;
    private int nextEpicId = 1;
    HistoryManager historyManager = Managers.getDefaultHistory();

    //2a) Методы для получения списка всех задач подзадач и эпиков
    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //2b) Методы для удаления всех задач, подзадач и эпиков

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear(); //Вместе со всеми эпиками удаляем все сабтаски
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) { //Удаляем все сабтаски из всех эпиков
            subtask.getEpic().removeSubtask(subtask);
        }
        subtasks.clear(); // Очищаем HashMap сабтасков
    }

    //2c) Методы получения задачи, подзадачи и эпика по id

    @Override
    public Task getTaskById(int id) {
        if (historyManager.getHistory().size() >= 10) {
            historyManager.getHistory().removeFirst();
        }
        Task currentTask = tasks.get(id);
        historyManager.getHistory().add(new Task(currentTask));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (historyManager.getHistory().size() >= 10) {
            historyManager.getHistory().removeFirst();
        }
        Epic currentEpic = epics.get(id);
        historyManager.getHistory().add(new Epic(currentEpic));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (historyManager.getHistory().size() >= 10) {
            historyManager.getHistory().removeFirst();
        }
        historyManager.getHistory().add(subtasks.get(id));
        return subtasks.get(id);
    }

    //2d) Методы для создания новых задач, подзадач и эпиков

    @Override
    public void newTask(Task task) {
        task.id = nextTaskId++;
        tasks.put(task.id, task);
    }


    @Override
    public void newEpic(Epic epic) {
        epic.id = nextEpicId++;
        epics.put(epic.id, epic);
    }

    @Override
    public void newSubtask(Subtask subtask) {
        //Так как сабтаск не может существовать вне эпика, то при создании нового сабтаска сразу же определяем
        //к какому эпику он принадлежит и заносим в список сабтасков эпика
        subtask.id = nextSubtaskId++;
        subtasks.put(subtask.id, subtask); //Заносим в список сабтасков
        Epic epic = epics.get(subtask.getEpic().id); // Получаем эпик
        epic.addSubtask(subtask); // Заносим сабтаск в список сабтасков эпика

    }

    @Override
    //2e) Методы для обновления задачи, подзадачи и эпика
    public void updateTask(Task updatedTask) {
        Task existingTask = tasks.get(updatedTask.id);
        if (existingTask != null) { //Проверяем существет ли задача
            existingTask.update(updatedTask);// Обновляем задачу с сохранением id
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        Epic existingEpic = epics.get(updatedEpic.id);
        if (existingEpic != null) {
            existingEpic.update(updatedEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        Subtask existingSubtask = subtasks.get(updatedSubtask.id);
        if (existingSubtask != null) {
            existingSubtask.update(updatedSubtask);
        }
    }

    @Override
    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id); //Удаляем элемент из HashMap epics
        if (epic != null) { //Удаляем из HashMap subtasks сабтаски, которые входили в эпик
            for (Subtask subtask : new ArrayList<>(epic.getSubtaskList())) {
                subtasks.remove(subtask.id);
            }
            epic.getSubtaskList().clear();
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);//даляем элемент из HashMap subtasks
        if (subtask != null) {
            Epic epic = subtask.getEpic(); //олучаем эпик в котором содержался удаленный сабтаск
            if (epic != null && epics.containsKey(epic.id)) {
                epic.removeSubtask(subtask); // Удаляем сабтаск из списка сабтасков эпика
            }
        }
    }

    //3a) Метод для получения списка всех задач определенного эпика
    @Override
    public ArrayList<Subtask> getSubtasksInEpic(int epicId) {
        return epics.get(epicId).getSubtaskList();
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
