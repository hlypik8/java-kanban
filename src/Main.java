import model.*;
import manager.*;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
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

        System.out.println("\n printAllTasks \n");
        printAllTasks(tm);

        // Обновим статусы задач для проверки методов обновления
        tm.updateTask(new Task(1, "Купить молоко", "В пятерочке", Status.DONE));
        tm.updateTask(new Task(2, "Погулять с собакой", "В парке", Status.IN_PROGRESS));

        tm.updateEpic(new Epic(1, "Переехать", "В купленную квартиру"));
        tm.updateSubtask(new Subtask(1, "Собрать вещи", "Из шкафа", Status.DONE, move));
        tm.updateSubtask(new Subtask(2, "Сложить вещи", "В машину", Status.DONE, move));

        tm.updateSubtask(new Subtask(3, "Сварить макароны", "Бабочки", Status.DONE, cook));

        System.out.println("\n printAllTasks \n");
        printAllTasks(tm);

        System.out.println("\n Проверка методов получнеия по id: и метода получения истории просмотра\n");
        System.out.println(tm.getEpicById(2));
        System.out.println(tm.getTaskById(2));
        System.out.println(tm.getSubtaskById(3));

        // Здесь история будет заполнена т.к. получаем задачи по Id
        System.out.println("\n printAllTasks \n");
        printAllTasks(tm);


        System.out.println("\n Проверка метода удаления по id: \n");
        tm.deleteTaskById(2);
        System.out.println(tm.getTasksList());

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

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicsList()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksInEpic(epic.id)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasksList()) {
            System.out.println(subtask);
        }
        //История будет пуста т.к по ТЗ в историю заносятся только те задачи, которые были получены по Id
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
