//Интерфес TaskManager для дальнейшей разработки
package manager;

import model.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager {

    Collection<Task> getTasksList();

    Collection<Epic> getEpicsList();

    Collection<Subtask> getSubtasksList();

    void deleteAllTasks() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    void deleteAllSubtasks() throws ManagerSaveException;

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void newTask(Task task) throws ManagerSaveException;

    void newEpic(Epic epic) throws ManagerSaveException;

    void newSubtask(Subtask subtask) throws ManagerSaveException;

    //2e) Методы для обновления задачи, подзадачи и эпика
    void updateTask(Task updatedTask) throws ManagerSaveException;

    void updateEpic(Epic updatedEpic) throws ManagerSaveException;

    void updateSubtask(Subtask updatedSubtask) throws ManagerSaveException;

    //2f) Методы для удаления задачи, подзадачи и эпика по id
    void deleteTaskById(int id) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;

    //3a) Метод для получения списка всех задач определенного эпика
    Collection<Subtask> getSubtasksInEpic(int epicId);

    List<Task> getHistory();
}
