package model;

import java.util.ArrayList;
import java.util.HashSet;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW); // При создании эпика по умолчанию устанавливем ему статус NEW, так
        //как подзадач в нем нет
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateStatus(); //При добавлении новой подзадачи проверяем статус эпика
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
        updateStatus();
    }

    public void updateStatus() {
        HashSet<Status> statuses = new HashSet<>();
        for (Subtask subtask : getSubtaskList()) {
            statuses.add(subtask.status);
        } //Спасибо за проверку задания! Было интересно его выполнять!
        if (statuses.size() == 1) {
            this.status = statuses.iterator().next();
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }

    @Override
    public void update(Task updatedTask) {
        super.update(updatedTask);
        updateStatus();//При обновлении эпика проверяем статус эпика по статусам подзадач
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
