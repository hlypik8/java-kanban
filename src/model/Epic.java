package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Epic extends Task {
    private final Collection<Subtask> subtaskList = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW); // При создании эпика по умолчанию устанавливем ему статус NEW, так
        //как подзадач в нем нет
        this.type = Type.EPIC;
    }

    //Также добавим констроуктор копировнаия
    //Здесь осталась ошибка с прошлого спринта. Эпик копировался без списка подзадач
    public Epic(Epic currentEpic) {
        super(currentEpic);
        this.subtaskList.addAll(currentEpic.getSubtaskList()); // Копируем подзадачи
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateStatus(); //При добавлении новой подзадачи проверяем статус эпика
    }

    public Collection<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
        updateStatus();
    }

    public void updateStatus() {
        HashSet<Status> statuses = new HashSet<>();
        for (Subtask subtask : getSubtaskList()) {
            statuses.add(subtask.getStatus());
        }
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
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
