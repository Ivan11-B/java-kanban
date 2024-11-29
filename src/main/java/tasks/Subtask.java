package main.java.tasks;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer EpicId) {
        super(name, description, taskStatus);
        this.epicId = EpicId;
    }

    public Integer getEpicId() {

        return epicId;
    }

    public void setEpicId(Integer epicId) {

        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}' + "\n";
    }
}
