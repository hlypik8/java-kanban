package model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    public int id;
    public Status status;

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //Добавлен конструктор копирования для того, чтобы была возможность сохранять
    // старые задачи в истории
    public Task(Task oldTask){
        this.id = oldTask.id;
        this.name = oldTask.name;
        this.description = oldTask.description;
        this.status = oldTask.status;
    }

    public void update(Task updatedTask) {
        this.name = updatedTask.name;
        this.description = updatedTask.description;
        this.status = updatedTask.status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
