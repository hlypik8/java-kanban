import model.*;
import manager.*;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        //Осуществляем проверку работы программы

        tm.newTask(new Task(100001, "Купить молоко", "В пятерочке", Status.NEW));
        tm.newTask(new Task(100002, "Погулять с собакой", "В парке", Status.NEW));

        Epic move = new Epic(200001, "Переехать", "в новую квартиру");
        tm.newEpic(move);
        tm.newSubtask(new Subtask(300001, "Собрать вещи", "Из шкафа", Status.NEW, move));
        tm.newSubtask(new Subtask(300002, "Сложить вещи", "В машину", Status.NEW, move));
        tm.newSubtask(new Subtask(300003, "Купить стол", "Кухонный", Status.NEW, move));

        Epic cook = new Epic(200002, "Приготовить ужин", "Пасту");
        tm.newEpic(cook);

        System.out.println(tm.getHistory());
        System.out.println("1///////////////////////////////");

        tm.getTaskById(100001);
        tm.getTaskById(100002);
        System.out.println(tm.getHistory());
        System.out.println("2///////////////////////////////");
        tm.getEpicById(200001);
        System.out.println(tm.getHistory());
        System.out.println("3///////////////////////////////");
        tm.getSubtaskById(300002);
        System.out.println(tm.getHistory());
        System.out.println("4///////////////////////////////");
        tm.getSubtaskById(300003);
        System.out.println(tm.getHistory());
        System.out.println("5///////////////////////////////");
        tm.getSubtaskById(300002);
        System.out.println(tm.getHistory());
        System.out.println("6///////////////////////////////");
        tm.deleteTaskById(100001);
        System.out.println(tm.getHistory());
        System.out.println("7///////////////////////////////");
        tm.deleteEpicById(200001);
        System.out.println(tm.getHistory());
        System.out.println("8///////////////////////////////");
        //Все работет согласно требованиям
    }
}
