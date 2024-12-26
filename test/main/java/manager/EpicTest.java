package main.java.manager;

import main.java.tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {

    @Test
    void shouldEpicEqualsToId() {
        Epic epic1 = new Epic("Эпик1", "Описание1");
        Epic epic2 = new Epic("Эпик2", "Описание2");
        epic1.setId(2);
        epic2.setId(2);

        Boolean isEqualsEpic = epic1.equals(epic2);

        Assertions.assertTrue(isEqualsEpic);
    }
}
