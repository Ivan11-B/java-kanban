package main.java.manager;

import main.java.exception.ManagerSaveException;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public Task addTask(Task task) throws IllegalAccessException {
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
    public Task updateTask(Task task) throws IllegalAccessException {
        Task currentTask = super.updateTask(task);
        save();
        return currentTask;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) throws IllegalAccessException {
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
    public Subtask updateSubTask(Subtask subtask) throws IllegalAccessException {
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

    private void save() {
        try (FileWriter fw = new FileWriter(file)) {
            String title = "id,type,name,status,description,epic" + "\n";
            fw.write(title);
            for (Task task : tasks.values()) {
                fw.write(CSVConverter.taskAsString(task));
            }
            for (Epic epic : epics.values()) {
                fw.write(CSVConverter.taskAsString(epic));
            }
            for (Subtask subtask : subtasks.values()) {
                fw.write(CSVConverter.taskAsString(subtask));
            }
        } catch (IOException e) {
            String errorMessage = "Ошибка при сохранении в файл: " + e.getMessage();
            System.out.println(errorMessage);
            throw new ManagerSaveException(errorMessage);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);
            List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < allLines.size(); i++) {
                Task task = CSVConverter.taskFromString(allLines.get(i));
                if (task instanceof Epic) {
                    fileBackedTaskManager.addToMap((Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.addToMap((Subtask) task);
                } else {
                    fileBackedTaskManager.addToMap(task);
                }
                fileBackedTaskManager.updateCounterId(task);
            }
            fileBackedTaskManager.saveSubtaskInListToEpic();
            return fileBackedTaskManager;
        } catch (IOException e) {
            String errorMessage = "Ошибка при загрузки из файла: " + e.getMessage();
            System.out.println(errorMessage);
            throw new ManagerSaveException(errorMessage);
        }
    }

    private void addToMap(Task task) {
        tasks.put(task.getId(), task);
    }

    private void addToMap(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void addToMap(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    private void saveSubtaskInListToEpic() {
        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtaskInEpic(subtask.getId());
            updateEpicStatus(epic);
            updateStartTimeEpic(epic);
            updateEndTimeEpic(epic);
            updateDurationEpic(epic);
            updateEpic(epic);
        }
    }

    private void updateCounterId(Task task) {
        int maxId = task.getId();
        if (maxId > idCounter) {
            idCounter = maxId;
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        File file = new File("save.csv");
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(new InMemoryHistoryManager(), file);

        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        fileBackedTaskManager1.addTask(task1);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        fileBackedTaskManager1.addEpic(epic1);

        Subtask subtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        fileBackedTaskManager1.addSubtask(subtask3);

        Subtask subtask1 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2,
                LocalDateTime.of(2025, 10, 1, 14, 0), Duration.ofMinutes(100));
        fileBackedTaskManager1.addSubtask(subtask1);

        Subtask subtask11 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2,
                LocalDateTime.of(2025, 10, 12, 14, 0), Duration.ofMinutes(100));
        subtask11.setId(3);

        fileBackedTaskManager1.updateSubTask(subtask11);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
//        System.out.println(fileBackedTaskManager2.getAllTasks());
//        System.out.println(fileBackedTaskManager2.getAllSubTasks());
        System.out.println(fileBackedTaskManager2.getAllEpics());
        System.out.println(fileBackedTaskManager2.getPrioritizedTasks());
    }
}

