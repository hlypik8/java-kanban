package manager;

import model.*;

import java.io.File;
import java.io.FileWriter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public void save() {
        try (FileWriter fw = new FileWriter()) {

        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {

    }

    public String toString(Task task) {
        if(task.getType() == Type.SUBTASK){
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

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) {
        super.newTask(epic);
        save();
    }

    @Override
    public void newSubtask(Subtask subtask) {
        super.newTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateTask(subtask);
        save();
    }
}
