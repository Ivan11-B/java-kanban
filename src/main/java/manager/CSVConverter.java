package main.java.manager;

import main.java.tasks.*;

public class CSVConverter {

    public static String taskAsString(Task task) {
        return String.format("%s,%s,%s,%s,%s\n", task.getId(), TaskType.TASK, task.getName(),
                task.getStatus(), task.getDescription());
    }

    public static String taskAsString(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s\n", subtask.getId(), TaskType.SUBTASK,
                subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
    }

    public static String taskAsString(Epic epic) {
        return String.format("%s,%s,%s,%s,%s\n", epic.getId(), TaskType.EPIC, epic.getName(),
                epic.getStatus(), epic.getDescription());
    }

    public static Task taskFromString(String value) {
        String[] taskData = value.trim().split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        switch (taskType) {
            case TASK:
                return new Task(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]));
            case EPIC:
                return new Epic(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]));
            case SUBTASK:
                return new Subtask(
                        Integer.parseInt(taskData[0]),
                        taskData[2],
                        taskData[4],
                        TaskStatus.valueOf(taskData[3]),
                        Integer.parseInt(taskData[5]));
            default:
                throw new IllegalStateException("Неизвестное значение: " + taskType);
        }
    }
}
