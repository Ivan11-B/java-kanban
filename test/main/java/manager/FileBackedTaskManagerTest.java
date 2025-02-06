package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    void loadFromFile_saveAndLoadEmptyFile() throws IOException {
        File file = File.createTempFile("test1", null);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        Task task = new Task("Задача 2", "Описание 11", TaskStatus.NEW);
        task = fileBackedTaskManager.addTask(task);

        fileBackedTaskManager.removeTask(task.getId());

        assertEquals(37, file.length(), "Файл не пустой.");
    }

    @Test
    void loadFromFile_loadFromFile() throws IOException {
        File file = File.createTempFile("test2", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
        Task task = new Task("Задача 2", "Описание 11", TaskStatus.NEW);
        fileBackedTaskManager.addTask(task);
        Task task2 = new Task("Задача 2", "Описание 11", TaskStatus.NEW);
        fileBackedTaskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        fileBackedTaskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 3);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        int i = fileBackedTaskManager.getAllTasks().size();
        int i2 = fileBackedTaskManager2.getAllTasks().size();

        assertEquals(i, i2, "Записи не равны");
    }
}