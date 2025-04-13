package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final int id;
    private String name;
    private String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected Status status;
    protected Type type;

    public Task(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    //Добавлен конструктор копирования для того, чтобы сохранять старые (не обновленные) задачи в истории
    public Task(Task oldTask) {
        this.id = oldTask.id;
        this.name = oldTask.name;
        this.description = oldTask.description;
        this.status = oldTask.status;
        this.type = oldTask.type;
        this.duration = oldTask.duration;
        this.startTime = oldTask.startTime;
    }

    public void update(Task updatedTask) {
        this.name = updatedTask.name;
        this.description = updatedTask.description;
        this.status = updatedTask.status;
        this.startTime = updatedTask.getStartTime();
        this.duration = updatedTask.getDuration();
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

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}
