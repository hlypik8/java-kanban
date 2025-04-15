//Класс InMemoryHistoryManager для управления историей просмотра задач
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

    private final Map<Integer, Node> historyMap = new HashMap<>();
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
        historyMap.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (taskExists(task)) {
            Node existingNode = historyMap.get(task.getId());
            removeNode(existingNode);
        }
        linkLast(task);
        historyMap.put(task.getId(), tail);
    }

    public boolean taskExists(Task task) {
        return historyMap.containsKey(task.getId());
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
        Task taskToRemove = historyMap.get(id).task;
        if (historyMap.containsKey(id)) {
            if (taskToRemove.getClass() == Epic.class) {
                removeAllSubtaskInEpic(id);
            }
            removeNode(historyMap.get(id));
        }
    }

    public void removeAllSubtaskInEpic(int id) {
        Epic epic = (Epic) historyMap.get(id).task;
        epic.getSubtaskList().stream()
                .map(Subtask::getId)
                .filter(historyMap::containsKey)
                .map(historyMap::get)
                .forEach(this::removeNode);
    }
}
