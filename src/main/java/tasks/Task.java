package main.java.tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public void setStatus(TaskStatus status) {

        this.status = status;
    }

    public TaskStatus getStatus() {

        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' + "\n";
    }
}