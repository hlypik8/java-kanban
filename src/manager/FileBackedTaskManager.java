package manager;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            //Записываем сналчала заголовок, затем все задачи по очереди. Наверное не очень хорошо, что при каждом
            //вызове save() файл полностью переписывается заново, но я пока не придумал как реализовать
            //метод, который  бы следил за всем записями
            writer.write("id,type,name,status,description,epic\n"); //Оформляем заголовок
            for (Task t : getTasksList()) {
                writer.write(toString(t) + "\n");
            }
            for (Epic e : getEpicsList()) {
                writer.write(toString(e) + "\n");
            }
            for (Subtask s : getSubtasksList()) {
                writer.write(toString(s) + "\n");
            }
        } catch (IOException e) {
            System.out.print("I/O ошибка при сохранении в файл: ");
            e.printStackTrace();
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] lines = content.split("\n");//Разделяем по строкам

            for (int i = 1; i < lines.length; i++) { //i=1 потому что нужно пропустить заголовок
                Task task = manager.fromString(lines[i]);

                if (lines[i].isEmpty()) {
                    continue;
                }

                switch (task.getType()) {
                    case TASK:
                        manager.newTask(task);
                        break;
                    case EPIC:
                        manager.newEpic((Epic) task);
                        break;
                    case SUBTASK:
                        manager.newSubtask((Subtask) task);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.print("Ошибка загрузки из файла: ");
            e.printStackTrace();
        }
        return manager;
    }

    public String toString(Task task) {
        if (task.getType() == Type.SUBTASK) { //Если сабтаск, то формат записи в файл отличен от тасков и эпиков
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d",
                    subtask.getId(),
                    subtask.getType().toString(),
                    subtask.getName(),
                    subtask.getStatus().toString(),
                    subtask.getDescription(),
                    subtask.getEpic().getId()
            );
        }
        return String.format("%d,%s,%s,%s,%s",
                task.getId(),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription()
        );
    }

    public Task fromString(String value) throws ManagerLoadException {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        Type type = Type.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        try {
            switch (type) {
                case TASK:
                    return new Task(id, name, description, status);
                case EPIC:
                    return new Epic(id, name, description);
                case SUBTASK:
                    int epicId = Integer.parseInt(fields[5]);
                    Epic parentEpic = getEpicById(epicId);
                    if (parentEpic == null) {
                        throw new IllegalArgumentException("Эпик не найден " + epicId);
                    }
                    Subtask subtask = new Subtask(id, name, description, status, parentEpic);
                    parentEpic.addSubtask(subtask);
                    return subtask;
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.print("Ошибка парсинга: ");
            e.printStackTrace();
            throw new ManagerLoadException("Ошибка записи из файла");
        }
    }

    @Override
    public void newTask(Task task) throws ManagerSaveException {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) throws ManagerSaveException {
        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubtask(Subtask subtask) throws ManagerSaveException {
        super.newSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    //2f) Методы для удаления задачи, подзадачи и эпика по id
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }
}
