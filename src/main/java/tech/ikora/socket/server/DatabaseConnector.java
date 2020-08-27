package tech.ikora.socket.server;

import org.apache.commons.cli.*;
import tech.ikora.socket.server.database.Database;
import tech.ikora.socket.server.server.Listener;

public class DatabaseConnector {
    public static void main(String[] args){
        try {
            final Options options = new Options();
            options.addOption("p", "port", true, "port to listen to");
            options.addOption("d", "database", true, "location of the sqlite database to store results");

            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse( options, args);

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
