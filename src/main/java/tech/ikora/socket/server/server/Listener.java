package tech.ikora.socket.server.server;

import tech.ikora.socket.server.Globals;
import tech.ikora.socket.server.database.Database;
import tech.ikora.socket.server.difference.StackTraceMapper;
import tech.ikora.socket.server.model.Action;
import tech.ikora.socket.server.model.StackTrace;
import tech.ikora.socket.server.model.Version;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    private final static char READ_VERSION_CODE = 'v';
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
            while (in.available() > 0){
                final char code = in.readChar();
                switch (code){
                    case READ_VERSION_CODE: System.out.println("Receive version frame"); setCommit(in); break;
                    case READ_FRAME_CODE:  System.out.println("Receive action frame"); setAction(in); break;
                    case STOP_CODE: System.out.println("Receive shutdown signal"); return false;
                    default: throw new IllegalStateException("Unexpected code: " + code);
                }
            }
        }
        catch(Exception e) {
            System.out.println(e.getClass().getCanonicalName() + ": " + e.getMessage());
        }

        return true;
    }

    private static void setCommit(DataInputStream in) throws IOException {
        final Version version = MessageParser.readVersion(in);
        Globals.setVersion(version);
        
        Database.store(version);
    }

    private static void setAction(DataInputStream in) throws IOException {
        final Action action = MessageParser.readAction(in);
        action.setCommitId(Globals.getVersion().getId());

        final String previousStackTrace = StackTraceMapper.map(
                action.getStackTrace().getContent(),
                Globals.getVersion().getDifference()
        );

        System.out.println(previousStackTrace);

        action.setPreviousStackTrace(new StackTrace(previousStackTrace));

        Database.store(action);
    }
}
