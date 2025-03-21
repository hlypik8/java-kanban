package manager;

import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    //Здесь я переработал систему генерации id у задач, так как при удалении из истории возникали большие проблемы с
    //определением типа задачи. То есть если в истории просмотра хранились, например Task с id = 1 и Epic с id = 1,
    //то при вызове метода removeTaskById(1) мог удалиться Epic (зависело от проядка нахождения в истории).Я очень
    // долго пытался найти решение этой проблемы через .getClass() и подобные штуки, но ни к чему не пришел.
    private int nextTaskId = 100001; //Если id начинается с 1, то это Task
    private int nextEpicId = 200001; //Если id начинается с 2, то это Epic
    private int nextSubtaskId = 300001; //Если id начинается с 3, то это Subtask

    InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

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
        Task currentTask = tasks.get(id);
        historyManager.add(new Task(currentTask));
        return currentTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic currentEpic = epics.get(id);
        historyManager.add(new Epic(currentEpic));
        return currentEpic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask currentSubtask = subtasks.get(id);
        historyManager.add(new Subtask(currentSubtask, currentSubtask.getEpic()));
        return currentSubtask;
    }

    //2d) Методы для создания новых задач, подзадач и эпиков

    @Override
    public void newTask(Task task) {
        int id = nextTaskId++;
        task = new Task(id, task.getName(), task.getDescription(), task.getStatus());
        tasks.put(id, task);
    }


    @Override
    public void newEpic(Epic epic) {
        int id = nextEpicId++;
        epic = new Epic(id, epic.getName(), epic.getDescription());
        epics.put(id, epic);
    }

    @Override
    public void newSubtask(Subtask subtask) {
        //Так как сабтаск не может существовать вне эпика, то при создании нового сабтаска сразу же определяем
        //к какому эпику он принадлежит и заносим в список сабтасков эпика
        int id = nextSubtaskId++;
        subtask = new Subtask(id, subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getEpic());
        subtasks.put(id, subtask); //Заносим в список сабтасков
        Epic epic = epics.get(subtask.getEpic().getId()); // Получаем эпик
        epic.addSubtask(subtask); // Заносим сабтаск в список сабтасков эпика

    }

    @Override
    //2e) Методы для обновления задачи, подзадачи и эпика
    public void updateTask(Task updatedTask) {
        Task existingTask = tasks.get(updatedTask.getId());
        if (existingTask != null) { //Проверяем существет ли задача
            existingTask.update(updatedTask);// Обновляем задачу с сохранением id
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        Epic existingEpic = epics.get(updatedEpic.getId());
        if (existingEpic != null) {
            existingEpic.update(updatedEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        Subtask existingSubtask = subtasks.get(updatedSubtask.getId());
        if (existingSubtask != null) {
            existingSubtask.update(updatedSubtask);
        }
    }

    @Override
    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id); //Удаляем элемент из HashMap epics
        if (epic != null) { //Удаляем из HashMap subtasks сабтаски, которые входили в эпик
            for (Subtask subtask : new ArrayList<>(epic.getSubtaskList())) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epic.getSubtaskList().clear();
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);//даляем элемент из HashMap subtasks
        if (subtask != null) {
            Epic epic = subtask.getEpic(); //Получаем эпик в котором содержался удаленный сабтаск
            if (epic != null && epics.containsKey(epic.getId())) {
                epic.removeSubtask(subtask); // Удаляем сабтаск из списка сабтасков эпика
            }
            historyManager.remove(id);
        }
    }

    //3a) Метод для получения списка всех задач определенного эпика
    @Override
    public Collection<Subtask> getSubtasksInEpic(int epicId) {
        return epics.get(epicId).getSubtaskList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
