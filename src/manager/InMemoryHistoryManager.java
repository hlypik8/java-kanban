//Класс InMemoryHistoryManager для управления историей просмотре задач
package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> historyHashMap = new HashMap<>();
    private Node tail;
    private Node head;

    public void linkLast(Task task) {
        final Node newNode = new Node(task, tail, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    void removeNode(Node node) {
        //Проверка на null
        if (node == null) {
            return;
        }
        //Переопределяем ссылки
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        //Очистка ссылок и удаление из HashMap
        node.prev = null;
        node.next = null;
        historyHashMap.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        //Проверяем, существет ли задача
        if (historyHashMap.containsKey(task.getId())) {
            Node existingNode = historyHashMap.get(task.getId());
            removeNode(existingNode);
        }
        linkLast(task);
        historyHashMap.put(task.getId(), tail);
    }

    //Реализация метода получения истории просмотра
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        Task taskToRemove = historyHashMap.get(id).task;
        if (historyHashMap.containsKey(id)) {
            if(taskToRemove.getClass() == Epic.class){
                removeAllSubtaskInEpic(id);
            }
            removeNode(historyHashMap.get(id));
        }
    }

    public void removeAllSubtaskInEpic(int id){
        Epic epic = (Epic) historyHashMap.get(id).task;
        for(Subtask subtask : epic.getSubtaskList()){
            if(historyHashMap.containsKey(subtask.getId())){
                removeNode(historyHashMap.get(subtask.getId()));
            }
        }
    }
}
