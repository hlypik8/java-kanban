package manager;

import manager.exceptions.*;
import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextTaskId = 100001; //Если id начинается с 1, то это Task
    private int nextEpicId = 200001; //Если id начинается с 2, то это Epic
    private int nextSubtaskId = 300001; //Если id начинается с 3, то это Subtask

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
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        prioritizedTasks.removeAll(subtasks.values());
        epics.clear();
        subtasks.clear(); //Вместе со всеми эпиками удаляем все сабтаски
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            prioritizedTasks.remove(subtask);
            subtask.getEpic().removeSubtask(subtask);
        });
        subtasks.clear();
    }

    //2c) Методы получения задачи, подзадачи и эпика по id

    @Override
    public Task getTaskById(int id) throws NotFoundException {
        Task currentTask = tasks.get(id);
        if (currentTask == null) {
            throw new NotFoundException("Задача не найдена!");
        }
        historyManager.add(new Task(currentTask));
        return currentTask;
    }

    @Override
    public Epic getEpicById(int id) throws NotFoundException {
        Epic currentEpic = epics.get(id);
        if (currentEpic == null) {
            throw new NotFoundException("Эпик не найден!");
        }
        historyManager.add(new Epic(currentEpic));
        return currentEpic;
    }

    @Override
    public Subtask getSubtaskById(int id) throws NotFoundException {
        Subtask currentSubtask = subtasks.get(id);
        if (currentSubtask == null) {
            throw new NotFoundException("Сабтаск не найден!");
        }
        historyManager.add(new Subtask(currentSubtask, currentSubtask.getEpic()));
        return currentSubtask;
    }

    //2d) Методы для создания новых задач, подзадач и эпиков

    @Override
    public void newTask(Task task) throws IntersectionException {
        if (intersectionCheck(task)) {
            throw new IntersectionException("Задача пересекается по времени с существующей");
        }
        int id = nextTaskId++;
        task = new Task(id, task.getName(), task.getDescription(), task.getStatus(),
                task.getStartTime(), task.getDuration());
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }


    @Override
    public void newEpic(Epic epic) {
        int id = nextEpicId++;
        epic = new Epic(id, epic.getName(), epic.getDescription());
        epics.put(id, epic);
    }

    @Override
    public void newSubtask(Subtask subtask) throws IntersectionException {
        if (intersectionCheck(subtask)) {
            throw new IntersectionException("Подзадача пересекается по времени с существующей");
        }
        int id = nextSubtaskId++;
        subtask = new Subtask(id, subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getEpic(),
                subtask.getStartTime(), subtask.getDuration());
        subtasks.put(id, subtask); //Заносим в список сабтасков
        Epic epic = epics.get(subtask.getEpic().getId()); // Получаем эпик
        epic.addSubtask(subtask); // Заносим сабтаск в список сабтасков эпика
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);//Добавляем в множество по приоритету
        }
    }

    @Override
    //2e) Методы для обновления задачи, подзадачи и эпика
    public void updateTask(Task updatedTask) throws IntersectionException {
        Task existingTask = tasks.get(updatedTask.getId());//здесь удаляем и снова добавляем обновленную задачу
        prioritizedTasks.remove(tasks.get(updatedTask.getId()));//т.к. старая версия задачи оставалась бы в множестве
        if (existingTask != null) {
            if (intersectionCheck(updatedTask)) {
                throw new IntersectionException("Обновленная версия задачи пресекается по времени с существующей!");
            }
            existingTask.update(updatedTask);// Обновляем задачу с сохранением id
            if (existingTask.getStartTime() != null) {
                prioritizedTasks.add(existingTask);
            }
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
    public void updateSubtask(Subtask updatedSubtask) throws IntersectionException {
        Subtask existingSubtask = subtasks.get(updatedSubtask.getId());
        prioritizedTasks.remove(subtasks.get(updatedSubtask.getId()));
        if (existingSubtask != null) {
            if (intersectionCheck(updatedSubtask)) {
                throw new IntersectionException("Обновленная подзадача пересекается по времени с существующей!");
            }
            existingSubtask.update(updatedSubtask);
            if (existingSubtask.getStartTime() != null) {
                prioritizedTasks.add(existingSubtask);
            }
        }
    }

    @Override
    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id)); //Сначала удаляем из списка по приоритету
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id); //Удаляем элемент из HashMap epics
        if (epic != null) { //Удаляем из HashMap subtasks сабтаски, которые входили в эпик
            new ArrayList<>(epic.getSubtaskList()).forEach(subtask -> {
                prioritizedTasks.remove(subtask);
                subtasks.remove(subtask.getId());
            });
            epic.getSubtaskList().clear();
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        prioritizedTasks.remove(subtasks.get(id));
        Subtask subtask = subtasks.remove(id);//Удаляем элемент из HashMap subtasks
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

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime startOfTask1 = task1.getStartTime();
        LocalDateTime endOfTask1 = task1.getEndTime();
        LocalDateTime startOfTask2 = task2.getStartTime();
        LocalDateTime endOfTask2 = task2.getEndTime();
        return startOfTask1.isBefore(endOfTask2) && endOfTask1.isAfter(startOfTask2);
    }

    public boolean intersectionCheck(Task newTask) {
        if (newTask.getStartTime() == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .anyMatch(existingTask -> isOverlapping(newTask, existingTask));
    }
}
