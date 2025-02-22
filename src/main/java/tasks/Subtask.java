package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, TaskStatus taskStatus, Integer epicId, LocalDateTime startTime, Duration duration) {
        super(id, name, description, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getStartTime(), subtask.getDuration());
        this.epicId = subtask.getEpicId();
    }

    public Integer getEpicId() {

        return epicId;
    }

    public void setEpicId(Integer epicId) {

        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String formattedStartTime = Optional.ofNullable(getStartTime())
                .map(time -> time.format(FORMATTER))
                .orElse("null");
        String formattedEndTime = Optional.ofNullable(getEndTime())
                .map(time -> time.format(FORMATTER))
                .orElse("null");
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", startTime=" + formattedStartTime +
                ", endTime=" + formattedEndTime +
                ", duration=" + getDuration() +
                '}' + "\n";
    }
}