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
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private FileBackedTaskManager(HistoryManager historyManager, File file) {
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
            updateEpic(epic);
        }
    }

    private void updateCounterId(Task task) {
        int maxId = task.getId();
        if (maxId > idCounter) {
            idCounter = maxId;
        }
    }

    public static void main(String[] args) {
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

