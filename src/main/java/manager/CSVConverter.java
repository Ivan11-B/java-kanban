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
        LocalDateTime formattedStartTime;
        Duration duration;
        TaskType taskType = TaskType.valueOf(taskData[1]);
        switch (taskType) {
            case TASK: {
                if (taskData[5].equals("null")) {
                    formattedStartTime = null;
                } else {
                    formattedStartTime = LocalDateTime.parse(taskData[5], Task.FORMATTER);
                }
                if (taskData[6].equals("null")) {
                    duration = null;
                } else {
                    duration = Duration.ofMinutes(Integer.parseInt(taskData[6]));
                }
                return new Task(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]),
                        formattedStartTime,
                        duration);
            }
            case EPIC:
                return new Epic(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]));
            case SUBTASK: {
                if (taskData[6].equals("null")) {
                    formattedStartTime = null;
                } else {
                    formattedStartTime = LocalDateTime.parse(taskData[6], Task.FORMATTER);
                }
                if (taskData[7].equals("null")) {
                    duration = null;
                } else {
                    duration = Duration.ofMinutes(Integer.parseInt(taskData[7]));
                }
                return new Subtask(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]),
                        Integer.parseInt(taskData[5]),
                        formattedStartTime,
                        duration);
            }
        }
        return null;
    }
}
