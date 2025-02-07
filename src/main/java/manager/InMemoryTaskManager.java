package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected int idCounter = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int getNextId() {
        return ++idCounter;
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
        Task taskInManager = new Task(task);
        taskInManager.setId(getNextId());
        tasks.put(taskInManager.getId(), taskInManager);
        System.out.println("Добавлена задача: " + taskInManager.getId());
        task.setId(taskInManager.getId());
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
        historyManager.remove(id);
        System.out.println("Задача удалена!");
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
        System.out.println("Все задачи удалены!");
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task currentTask = tasks.get(task.getId());
            currentTask.setName(task.getName());
            currentTask.setDescription(task.getDescription());
            currentTask.setStatus(task.getStatus());
            tasks.put(task.getId(), currentTask);
            return task;
        } else {
            return null;
        }

    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask subtaskInManager = new Subtask(subtask);
        subtaskInManager.setId(getNextId());
        subtasks.put(subtaskInManager.getId(), subtaskInManager);
        System.out.println("Добавлена подзадача: " + subtaskInManager.getId());
        Epic epic = epics.get(subtaskInManager.getEpicId());
        epic.addSubtaskInEpic(subtaskInManager.getId());
        updateEpicStatus(epic);
        updateEpic(epic);
        subtask.setId(subtaskInManager.getId());
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
        historyManager.remove(id);
        System.out.println("Подзадача удалена!");
    }

    @Override
    public void removeAllSubtasks() {
        for (Task subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
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
        if (subtasks.containsKey(subtask.getId())) {
            Subtask currentSubtask = subtasks.get(subtask.getId());
            currentSubtask.setName(subtask.getName());
            currentSubtask.setDescription(subtask.getDescription());
            currentSubtask.setStatus(subtask.getStatus());
            currentSubtask.setEpicId(subtask.getEpicId());
            subtasks.put(subtask.getId(), currentSubtask);
            Epic epic = epics.get(currentSubtask.getEpicId());
            updateEpicStatus(epic);
            updateEpic(epic);
            return subtask;
        } else {
            return null;
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic epicInManager = new Epic(epic);
        epicInManager.setId(getNextId());
        epics.put(epicInManager.getId(), epicInManager);
        System.out.println("Добавлен эпик: " + epicInManager.getId());
        epic.setId(epicInManager.getId());
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
            historyManager.remove(idSubtask);
        }
        for (Integer idSubtask : epics.get(id).getSubtaskId()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
        historyManager.remove(id);
        System.out.println("Эпик удален!");
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                historyManager.remove(subtaskId);
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены!");
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic currentEpic = epics.get(epic.getId());
            currentEpic.setName(epic.getName());
            currentEpic.setDescription(epic.getDescription());
            epics.put(epic.getId(), currentEpic);
            return epic;
        }
        return null;
    }

    protected void updateEpicStatus(Epic epic) {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        for (Integer i : epic.getSubtaskId()) {
            if (subtasks.get(i).getStatus() == TaskStatus.NEW) {
                newStatusCounter++;
            } else if (subtasks.get(i).getStatus() == TaskStatus.DONE) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == epic.getSubtaskId().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (doneStatusCounter == epic.getSubtaskId().size()) {
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}