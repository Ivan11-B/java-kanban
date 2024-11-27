import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksToEpic;

    public Epic(String name, String description) {
        super(name, description, StatusTask.NEW);
        this.subtasksToEpic = new ArrayList<>();
    }

    public ArrayList<Subtask> getListSubTasks() {
        return subtasksToEpic;
    }

    public void addSubtask(Subtask subTask) {
        if (!subtasksToEpic.contains(subTask)) {
            subtasksToEpic.add(subTask);
        } else {
            System.out.println("Задача уже существует!");
        }
    }
    public void removeSubTask(Subtask subTask) {
        if (subtasksToEpic == null) {
            System.out.println("Список подзадач пуст");
        } else {
            if (subtasksToEpic.contains(subTask)) {
                subtasksToEpic.remove(subTask);
            } else {
                System.out.println("Задача не найдена!");
            }
        }
    }
    public void removeAllSubTasks() {
        if (subtasksToEpic == null) {
            System.out.println("Список подзадач пуст");
        } else {
            subtasksToEpic.clear();
        }
    }
    public Subtask getSubTask(int idTask) {
        Subtask subTask = null;
        for (int i = 0; i < subtasksToEpic.size(); i++) {
            if (subtasksToEpic.get(i).getIdTask() == idTask) {
                subTask = subtasksToEpic.get(i);
                break;
            }
        }
        return subTask;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", idTask=" + getIdTask() +
                ", statusTask=" + getStatusTask() + "," +
                "\n" +
                "subtasks=" + subtasksToEpic +
                "\n"+
                '}';
    }
}
