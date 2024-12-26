package main.java.manager;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    Task addTask(Task task);

    Task getTaskById(Integer id);

    void removeTask(Integer id);

    void removeAllTasks();

    Task updateTask(Task task);

    Subtask addSubtask(Subtask subtask);

    Subtask getSubTaskById(Integer id);

    void removeSubtask(Integer id);

    void removeAllSubtasks();

    Subtask updateSubTask(Subtask subtask);

    Epic addEpic(Epic epic);

    Epic getEpicById(Integer id);

    void removeEpic(Integer id);

    void removeAllEpics();

    Epic updateEpic(Epic epic);

    ArrayList<Subtask> getSubtasksByEpic(Integer id);

    List<Task> getHistory();

}
