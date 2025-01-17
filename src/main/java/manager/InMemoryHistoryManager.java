package main.java.manager;

import main.java.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private Node head;
    private Node last;
    private final Map<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
       Integer id = task.getId();
       linkLast(task);
       if (history.containsKey(id)) {
           removeNode(history.get(id));
       }
       history.put(id, last);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            historyList.add(currentNode.item);
            currentNode = currentNode.next;
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node currenNode = history.get(id);
        if (currenNode == null) {
            return;
        }
        if (history.size() == 1) {
            history.remove(id);
            head = null;
        } else {
            removeNode(currenNode);
            history.remove(id);
        }
    }

    private void linkLast(Task task) {
        Node currentLast = last;
        Node newNode = new Node(currentLast,task, null);
        last = newNode;
        if (currentLast == null) {
            head = newNode;
        } else {
            currentLast.next = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node == head && history.size() == 1) {
            Node currentNode = node.next;
            currentNode.prev = null;
            head = currentNode;
            last = currentNode;
        } else if (node == head && history.size() > 1) {
            Node nodeNext = head.next;
            nodeNext.prev = null;
            head = nodeNext;
        } else if (node == last) {
            Node nodePrev = last.prev;
            nodePrev.next = null;
            last = nodePrev;
        } else {
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
        }
    }

    private static class Node{
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
