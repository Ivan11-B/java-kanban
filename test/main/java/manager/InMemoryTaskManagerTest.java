package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryTaskManagerTest {
    TaskManager taskManager;


    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask() {
        String name = "Задача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Task task = new Task(name, description, status);

        Task createdTask = taskManager.addTask(task);

        Assertions.assertNotNull(createdTask.getId());
    }

    @Test
    void getTaskById() {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        int taskId = taskManager.addTask(task).getId();

        Task createdTask = taskManager.getTaskById(taskId);

        Assertions.assertEquals(task,createdTask);
    }

    @Test
    void addSubtask() {
        String name = "Подзадача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(name, description, status,1);

        Subtask createdSubtask = taskManager.addSubtask(subtask);

        Assertions.assertNotNull(createdSubtask.getId());
    }

    @Test
    void getSubTaskById() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Задача 1", "Описание 1", TaskStatus.NEW, 1);
        int subtaskId = taskManager.addSubtask(subtask).getId();

        Subtask createdSubtask = taskManager.getSubTaskById(subtaskId);

        Assertions.assertEquals(subtask,createdSubtask);
    }

    @Test
    void addEpic() {
        Epic epic1 = new Epic("Эпик1", "Описание1");

        Epic createdEpic = taskManager.addEpic(epic1);

        Assertions.assertNotNull(createdEpic.getId());
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Эпик1", "Описание1");
        int epicId = taskManager.addEpic(epic).getId();

        Epic createdEpic = taskManager.getEpicById(epicId);

        Assertions.assertEquals(epic, createdEpic);
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        final int taskId = taskManager.addTask(task).getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

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
    void taskShouldEqualsToId() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);
        task2.setId(1);

        Assertions.assertEquals(task1,task2,"Разные ID");
    }

    @Test
    void subtaskShouldEqualsToId() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        subtask1.setId(4);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1);
        subtask2.setId(4);

        Assertions.assertEquals(subtask1,subtask2,"Разные ID");
    }

    @Test
    void epicShouldEqualsToId() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        epic2.setId(1);

        Assertions.assertEquals(epic1,epic2,"Разные ID");
    }

    @Test
    void managerToReadyWork() {
        Assertions.assertNotNull(taskManager);
    }

    @Test
    void shouldConstantFieldsTaskAfterAddInManager() {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);

        Task createdTask = taskManager.addTask(task);

        Assertions.assertEquals(task.getName(), createdTask.getName());
        Assertions.assertEquals(task.getDescription(), createdTask.getDescription());
        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    void shouldTaskByHistoryAfterUpdate() {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);

        Task createdTask = taskManager.getHistory().get(0);

        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
    }
}