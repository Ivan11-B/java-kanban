package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idCounter = 1;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int getNextId() {
        return idCounter++;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public Task addTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        System.out.println("Добавлена задача: " + task.getId());
        return task;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void removeTask(Integer id) {
        tasks.remove(id);
        System.out.println("Задача удалена!");
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены!");
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        System.out.println("Добавлена подзадача: " + subtask.getId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskInEpic(subtask.getId());
        updateEpicStatus(epic);
        updateEpic(epic);
        return subtask;
    }

    @Override
    public Subtask getSubTaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void removeSubtask(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.removeSubTask(id);
        updateEpicStatus(epic);
        updateEpic(epic);
        subtasks.remove(id);
        System.out.println("Подзадача удалена!");
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
            updateEpicStatus(epic);
            updateEpic(epic);
        }
        System.out.println("Все подзадачи удалены!");
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
        updateEpic(epic);
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        System.out.println("Добавлен эпик: " + epic.getId());
        return epic;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void removeEpic(Integer id) {
        for (Integer idSubtask : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
        System.out.println("Эпик удален!");
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены!");
    }

    @Override
    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        for (Integer i : epic.getSubtaskId()) {
            if (subtasks.get(i).getStatus() == TaskStatus.NEW) {
                newStatusCounter++;
            } else if (subtasks.get(i).getStatus() == TaskStatus.DONE){
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == epic.getSubtaskId().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (doneStatusCounter == epic.getSubtaskId().size()){
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Integer id) {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Integer idSubtask : epics.get(id).getSubtaskId()) {
            listSubtask.add(subtasks.get(idSubtask));
        }
        return listSubtask;
    }
    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

}