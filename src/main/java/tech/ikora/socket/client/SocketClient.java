package tech.ikora.socket.client;

import org.apache.commons.cli.*;
import tech.ikora.socket.client.database.Database;
import tech.ikora.socket.client.server.Listener;

public class SocketClient {
    public static void main(String[] args){
        try {
            final Options options = new Options();
            options.addOption("p", "port", true, "port to listen to");
            options.addOption("r", "project", true, "project being executed");
            options.addOption("c", "commit", true, "git commit id of the project being analyzed");
            options.addOption("d", "database", true, "location of the sqlite database to store results");

            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse( options, args);

            if(cmd.hasOption('r')) {
                Globals.setProject(cmd.getOptionValue('r'));
            }
            else{
                throw new MissingArgumentException("Fatal error: Need to specify the name of the project being analyzed");
            }

            if(cmd.hasOption('c')){
                Globals.setCommitId(cmd.getOptionValue('c'));
            }
            else{
                throw new MissingArgumentException("Fatal error: Need to specify the commit id of the project being analyzed");
            }

            if(cmd.hasOption('d')){
                Database.initialize(cmd.getOptionValue('d'));
            }
            else{
                throw new MissingArgumentException("Fatal error: Need to specify valid path for database");
            }

            if(cmd.hasOption('p')){
                Listener.listen(Integer.parseInt(cmd.getOptionValue('p')));
            }
            else{
                throw new MissingArgumentException("Fatal error: Need to specify a port to listen to");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
