package main;

import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

//        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
//        taskManager.addTask(task1);
//        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);
//        taskManager.addTask(task2);
//        Epic epic1 = new Epic("Эпик1", "Описание1");
//        taskManager.addEpic(epic1);
//        Subtask subtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 3);
//        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 3);
//        Subtask subtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 3);
//        taskManager.addSubtask(subtask1);
//        taskManager.addSubtask(subtask2);
//        taskManager.addSubtask(subtask3);
//        Epic epic2 = new Epic("Эпик2", "Описание2");
//        taskManager.addEpic(epic2);
//
//        taskManager.getTaskById(2);
//        taskManager.getSubTaskById(5);
//        taskManager.getEpicById(7);
//        taskManager.getTaskById(1);
//        taskManager.getSubTaskById(4);
//        taskManager.getSubTaskById(6);
//        taskManager.getEpicById(3);
//        taskManager.getTaskById(1);
//        taskManager.getSubTaskById(5);
//        taskManager.getTaskById(2);
//        taskManager.getTaskById(2);
//        System.out.println(taskManager.getHistory());
//        System.out.println("_".repeat(20));
//
//        taskManager.removeTask(1);
//        System.out.println(taskManager.getHistory());
//        System.out.println("_".repeat(20));
//
//        taskManager.removeSubtask(5);
//        System.out.println(taskManager.getHistory());
//        System.out.println("_".repeat(20));
//
//        taskManager.removeEpic(3);
//        System.out.println(taskManager.getHistory());
//        System.out.println("_".repeat(20));
//
//        printAllTasks(taskManager);
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, null, null);
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик1", "Описание1");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2
                ,LocalDateTime.of(2025,10,1,10,0), Duration.ofMinutes(55));
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2
                ,LocalDateTime.of(2025,10,10,10,0), Duration.ofMinutes(55));
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2
                ,null, Duration.ofMinutes(55));
        taskManager.addSubtask(subtask3);
        Subtask subtask4 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, 2
                ,LocalDateTime.of(2025,10,1,5,30), Duration.ofMinutes(55));
        subtask4.setId(3);
        taskManager.updateSubTask(subtask4);

        Task task2 = new Task("Задача 2", "Описание 1", TaskStatus.NEW
                , LocalDateTime.of(2025,10,1,14,0), Duration.ofMinutes(30));
        taskManager.addTask(task2);

        Task task11 = new Task("Задача 11", "Описание 11", TaskStatus.NEW,
                LocalDateTime.of(2025,11,1,14,10), Duration.ofMinutes(30));
        task11.setId(1);
        taskManager.updateTask(task11);

        System.out.println(taskManager.getPrioritizedTasks());

        printAllTasks(taskManager);



    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubtasksByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}