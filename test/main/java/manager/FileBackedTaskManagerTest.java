package main.java.manager;

import main.java.exception.ManagerSaveException;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    File file;

    @BeforeEach
    public void init() {
        try {
            file = File.createTempFile("test1", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.taskManager = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    void loadFromFile_saveAndLoadEmptyFile() throws IOException, IllegalAccessException {
        Task task = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        task = taskManager.addTask(task);

        taskManager.removeTask(task.getId());
        List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        assertEquals(1, allLines.size(), "Файл не пустой.");
    }

    @Test
    void loadFromFile_loadFromFile() throws IllegalAccessException {
        Task task = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        taskManager.addTask(task);
        Task task2 = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 3, null, null);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 3, null, null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);

        assertEquals(taskManager.getAllTasks().size(), fileBackedTaskManager2.getAllTasks().size(),
                "Записи не равны");
        assertEquals(taskManager.getSubtasksByEpic(3), fileBackedTaskManager2.getSubtasksByEpic(3),
                "Списки subtask в эпиках не равны");
    }

    @Test
    void loadFromFile_loadIncorrectFile() {
        File file1 = new File("save.ttt");
        boolean exeptionThrown = false;

        try {
            FileBackedTaskManager.loadFromFile(file1);
        } catch (ManagerSaveException e) {
            exeptionThrown = true;
        }

        assertTrue(exeptionThrown);
    }

    @Test
    void updateCounterId() throws IllegalAccessException {
        Task task = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        taskManager.addTask(task);
        Task task2 = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        taskManager.addTask(task2);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        Task task3 = new Task("Задача 2", "Описание 11", TaskStatus.NEW, null, null);
        task3 = fileBackedTaskManager2.addTask(task3);

        assertEquals(3, task3.getId(), "Счетчик не обновился");
    }
}