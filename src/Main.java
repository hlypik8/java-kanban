import manager.TaskManager;
import model.*;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        //Осуществляем проверку работы программы

        tm.newTask(new Task(1, "Купить молоко", "В пятерочке", Status.NEW));
        tm.newTask(new Task(2, "Погулять с собакой", "В парке", Status.NEW));

        Epic move = new Epic(1, "Переехать", "в новую квартиру");
        tm.newEpic(move);
        tm.newSubtask(new Subtask(1, "Собрать вещи", "Из шкафа", Status.NEW, move));
        tm.newSubtask(new Subtask(2, "Сложить вещи", "В машину", Status.NEW, move));

        Epic cook = new Epic(2, "Приготовить ужин", "Пасту");
        tm.newEpic(cook);
        tm.newSubtask(new Subtask(3, "Сварить макароны", "Бабочки", Status.NEW, cook));

        System.out.println("\nПроверка методов добавления задач:\n");
        System.out.println(tm.getTasksList());
        System.out.println();
        System.out.println(tm.getEpicsList());
        System.out.println();
        System.out.println(tm.getSubtasksList());

        tm.updateTask(new Task(1, "Купить молоко", "В пятерочке", Status.DONE));
        tm.updateTask(new Task(2, "Погулять с собакой", "В парке", Status.IN_PROGRESS));

        tm.updateEpic(new Epic(1, "Переехать", "В купленную квартиру"));
        tm.updateSubtask(new Subtask(1, "Собрать вещи", "Из шкафа", Status.DONE, move));
        tm.updateSubtask(new Subtask(2, "Сложить вещи", "В машину", Status.DONE, move));

        tm.updateSubtask(new Subtask(3, "Сварить макароны", "Бабочки", Status.DONE, cook));

        System.out.println("\n Проверка методов обновления задач: \n");
        System.out.println(tm.getTasksList());
        System.out.println();
        System.out.println(tm.getEpicsList());
        System.out.println();
        System.out.println(tm.getSubtasksList());

        System.out.println("\n Проверка методов получнеия по id: \n");
        System.out.println(tm.getEpicById(2));
        System.out.println(tm.getTaskById(2));
        System.out.println(tm.getSubtaskById(3));

        System.out.println("\n Проверка метода удаления по id: \n");
        tm.deleteTaskById(2);
        System.out.println(tm.getTaskById(2));

        System.out.println("\n Проверка метода полчуения сабтаска из списка эпиков: \n");
        System.out.println(tm.getSubtasksInEpic(2));

        System.out.println("\n Проверка удаления всех сабтасков из всех эпиков \n");
        tm.deleteAllSubtasks();
        System.out.println(tm.getSubtasksList());
        System.out.println(tm.getEpicsList());

        System.out.println("\n Проверка удаления всех эпиков (и сабтасков): \n");
        tm.deleteAllEpics();
        System.out.println(tm.getEpicsList());
        System.out.println(tm.getSubtasksList());
    }
}
