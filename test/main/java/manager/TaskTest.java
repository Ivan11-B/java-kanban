package main.java.manager;

import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    void shouldTaskEqualsToId() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW, null, null);
        task1.setId(2);
        task2.setId(2);

        Boolean isEqualsTask = task1.equals(task2);

        Assertions.assertTrue(isEqualsTask);
    }
}
