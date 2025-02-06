package main.java.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskId;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription(), TaskStatus.NEW);
        this.subtaskId = epic.getSubtaskId();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void addSubtaskInEpic(Integer id) {
        if (!subtaskId.contains(id)) {
            subtaskId.add(id);
        } else {
            System.out.println("Задача уже существует!");
        }
    }

    public void removeSubTask(Integer id) {
        if (subtaskId.contains(id)) {
            subtaskId.remove(id);
        } else {
            System.out.println("Задача не найдена!");
        }
    }

    public void removeAllSubTasks() {
        subtaskId.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() + "," +
                "\n" +
                "subtasks=" + subtaskId +
                "\n" +
                '}';
    }
}