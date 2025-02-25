package main.java.manager;

import main.java.exception.ManagerSaveException;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask_setNewId() throws IllegalAccessException {
        String name = "Задача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Task task = new Task(name, description, status, null, null);

        Task createdTask = taskManager.addTask(task);

        assertNotNull(createdTask, "Задача не найдена.");
        Assertions.assertNotNull(createdTask.getId());
        Assertions.assertEquals(task.getName(), createdTask.getName());
        Assertions.assertEquals(task.getDescription(), createdTask.getDescription());
        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    void getTaskById_getTaskEqualsTaskAddInTaskManager() throws IllegalAccessException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        int taskId = taskManager.addTask(task).getId();

        Optional<Task> createdTask = taskManager.getTaskById(taskId);

        Assertions.assertEquals(task, createdTask.get());

    }

    @Test
    void addSubtask_setNewId() throws IllegalAccessException {
        String name = "Подзадача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(name, description, status, 1, null, null);

        Subtask createdSubtask = taskManager.addSubtask(subtask);

        assertNotNull(createdSubtask, "Подзадача не найдена.");
        Assertions.assertNotNull(createdSubtask.getId());
        Assertions.assertEquals(subtask.getName(), createdSubtask.getName());
        Assertions.assertEquals(subtask.getDescription(), createdSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), createdSubtask.getStatus());
    }

    @Test
    void getSubTaskById_getSubtaskEqualsSubtaskAddInTaskManager() throws IllegalAccessException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Задача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        int subtaskId = taskManager.addSubtask(subtask).getId();

        Optional<Subtask> createdSubtask = taskManager.getSubTaskById(subtaskId);

        Assertions.assertEquals(subtask, createdSubtask.get());
    }

    @Test
    void addEpic_setNewId() {
        Epic epic = new Epic("Эпик1", "Описание1");

        Epic createdEpic = taskManager.addEpic(epic);

        assertNotNull(createdEpic, "Эпик не найден.");
        Assertions.assertNotNull(createdEpic.getId());
        Assertions.assertEquals(epic.getName(), createdEpic.getName());
        Assertions.assertEquals(epic.getDescription(), createdEpic.getDescription());
    }

    @Test
    void getEpicById_getEpicEqualsTaEpicAddInTaskManager() {
        Epic epic = new Epic("Эпик1", "Описание1");
        int epicId = taskManager.addEpic(epic).getId();

        Optional<Epic> createdEpic = taskManager.getEpicById(epicId);

        Assertions.assertEquals(epic, createdEpic.get());
    }

    @Test
    void getAllTasks_listTasksNotEntry() throws IllegalAccessException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, null, null);
        taskManager.addTask(task);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllSubTasks_listSubtasksNotEntry() throws IllegalAccessException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask);

        final List<Subtask> subtasks = taskManager.getAllSubTasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getAllEpics_listEpicsNotEntry() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void managerToReadyWork() {
        Assertions.assertNotNull(taskManager);
    }

    @Test
    void removeTask_deleteTaskFromTaskListAndHistory() throws IllegalAccessException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task);
        List<Task> tasks = taskManager.getAllTasks();
        taskManager.getTaskById(1);
        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(!tasks.isEmpty());
        Assertions.assertTrue(!history.isEmpty());

        taskManager.removeTask(task.getId());
        tasks = taskManager.getAllTasks();
        history = taskManager.getHistory();

        Assertions.assertTrue(tasks.isEmpty());
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void removeSubtask_deleteSubtaskFromSubtaskListAndHistory() throws IllegalAccessException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask);
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        taskManager.getSubTaskById(2);
        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(!subtasks.isEmpty());
        Assertions.assertTrue(!history.isEmpty());

        taskManager.removeSubtask(subtask.getId());
        subtasks = taskManager.getAllSubTasks();
        List<Integer> subtaskId = epic.getSubtaskId();
        history = taskManager.getHistory();

        Assertions.assertTrue(subtasks.isEmpty());
        Assertions.assertTrue(subtaskId.isEmpty());
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void removeEpic_deleteEpicFromEpicListAndHistory() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        List<Epic> epics = taskManager.getAllEpics();
        taskManager.getEpicById(1);
        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(!epics.isEmpty());
        Assertions.assertTrue(!history.isEmpty());

        taskManager.removeEpic(epic.getId());
        epics = taskManager.getAllEpics();
        history = taskManager.getHistory();

        Assertions.assertTrue(epics.isEmpty());
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void removeAllTask_cleanTaskListAndHistory() throws IllegalAccessException {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);

        taskManager.removeAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        List<Task> history = taskManager.getHistory();

        Assertions.assertTrue(tasks.isEmpty());
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void removeAllSubtask_cleanSubtaskListAndHistory() throws IllegalAccessException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.getSubTaskById(2);

        taskManager.removeAllSubtasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        List<Integer> subtaskId = epic.getSubtaskId();
        List<Task> history = taskManager.getHistory();

        Assertions.assertTrue(subtasks.isEmpty());
        Assertions.assertTrue(subtaskId.isEmpty());
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    void removeAllEpic_cleanEpicListAndHistory() throws IllegalAccessException {
        Epic epic1 = new Epic("Эпик", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1,null, null);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addEpic(epic2);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);

        taskManager.removeAllEpics();
        List<Epic> epics = taskManager.getAllEpics();
        List<Task> history = taskManager.getHistory();

        Assertions.assertTrue(epics.isEmpty());
        Assertions.assertTrue(history.isEmpty());

    }

    @Test
    void updateTask_doNothing_taskNotExist() throws IllegalAccessException {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW, null, null);

        Task updateTask = taskManager.updateTask(task2);

        Assertions.assertNull(updateTask);
    }

    @Test
    void updateSubtask_doNothing_subtaskNotExist() throws IllegalAccessException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1, null, null);

        Subtask updateSubtask = taskManager.updateSubTask(subtask2);

        Assertions.assertNull(updateSubtask);
    }

    @Test
    void updateEpic_doNothing_epicNotExist() {
        Epic epic1 = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик", "Описание");

        Epic updateEpic = taskManager.updateEpic(epic2);

        Assertions.assertNull(updateEpic);
    }

    @Test
    void updateEpicStatus_autoStatusEpicChange() throws IllegalAccessException {
        Epic epic1 = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1, null, null);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        Optional<Epic> epicInManager = taskManager.getEpicById(1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicInManager.get().getStatus());

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subtask1);
        taskManager.updateSubTask(subtask2);

        Assertions.assertEquals(TaskStatus.DONE, epicInManager.get().getStatus());
    }

    @Test
    void shouldTaskByHistoryAfterUpdate() throws IllegalAccessException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);

        Task createdTask = taskManager.getHistory().get(0);

        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    void removeTimeTaskInGrid_AddToGrid() throws IllegalAccessException {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.of(2025, 10, 2, 12, 0), Duration.ofMinutes(55));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        int count = taskManager.getPrioritizedTasks().size();

        Assertions.assertEquals(2, count, "Список не заполнился");
    }

    @Test
    void removeTimeTaskInGrid_intersectionTime() throws IllegalAccessException {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.of(2025, 10, 1, 12, 10), Duration.ofMinutes(55));
        taskManager.addTask(task1);
        boolean exeptionThrown = false;

        try {
            taskManager.addTask(task2);
        } catch (IllegalAccessException e) {
            exeptionThrown = true;
        }

        assertTrue(exeptionThrown);
    }
}