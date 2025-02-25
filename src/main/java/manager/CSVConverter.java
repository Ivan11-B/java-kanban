package main.java.manager;

import main.java.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class CSVConverter {

    public static String taskAsString(Task task) {
        String formattedStartTimeTask = Optional.ofNullable(task.getStartTime())
                .map(time -> time.format(Task.FORMATTER))
                .orElse("null");
        String formattedDurationTask = Optional.ofNullable(task.getDuration())
                .map(time -> Long.toString(time.toMinutes()))
                .orElse("null");
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), TaskType.TASK, task.getName(),
                task.getStatus(), task.getDescription(),
                formattedStartTimeTask,
                formattedDurationTask);
    }

    public static String taskAsString(Subtask subtask) {
        String formattedStartTimeSubtask = Optional.ofNullable(subtask.getStartTime())
                .map(time -> time.format(Task.FORMATTER))
                .orElse("null");
        String formattedDurationSubtask = Optional.ofNullable(subtask.getDuration())
                .map(time -> Long.toString(time.toMinutes()))
                .orElse("null");
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", subtask.getId(), TaskType.SUBTASK,
                subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId(),
                formattedStartTimeSubtask,
                formattedDurationSubtask);
    }

    public static String taskAsString(Epic epic) {
        return String.format("%s,%s,%s,%s,%s\n", epic.getId(), TaskType.EPIC, epic.getName(),
                epic.getStatus(), epic.getDescription());
    }

    public static Task taskFromString(String value) {
        String[] taskData = value.trim().split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        return switch (taskType) {
            case TASK -> new Task(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    TaskStatus.valueOf(taskData[3]),
                    getStartTime(taskData[5]),
                    getDuration(taskData[6]));
            case EPIC -> new Epic(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    TaskStatus.valueOf(taskData[3]));
            case SUBTASK -> new Subtask(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    TaskStatus.valueOf(taskData[3]),
                    Integer.parseInt(taskData[5]),
                    getStartTime(taskData[6]),
                    getDuration(taskData[7]));
        };
    }

    private static LocalDateTime getStartTime(String time) {
        if (time.equals("null")) {
            return null;
        } else {
            return LocalDateTime.parse(time, Task.FORMATTER);
        }
    }

    private static Duration getDuration(String time) {
        if (time.equals("null")) {
            return null;
        } else {
            return Duration.ofMinutes(Integer.parseInt(time));
        }
    }
}
