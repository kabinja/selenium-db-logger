package tech.ikora.socket.server.model;

public class StackTrace {
    private final int id;
    private final String content;

    public StackTrace(String stackTrace) {
        this.id = stackTrace.hashCode();
        this.content = stackTrace;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
