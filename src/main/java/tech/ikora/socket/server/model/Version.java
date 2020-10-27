package tech.ikora.socket.server.model;

public class Version {
    private String id;
    private String project;
    private String date;

    public void setId(String id) {
        this.id = id;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setDate(String date) {
        this.date = date;
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
}
