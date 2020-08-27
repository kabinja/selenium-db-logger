package tech.ikora.socket.server.model;

public class Dom {
    private final int id;
    private final String content;

    public Dom(String dom) {
        this.id = dom.hashCode();
        this.content = dom;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
