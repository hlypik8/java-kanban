package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final Epic epic; //Ссылка на эпик в котором находится сабтаск

    public Subtask(int id, String name, String description, Status status, Epic epic, LocalDateTime startTime,
                   Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.epic = epic;
        this.type = Type.SUBTASK;
    }

    public Subtask(Subtask subtask, Epic epic) {
        super(subtask);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    //При обновлении сабтаска обновляются все поля и вызывается проверка статуса эпика, в котором находится сабтаск
    @Override
    public void update(Task updatedTask) {
        super.update(updatedTask);
        if (epic != null) {
            epic.updateStatus();
        }
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + (epic != null ? epic.getId() : -1) +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
