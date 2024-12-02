package main;

import main.java.manager.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик1" , "Описание1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 3 );
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 3 );
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Epic epic2 = new Epic("Эпик2" , "Описание2");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 6 );
        taskManager.addSubtask(subtask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());

        task1.setName("Задача 11");
        taskManager.updateTask(task1);
        task2.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task2);
        System.out.println(taskManager.getAllTasks());

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        taskManager.updateSubTask(subtask2);
        taskManager.updateSubTask(subtask3);

        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());

        epic2.setDescription("Описание 22");
        taskManager.updateEpic(epic2);
        System.out.println(taskManager.getAllEpics());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());

        taskManager.removeTask(2);
        System.out.println(taskManager.getAllTasks());

        taskManager.removeEpic(3);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
    }
}