package tech.ikora.socket.client.server;

import tech.ikora.socket.client.Globals;
import tech.ikora.socket.client.database.Database;
import tech.ikora.socket.client.model.Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    public static void listen(int port) throws IOException {
        final MessageParser parser = new MessageParser();

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                final Socket socket = serverSocket.accept();

                final Action action = parser.read(socket);
                action.setCommitId(Globals.getCommitId());
                action.setProject(Globals.getProject());

                Database.store(action);
            }
        }
    }
}
