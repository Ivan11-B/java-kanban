package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Subtask> getAllSubTasks();

    List<Epic> getAllEpics();

    Task addTask(Task task) throws IllegalAccessException;

    Optional<Task> getTaskById(Integer id);

    void removeTask(Integer id);

    void removeAllTasks();

    Task updateTask(Task task) throws IllegalAccessException;

    Subtask addSubtask(Subtask subtask) throws IllegalAccessException;

    Optional<Subtask> getSubTaskById(Integer id);

    void removeSubtask(Integer id);

    void removeAllSubtasks();

    Subtask updateSubTask(Subtask subtask) throws IllegalAccessException;

    Epic addEpic(Epic epic);

    Optional<Epic> getEpicById(Integer id);

    void removeEpic(Integer id);

    void removeAllEpics();

    Epic updateEpic(Epic epic);

    List<Subtask> getSubtasksByEpic(Integer id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}
