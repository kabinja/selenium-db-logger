package tech.ikora.socket.client.server;

import tech.ikora.socket.client.Globals;
import tech.ikora.socket.client.database.Database;
import tech.ikora.socket.client.model.Action;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    private final static char READ_PROJECT_CODE = 'p';
    private final static char READ_COMMIT_CODE = 'c';
    private final static char READ_FRAME_CODE = 'f';
    private final static char STOP_CODE = 'e';

    public static void listen(int port) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                final Socket socket = serverSocket.accept();
                if(!processMessage(socket)){
                    break;
                }
            }
        }
    }

    private static boolean processMessage(Socket socket){
        try(DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))){
            switch (in.readChar()){
                case READ_PROJECT_CODE: System.out.println("Receive project name"); setProject(in); break;
                case READ_COMMIT_CODE: System.out.println("Receive commit id"); setCommit(in); break;
                case READ_FRAME_CODE:  System.out.println("Receive action frame"); setAction(in); break;
                case STOP_CODE: System.out.println("Receive shutdown signal"); return false;
                default: throw new IllegalStateException("Unexpected code: " + in.readChar());
            }
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    private static void setProject(DataInputStream in) throws IOException {
        final String project = MessageParser.readBlock(in);
        Globals.setProject(project);
    }

    private static void setCommit(DataInputStream in) throws IOException {
        final String commit = MessageParser.readBlock(in);
        Globals.setCommitId(commit);
    }

    private static void setAction(DataInputStream in) throws IOException {
        final Action action = MessageParser.readAction(in);

        action.setCommitId(Globals.getCommitId());
        action.setProject(Globals.getProject());

        Database.store(action);
    }
}
