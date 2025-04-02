import model.*;
import manager.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {
        File file = null;
        try {
            file = File.createTempFile("Test", ".csv");
        } catch (IOException e) {
            System.out.print("Ошибка при создании файла: ");
            e.printStackTrace();
        }

        TaskManager tm = Managers.getDefaultFile(file);

        System.out.println("Создан пустой файл");
        //Осуществляем проверку работы программы
        //Заносим все задачи в файл
        tm.newTask(new Task(100001, "Купить молоко", "В пятерочке", Status.NEW));
        tm.newTask(new Task(100002, "Погулять с собакой", "В парке", Status.NEW));

        Epic move = new Epic(200001, "Переехать", "в новую квартиру");
        tm.newEpic(move);
        tm.newSubtask(new Subtask(300001, "Собрать вещи", "Из шкафа", Status.NEW, move));
        tm.newSubtask(new Subtask(300002, "Сложить вещи", "В машину", Status.NEW, move));
        tm.newSubtask(new Subtask(300003, "Купить стол", "Кухонный", Status.NEW, move));

        Epic cook = new Epic(200002, "Приготовить ужин", "Пасту");
        tm.newEpic(cook);

        Task updatedTask1 = new Task(100001, "Купить молоко", "В пятерочке", Status.DONE);
        tm.updateTask(updatedTask1);

        //Создаем еще один менеджер и получаем копии записанных задач
        FileBackedTaskManager tm1 = FileBackedTaskManager.loadFromFile(file);
        //Выводим скопированные задачи
        System.out.println("Задачи: " + tm1.getTasksList());
        System.out.println("Эпики: " + tm1.getEpicsList());
        System.out.println("Подзадачи: " + tm1.getSubtasksList());

    }
}
