package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void addHistoryList() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, null, Duration.ofMinutes(100));
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove_deleteTaskFromHistory() {
        Task task1 = new Task("Test addNewTask1", "Test addNewTask description", TaskStatus.NEW, null, null);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask description", TaskStatus.NEW, null, null);
        Task task3 = new Task("Test addNewTask3", "Test addNewTask description", TaskStatus.NEW, null, null);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        Epic epic2 = new Epic("Эпик2", "Описание2");
        epic1.setId(4);
        epic2.setId(5);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 4, null, null);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 4, null, null);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание 3", TaskStatus.NEW, 5, null, null);
        subtask1.setId(6);
        subtask2.setId(7);
        subtask3.setId(8);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(task2);
        historyManager.add(subtask2);
        historyManager.add(epic2);
        historyManager.add(task3);
        historyManager.add(subtask3);

        historyManager.remove(2);
        historyManager.remove(4);
        historyManager.remove(8);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(5, history.size(), "Задача, подзадача и эпик не удалены.");
    }
}