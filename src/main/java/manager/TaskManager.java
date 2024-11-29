package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int idCounter = 1;

    private int getNextId() {
        return idCounter++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }


    public Task addTask(Task task) {
        task.setId(idCounter);
        tasks.put(idCounter, task);
        System.out.println("Добавлена задача: " + idCounter);
        getNextId();
        return task;
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
        System.out.println("Задача удалена!");
    }

    public void removeAllTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены!");
    }

    public void updateTask(Task task) {
        if (task.getStatus() == TaskStatus.NEW) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            task.setStatus(TaskStatus.DONE);
        }
        tasks.put(task.getId(), task);
    }

    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(idCounter);
        subtasks.put(idCounter, subtask);
        System.out.println("Добавлена подзадача: " + idCounter);
        getNextId();
        epics.get(subtask.getEpicId()).addSubtaskInEpic(subtask.getId());
        updateEpic(epics.get(subtask.getEpicId()));                                           //обновить статут эпика
        return subtask;
    }

    public Subtask getSubTaskById(Integer id) {
        return subtasks.get(id);
    }

    public void removeSubtask(Integer id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubTask(id);
        updateEpic(epics.get(subtasks.get(id).getEpicId()));
        subtasks.remove(id);
        System.out.println("Подзадача удалена!");
                                                                            //  обновить статут эпика
    }

    public void removeAllSubtasks() {

        for (Subtask subtask : subtasks.values()) {
            epics.get(subtasks.get(subtask.getId()).getEpicId()).setStatus(TaskStatus.NEW);
            epics.get(subtasks.get(subtask.getId()).getEpicId()).removeAllSubTasks();
        }
        subtasks.clear();
        System.out.println("Все подзадачи удалены!");              // обновить статусы эпиков
    }

    public void updateSubTask(Subtask subtask) {
        if (subtask.getStatus() == TaskStatus.NEW) {
            subtask.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            subtask.setStatus(TaskStatus.DONE);
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epics.get(subtask.getEpicId()));
                                                                         // обновить статус эпика
    }

    public Epic addEpic(Epic epic) {
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        System.out.println("Добавлен эпик: " + idCounter);
        getNextId();
        return epic;
    }

    public Epic getSEpicById(Integer id) {
        return epics.get(id);
    }

    public void removeEpic(Integer id) {
        for (Integer idSubtask : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
        System.out.println("Эпик удален!");
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены!");
    }

    public void updateEpic(Epic epic) {
        int statusCounter = 0;
        for (Integer i : epic.getSubtaskId()) {
            if (subtasks.get(i).getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                break;
            } else if (subtasks.get(i).getStatus() == TaskStatus.DONE){
                statusCounter++;
            }
        }
        if (statusCounter == epic.getSubtaskId().size()) {
            epic.setStatus(TaskStatus.DONE);
            epics.put(epic.getId(), epic);
        } else if (statusCounter > 0){
            epic.setStatus(TaskStatus.IN_PROGRESS);
            epics.put(epic.getId(), epic);
        }
    }

    public ArrayList<Subtask> getSubtasksToEpic(Integer id) {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Integer idSubtask : epics.get(id).getSubtaskId()) {
            listSubtask.add(subtasks.get(idSubtask));
        }
        return listSubtask;
    }




















}
