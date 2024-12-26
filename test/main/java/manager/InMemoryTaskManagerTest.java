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
    void addTask_setNewId() {
        String name = "Задача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Task task = new Task(name, description, status);

        Task createdTask = taskManager.addTask(task);

        assertNotNull(createdTask, "Задача не найдена.");
        Assertions.assertNotNull(createdTask.getId());
        Assertions.assertEquals(task.getName(), createdTask.getName());
        Assertions.assertEquals(task.getDescription(), createdTask.getDescription());
        Assertions.assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    void getTaskById_getTaskEqualsTaskAddInTaskManager() {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        int taskId = taskManager.addTask(task).getId();

        Task createdTask = taskManager.getTaskById(taskId);

        Assertions.assertEquals(task,createdTask);

    }

    @Test
    void addSubtask_setNewId() {
        String name = "Подзадача 1";
        String description = "Описание 1";
        TaskStatus status = TaskStatus.NEW;
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(name, description, status,1);

        Subtask createdSubtask = taskManager.addSubtask(subtask);

        assertNotNull(createdSubtask, "Подзадача не найдена.");
        Assertions.assertNotNull(createdSubtask.getId());
        Assertions.assertEquals(subtask.getName(), createdSubtask.getName());
        Assertions.assertEquals(subtask.getDescription(), createdSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), createdSubtask.getStatus());
    }

    @Test
    void getSubTaskById_getSubtaskEqualsSubtaskAddInTaskManager() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Задача 1", "Описание 1", TaskStatus.NEW, 1);
        int subtaskId = taskManager.addSubtask(subtask).getId();

        Subtask createdSubtask = taskManager.getSubTaskById(subtaskId);

        Assertions.assertEquals(subtask,createdSubtask);
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

        Epic createdEpic = taskManager.getEpicById(epicId);

        Assertions.assertEquals(epic, createdEpic);
    }

    @Test
    void getAllTasks_listTasksNotEntry() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        taskManager.addTask(task);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllSubTasks_listSubtasksNotEntry() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
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
    void removeTask_deleteTaskFromTaskList() {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task);
        List<Task> tasks = taskManager.getAllTasks();
        Assertions.assertTrue(!tasks.isEmpty());

        taskManager.removeTask(task.getId());
        tasks = taskManager.getAllTasks();

        Assertions.assertTrue(tasks.isEmpty());
    }

    @Test
    void removeSubtask_deleteSubtaskFromSubtaskList() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        taskManager.addSubtask(subtask);
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        Assertions.assertTrue(!subtasks.isEmpty());

        taskManager.removeSubtask(subtask.getId());
        subtasks = taskManager.getAllSubTasks();
        List<Integer> subtaskId = epic.getSubtaskId();

        Assertions.assertTrue(subtasks.isEmpty());
        Assertions.assertTrue(subtaskId.isEmpty());
    }

    @Test
    void removeEpic_deleteEpicFromEpicList() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        List<Epic> epics = taskManager.getAllEpics();
        Assertions.assertTrue(!epics.isEmpty());

        taskManager.removeEpic(epic.getId());
        epics = taskManager.getAllEpics();

        Assertions.assertTrue(epics.isEmpty());
    }

    @Test
    void removeAllTask_cleanTaskList() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task2);

        taskManager.removeAllTasks();
        List<Task> tasks = taskManager.getAllTasks();

        Assertions.assertTrue(tasks.isEmpty());
    }

    @Test
    void removeAllSubtask_cleanSubtaskList() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.removeAllSubtasks();
        List<Subtask> subtasks = taskManager.getAllSubTasks();
        List<Integer> subtaskId = epic.getSubtaskId();

        Assertions.assertTrue(subtasks.isEmpty());
        Assertions.assertTrue(subtaskId.isEmpty());
    }

    @Test
    void removeAllEpic_cleanEpicList() {
        Epic epic1 = new Epic("Эпик", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.removeAllEpics();
        List<Epic> epics = taskManager.getAllEpics();

        Assertions.assertTrue(epics.isEmpty());
    }

    @Test
    void updateTask_doNothing_taskNotExist() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);

        Task updateTask = taskManager.updateTask(task2);

        Assertions.assertNull(updateTask);
    }

    @Test
    void updateSubtask_doNothing_subtaskNotExist() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1);

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
    void updateEpicStatus_autoStatusEpicChange() {
        Epic epic1 = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, 1);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subtask1);
        taskManager.updateSubTask(subtask2);

        Assertions.assertEquals(TaskStatus.DONE, epic1.getStatus());
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