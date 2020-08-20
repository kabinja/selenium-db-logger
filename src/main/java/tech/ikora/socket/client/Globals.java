package tech.ikora.socket.client;

public class Globals {
    private static String project;
    private static String commitId;

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
