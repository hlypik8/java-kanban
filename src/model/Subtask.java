package model;

public class Subtask extends Task {
    //Как я понял из ТЗ, сабтаски не могут существовать сами по себе, они обязательно должны
    //принадлежать какому-либо эпику. Эпик же в свою очередь может существовать и без сабтасков (то есть быть пустым)

    private final Epic epic; //Ссылка на эпик в котором находится сабтаск

    public Subtask(int id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Subtask(Subtask subtask, Epic epic){
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
                "epic=" + (epic != null ? epic.id : -1) +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
