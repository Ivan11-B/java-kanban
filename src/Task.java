import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int idTask;
    private StatusTask statusTask;

    public Task(String name, String description, StatusTask statusTask) {
        this.name = name;
        this.description = description;
        this.statusTask = statusTask;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void update(String name, String description, StatusTask statusTask) {
        this.name = name;
        this.description = description;
        this.statusTask = statusTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                statusTask == task.statusTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, idTask, statusTask);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idTask=" + idTask +
                ", statusTask=" + statusTask +
                '}' + "\n";
    }
}
