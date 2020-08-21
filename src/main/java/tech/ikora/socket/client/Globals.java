package tech.ikora.socket.client;

public class Globals {
    private static String project = "NONE";
    private static String commitId = "NONE";

    public static String getProject() {
        return project;
    }

    public static void setProject(String project) {
        Globals.project = project;
    }

    public static String getCommitId() {
        return commitId;
    }

    public static void setCommitId(String commitId) {
        Globals.commitId = commitId;
    }
}
