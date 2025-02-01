import java.util.ArrayList;

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
        boolean allSubtasksIsDone = true;
        boolean allSubtasksIsNew = true;

        for (Subtask subtask : subtaskList) {
            if (subtask.status != Status.NEW) {
                allSubtasksIsNew = false;
            }
            if (subtask.status != Status.DONE) {
                allSubtasksIsDone = false;
            }
        }
        if (allSubtasksIsDone) {
            this.status = Status.DONE;
        } else if (allSubtasksIsNew) {
            this.status = Status.NEW;
        } else this.status = Status.IN_PROGRESS;
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
