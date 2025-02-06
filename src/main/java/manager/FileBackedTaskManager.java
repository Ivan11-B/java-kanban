package main.java.manager;

import main.java.exception.FileManagerFileInitializationException;
import main.java.exception.FileManagerSaveException;
import main.java.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        Task currentTask = super.addTask(task);
        save();
        return currentTask;
    }

    @Override
    public void removeTask(Integer id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task currentTask = super.updateTask(task);
        save();
        return currentTask;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask currrentSubtask = super.addSubtask(subtask);
        save();
        return currrentSubtask;
    }

    @Override
    public void removeSubtask(Integer id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        Subtask currrentSubtask = super.updateSubTask(subtask);
        save();
        return currrentSubtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic currentEpic = super.addEpic(epic);
        save();
        return currentEpic;
    }

    @Override
    public void removeEpic(Integer id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic currentEpic = super.updateEpic(epic);
        save();
        return currentEpic;
    }

    private void save()  {
        try (FileWriter fw = new FileWriter(file)) {
            String title = "id,type,name,status,description,epic" + "\n";
            fw.write(title);
            for (Task task : tasks.values()) {
                fw.write(taskAsString(task));
            }
            for (Task task : epics.values()) {
                fw.write(taskAsString(task));
            }
            for (Task task : subtasks.values()) {
                fw.write(taskAsString(task));
            }
        } catch (IOException e) {
            String errorMessage = "Ошибка при сохранении в файл: " + e.getMessage();
            System.out.println(errorMessage);
            throw new FileManagerSaveException(errorMessage);
        }
    }

    private String taskAsString(Task task) {
        Class<? extends Task> taskClass = task.getClass();
        if (taskClass == Epic.class) {
            return String.format("%s,%s,%s,%s,%s\n", task.getId(), TaskType.EPIC, task.getName(),
                    task.getStatus(), task.getDescription());
        } else if (taskClass == Subtask.class) {
            Subtask subtask = (Subtask) task;
            return String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), TaskType.SUBTASK,
                    task.getName(), task.getStatus(), task.getDescription(), subtask.getEpicId());
        } else {
            return String.format("%s,%s,%s,%s,%s\n", task.getId(), TaskType.TASK, task.getName(),
                    task.getStatus(), task.getDescription());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
            List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < allLines.size(); i++) {
                fileBackedTaskManager.taskFromString(allLines.get(i));
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            String errorMessage = "Ошибка при загрузки из файла: " + e.getMessage();
            System.out.println(errorMessage);
            throw new FileManagerFileInitializationException(errorMessage);
        }
    }

    private Task taskFromString(String value)  {
        String[] taskData = value.trim().split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        switch (taskType) {
            case TASK :
                Task task = new Task(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    getTaskStatusFromString(taskData[3]));
                tasks.put(task.getId(),task);
                setCounterId(task);
                return task;
            case EPIC :
                Epic epic = new Epic(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    getTaskStatusFromString(taskData[3]));
                epics.put(epic.getId(), epic);
                setCounterId(epic);
                return epic;
            case SUBTASK :
                Subtask subtask = new Subtask(
                    Integer.parseInt(taskData[0]),
                    taskData[2],
                    taskData[4],
                    getTaskStatusFromString(taskData[3]),
                    Integer.parseInt(taskData[5]));
                subtasks.put(subtask.getId(), subtask);
                setCounterId(subtask);
                return subtask;
            default :
                throw new IllegalStateException("Неизвестное значение: " + taskType);
        }
    }

    private void setCounterId(Task task) {
        int maxId = task.getId();
        if (maxId > idCounter) {
            idCounter = maxId;
        }
    }

    private TaskStatus getTaskStatusFromString(String value) {
        return switch (value) {
            case "NEW" -> TaskStatus.NEW;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "DONE" -> TaskStatus.DONE;
            default -> throw new IllegalStateException("Неизвестное значение: " + value);
        };
    }

    public static void main (String[] args) {
        File file = new File("save.csv");
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
        Task task11 = new Task("Задача 2", "Описание 11", TaskStatus.NEW);
        fileBackedTaskManager1.addTask(task11);
        Task task21 = new Task("Задача 2", "Описание 21", TaskStatus.NEW);
        fileBackedTaskManager1.addTask(task21);
        Task task31 = new Task("Задача 2", "Описание 31", TaskStatus.NEW);
        fileBackedTaskManager1.addTask(task31);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        fileBackedTaskManager1.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 4);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 4);
        fileBackedTaskManager1.addSubtask(subtask1);
        fileBackedTaskManager1.addSubtask(subtask2);
        Epic epic2 = new Epic("Эпик2", "Описание2");
        fileBackedTaskManager1.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 7);
        fileBackedTaskManager1.addSubtask(subtask3);
        fileBackedTaskManager1.removeEpic(4);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        fileBackedTaskManager2.removeTask(1);
    }
}

