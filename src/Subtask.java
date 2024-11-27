public class Subtask extends Task {
    private int idEpic;

    public Subtask(String name, String description, StatusTask statusTask, int idEpic) {
        super(name, description, statusTask);
        this.idEpic = idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", idTask=" + getIdTask() +
                ", statusTask=" + getStatusTask() +
                '}' + "\n";
    }
}
