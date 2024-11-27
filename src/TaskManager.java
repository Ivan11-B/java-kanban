import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> allTasks = new HashMap<>();
    HashMap<Integer, Task> listTasks = new HashMap<>();
    HashMap<Integer, Subtask> listSubTasks = new HashMap<>();
    HashMap<Integer, Epic> listEpics = new HashMap<>();

    int idCount = 1;

    void addTask(Task task) {
        boolean isTasks = true;
        for (Task task1: listTasks.values()){
            if (task.equals(task1)){
                System.out.println("Такая задача уже существует");
                isTasks = false;
                break;
            }
        }
        if (isTasks) {
            task.setIdTask(idCount);
            listTasks.put(idCount, task);
            allTasks.put(idCount, task);
            System.out.println("Добавлена задача: " + idCount);
            idCount++;
        }
    }

    Task setTask(int idTask) {
        return listTasks.get(idTask);
    }

    void removeTask(int idTask) {
        if (listTasks.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            for (Task task : listTasks.values()) {
                if (task.getIdTask() == idTask) {
                    listTasks.remove(idTask);
                    allTasks.remove(idTask);
                    System.out.println("Задача удалена!");
                    break;
                }
            }
        }
    }

    void removeAllTasks() {
        if (listTasks.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            listTasks.clear();
            allTasks.clear();
            System.out.println("Все задачи удалены!");
        }
    }

    public void updateTask(Task task, StatusTask statusTask) {
        task.setStatusTask(statusTask);
        listTasks.put(task.getIdTask(), task);
    }

    void addSubtask(Subtask subtask) {
        boolean isSubtasks = true;
        for (Subtask subtask1 : listSubTasks.values()) {
            if (subtask.equals(subtask1)) {
                System.out.println("Такая подзадача уже существует");
                isSubtasks = false;
                break;
            }
        }
        if (isSubtasks) {
            subtask.setIdTask(idCount);
            subtask.setIdEpic(subtask.getIdEpic());
            listSubTasks.put(idCount, subtask);
            allTasks.put(idCount, subtask);
            listEpics.get(subtask.getIdEpic()).addSubtask(subtask);
            System.out.println("Добавлена подзадача: " + idCount);
            idCount++;
        }
    }

    Subtask setSubTask(int idTask) {
        return listSubTasks.get(idTask);
    }

    void removeSubTask(int idTask) {
        if (listSubTasks.isEmpty()) {
            System.out.println("Список подзадач пуст");
        } else {
            for (Subtask subtask : listSubTasks.values()) {
                if (subtask.getIdTask() == idTask) {
                    listSubTasks.remove(idTask);
                    allTasks.remove(idTask);
                    listEpics.get(subtask.getIdEpic()).removeSubTask(subtask);
                    System.out.println("Подзадача удалена!");
                    break;
                }
            }
        }
    }

    void removeAllSubTasks() {
        if (listSubTasks.isEmpty()) {
            System.out.println("Список подзадач пуст");
        } else {
            listSubTasks.clear();
            allTasks.clear();
            System.out.println("Все подзадачи удалены!");
        }
    }

    public void updateSubTask(Subtask subTask, StatusTask statusTask) {
        subTask.setStatusTask(statusTask);
        listSubTasks.put(subTask.getIdTask(), subTask);
        Epic epic = listEpics.get(subTask.getIdEpic());
        if (statusTask == StatusTask.IN_PROGRESS || statusTask == StatusTask.DONE) {
            epic.setStatusTask(StatusTask.IN_PROGRESS);
        }
        int count = 0;
        for (Subtask subTask1 : epic.getListSubTasks()) {
            if (subTask1.getStatusTask() == statusTask) {
                count++;
            }
        }
        if (count == epic.getListSubTasks().size()) {
            epic.setStatusTask(statusTask);
        }

    }

    void addEpic(Epic epic) {
        boolean isEpics = true;
        for (Epic epic1 : listEpics.values()) {
            if (epic.equals(epic1)) {
                System.out.println("Такой эпик уже существует");
                isEpics = false;
                break;
            }
        }
        if (isEpics) {
            epic.setIdTask(idCount);
            listEpics.put(idCount, epic);
            allTasks.put(idCount, epic);
            System.out.println("Добавлен эпик: " + idCount);
            idCount++;
        }
    }

    Epic setEpic(int idTask) {
        return listEpics.get(idTask);
    }

    void removeEpic(int idTask) {
        if (listEpics.isEmpty()) {
            System.out.println("Список эпиков пуст");
        } else {
            for (Epic epic : listEpics.values()) {
                if (epic.getIdTask() == idTask) {
                    listEpics.remove(idTask);
                    allTasks.remove(idTask);
                    for (Subtask subtask : epic.getListSubTasks()) {
                        allTasks.remove(subtask.getIdTask());
                        listSubTasks.remove(subtask.getIdTask());
                    }
                    System.out.println("Эпик удален!");
                    break;
                }
            }
        }
    }

    void removeAllEpics() {
        if (listEpics.isEmpty()) {
            System.out.println("Список эпиков пуст");
        } else {
            listEpics.clear();
            allTasks.clear();
            System.out.println("Все эпики удалены!");
        }
    }

    public String printTasks() {
        if (listTasks.isEmpty()) {
            return "Список задач пуст!";
        } else {
            return "Задачи: " + listTasks.values().toString();
        }
    }

    public String printSubTask() {
        if (listSubTasks.isEmpty()) {
            return "Список подзадач пуст!";
        } else {
            return "Подзадачи: " + listSubTasks.values().toString();
        }
    }
    public String printEpic() {
        if (listEpics.isEmpty()) {
            return "Список эпиков пуст!";
        } else {
            return "Эпики: " + listEpics.values().toString();
        }
    }

    public String printAllTasks() {
        return allTasks.toString();
    }

    public String printSubTaskToEpic(Epic epic) {
        return epic.getListSubTasks().toString();

    }












}
