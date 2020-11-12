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
}
