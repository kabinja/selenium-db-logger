package tech.ikora.socket.server.model;

public class Version {
    private String id;
    private String project;
    private String date;
    private String difference;

    public void setId(String id) {
        this.id = id;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getDate() {
        return date;
    }

    public String getDifference() {
        return difference;
    }

    @Override
    public String toString() {
        return String.format("Version[id=%s; project=%s; date=%s; difference=%s",
                id,
                project,
                date,
                difference.isBlank() ? "no difference" : difference.substring(0, 8) + "..."
        );
    }
}
