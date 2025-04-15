//Интерфес TaskManager для дальнейшей разработки
package manager;

import model.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager {

    Collection<Task> getTasksList();

    Collection<Epic> getEpicsList();

    Collection<Subtask> getSubtasksList();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void newTask(Task task) throws IntersectionException;

    void newEpic(Epic epic);

    void newSubtask(Subtask subtask) throws IntersectionException;

    //2e) Методы для обновления задачи, подзадачи и эпика
    void updateTask(Task updatedTask) throws IntersectionException;

    void updateEpic(Epic updatedEpic);

    void updateSubtask(Subtask updatedSubtask) throws IntersectionException;

    //2f) Методы для удаления задачи, подзадачи и эпика по id
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    //3a) Метод для получения списка всех задач определенного эпика
    Collection<Subtask> getSubtasksInEpic(int epicId);

    List<Task> getHistory();
}
