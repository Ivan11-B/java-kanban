package main.java.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private final List<Integer> subtaskId;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, null, null);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus, null, null);
        this.subtaskId = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription(), TaskStatus.NEW, epic.getStartTime(), epic.getDuration());
        this.subtaskId = new ArrayList<>();
    }

    public List<Integer> getSubtaskId() {
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
        String formattedStartTime = Optional.ofNullable(getStartTime())
                .map(time -> time.format(FORMATTER))
                .orElse("null");
        String formattedEndTime = Optional.ofNullable(getEndTime())
                .map(time -> time.format(FORMATTER))
                .orElse("null");
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() + "," +
                "\n" +
                "subtasks=" + subtaskId +
                ", startTime=" + formattedStartTime +
                ", endTime=" + formattedEndTime +
                ", duration=" + getDuration() +
                "\n" +
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}