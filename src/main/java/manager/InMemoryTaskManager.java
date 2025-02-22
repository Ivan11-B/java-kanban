package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected int idCounter = 0;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritySet;
    private final Map<LocalDateTime, Boolean> gridYearTo15Minutes;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.prioritySet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        this.gridYearTo15Minutes = initializeTimeGrid();
    }

    private int getNextId() {
        return ++idCounter;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task addTask(Task task) {
        try {
            isFreeTimeInGrid(task);
            Task taskInManager = new Task(task);
            taskInManager.setId(getNextId());
            tasks.put(taskInManager.getId(), taskInManager);
            System.out.println("Добавлена задача: " + taskInManager.getId());
            task.setId(taskInManager.getId());
            return task;
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        Optional<Task> task = Optional.ofNullable(tasks.get(id));
        task.ifPresentOrElse(historyManager::add,
                () -> System.out.println("По заданному ID нет задачи"));
        return task;
    }

    @Override
    public void removeTask(Integer id) {
        removeTimeTaskInGrid(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
        System.out.println("Задача удалена!");
    }

    @Override
    public void removeAllTasks() {
        tasks.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove);
        tasks.values().forEach(this::removeTimeTaskInGrid);
        tasks.clear();
        System.out.println("Все задачи удалены!");
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            removeTimeTaskInGrid(tasks.get(task.getId()));
            try {
                isFreeTimeInGrid(task);
                Task currentTask = tasks.get(task.getId());
                currentTask.setName(task.getName());
                currentTask.setDescription(task.getDescription());
                currentTask.setStatus(task.getStatus());
                currentTask.setStartTime(task.getStartTime());
                currentTask.setDuration(task.getDuration());
                tasks.put(task.getId(), currentTask);
                return task;
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        try {
            isFreeTimeInGrid(subtask);
            Subtask subtaskInManager = new Subtask(subtask);
            subtaskInManager.setId(getNextId());
            subtasks.put(subtaskInManager.getId(), subtaskInManager);
            System.out.println("Добавлена подзадача: " + subtaskInManager.getId());
            Epic epic = epics.get(subtaskInManager.getEpicId());
            epic.addSubtaskInEpic(subtaskInManager.getId());
            updateEpicStatus(epic);
            updateStartTimeEpic(epic);
            updateEndTimeEpic(epic);
            updateDurationEpic(epic);
            updateEpic(epic);
            subtask.setId(subtaskInManager.getId());
            return subtask;
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Subtask> getSubTaskById(Integer id) {
        Optional<Subtask> subtask = Optional.ofNullable(subtasks.get(id));
        subtask.ifPresentOrElse(historyManager::add,
                () -> System.out.println("По заданному ID нет подзадачи"));
        return subtask;
    }

    @Override
    public void removeSubtask(Integer id) {
        removeTimeTaskInGrid(subtasks.get(id));
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
        subtasks.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove);
        subtasks.values().forEach(this::removeTimeTaskInGrid);
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.removeAllSubTasks();
            updateEpicStatus(epic);
            updateEpic(epic);
        });
        System.out.println("Все подзадачи удалены!");
    }

    @Override
    public Subtask updateSubTask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            removeTimeTaskInGrid(subtasks.get(subtask.getId()));
            try {
                isFreeTimeInGrid(subtask);
                Subtask currentSubtask = subtasks.get(subtask.getId());
                currentSubtask.setName(subtask.getName());
                currentSubtask.setDescription(subtask.getDescription());
                currentSubtask.setStatus(subtask.getStatus());
                currentSubtask.setEpicId(subtask.getEpicId());
                currentSubtask.setStartTime(subtask.getStartTime());
                currentSubtask.setDuration(subtask.getDuration());
                subtasks.put(subtask.getId(), currentSubtask);
                Epic epic = epics.get(currentSubtask.getEpicId());
                updateEpicStatus(epic);
                updateStartTimeEpic(epic);
                updateEndTimeEpic(epic);
                updateDurationEpic(epic);
                updateEpic(epic);
                return subtask;
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        } else {
            return null;
        }
        return null;
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
    public Optional<Epic> getEpicById(Integer id) {
        Optional<Epic> epic = Optional.ofNullable(epics.get(id));
        epic.ifPresentOrElse(historyManager::add,
                () -> System.out.println("По заданному ID нет эпика"));
        return epic;
    }

    @Override
    public void removeEpic(Integer id) {
        epics.get(id).getSubtaskId().forEach(historyManager::remove);
        epics.get(id).getSubtaskId().forEach(subtasks::remove);
        epics.remove(id);
        historyManager.remove(id);
        System.out.println("Эпик удален!");
    }

    @Override
    public void removeAllEpics() {
        epics.values().forEach(epic -> {
            epic.getSubtaskId().forEach(historyManager::remove);
            historyManager.remove(epic.getId());
        });
        epics.values().forEach(epic -> epic.getSubtaskId().forEach(subtasks::remove));
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
    public List<Subtask> getSubtasksByEpic(Integer id) {
        return epics.get(id).getSubtaskId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void updateStartTimeEpic(Epic epic) {
        Optional<LocalDateTime> startTimeEpic = getSubtasksByEpic(epic.getId()).stream()
                .map(subtask -> Optional.ofNullable(subtask.getStartTime()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(LocalDateTime::compareTo);
        startTimeEpic.ifPresent(epic::setStartTime);
    }

    protected void updateEndTimeEpic(Epic epic) {
        Optional<LocalDateTime> endTimeEpic = getSubtasksByEpic(epic.getId()).stream()
                .map(subtask -> Optional.ofNullable(subtask.getEndTime()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(LocalDateTime::compareTo);
        endTimeEpic.ifPresent(epic::setEndTime);
    }

    protected void updateDurationEpic(Epic epic) {
        if (epic.getStartTime() != null) {
            epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
        }
    }

    public Set<Task> getPrioritizedTasks() {
        prioritySet.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList());
        prioritySet.addAll(subtasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList());
        return prioritySet;
    }

    private Map<LocalDateTime, Boolean> initializeTimeGrid() {
        Map<LocalDateTime, Boolean> grid = new HashMap<>();
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        for (int i = 0; i < 365 * 96; i++) {
            grid.put(start.plusMinutes(i * 15L), true);
        }
        return grid;
    }

    private boolean isFreeTimeTask(LocalDateTime start, Task task) {
        while (start.isBefore(task.getEndTime())) {
            if (!gridYearTo15Minutes.getOrDefault(start, false)) {
                return false;
            }
            start = start.plusMinutes(15);
        }
        return true;
    }

    protected void isFreeTimeInGrid(Task task) throws IllegalAccessException {
        if (task.getStartTime() == null) {
            return;
        }
        LocalDateTime start = translateInSection(task);
        if (!isFreeTimeTask(start, task)) {
            throw new IllegalAccessException("Задачи пересекаются");
        }
        while (start.isBefore(task.getEndTime())) {
            gridYearTo15Minutes.put(start, false);
            start = start.plusMinutes(15);
        }
    }

    private void removeTimeTaskInGrid(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        LocalDateTime start = translateInSection(task);
        while (start.isBefore(task.getEndTime())) {
            gridYearTo15Minutes.put(start, true);
            start = start.plusMinutes(15);
        }
    }

    private LocalDateTime translateInSection(Task task) {
        LocalDateTime truncateTime = task.getStartTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime timeToMinutes = truncateTime.toLocalTime();
        int minutes = timeToMinutes.getHour() * 60 + timeToMinutes.getMinute();
        int section = minutes / 15;
        LocalDateTime startOfDay = truncateTime.toLocalDate().atStartOfDay();
        return startOfDay.plusMinutes(section * 15);
    }
}