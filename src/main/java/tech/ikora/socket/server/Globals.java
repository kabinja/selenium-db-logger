package tech.ikora.socket.server;

import tech.ikora.socket.server.model.Version;

public class Globals {
    private static Version version;

    public static void setVersion(Version version) {
        Globals.version = version;
    }

    public static Version getVersion() {
        return Globals.version;
    }
}
