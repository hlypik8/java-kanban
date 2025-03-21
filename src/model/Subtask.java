package model;

public class Subtask extends Task {

    private final Epic epic; //Ссылка на эпик в котором находится сабтаск

    public Subtask(int id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Subtask(Subtask subtask, Epic epic) {
        super(subtask);
        this.epic = epic;
    }

    //При обновлении сабтаска обновляются все поля и вызывается проверка статуса эпика, в котором находится сабтаск
    @Override
    public void update(Task updatedTask) {
        super.update(updatedTask);
        if (epic != null) {
            epic.updateStatus();
        }
    }

    public Epic getEpic() {
        return epic;
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
