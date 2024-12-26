package main.java.manager;

import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {

    @Test
    void addHistoryList() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        HistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void add_MaxListEquals10() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(10, history.size(), "История не равна 10");
    }
}