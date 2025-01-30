import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubtask(Subtask subtask){
        subtaskList.add(subtask);
    }


}
