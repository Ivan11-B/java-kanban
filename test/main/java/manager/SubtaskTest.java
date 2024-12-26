package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubtaskTest {
    @Test
    void shouldSubtaskEqualsToId() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик1", "Описание1");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1);
        subtask1.setId(2);
        subtask2.setId(2);

        Boolean isEqualsSubtask = subtask1.equals(subtask2);

        Assertions.assertTrue(isEqualsSubtask);
    }
}
