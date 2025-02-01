
public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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
}
