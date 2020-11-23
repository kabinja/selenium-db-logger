package tech.ikora.socket.server.database;

import tech.ikora.socket.server.model.Action;
import tech.ikora.socket.server.model.Version;

import java.sql.*;

public class Database {
    public static Database instance = null;

    private final String url;

    private Database(String url) {
        this.url = url;
    }

    public static void initialize(String url) throws Exception {
        if(instance != null){
            throw new Exception("Initializing twice the singleton database");
        }

        instance = new Database("jdbc:sqlite:" + url);
        createDatabase();
    }

    private static void createDatabase(){
        try (Connection connection = DriverManager.getConnection(instance.url)) {
            Statement statement = connection.createStatement();

            final String createVersionTable =
                    "create table if not exists version("
                    + "id text primary key,"
                    + "project text not null,"
                    + "date text not null"
                    + ");";

            statement.execute(createVersionTable);

            final String createStackTraceTable =
                    "create table if not exists stack_trace("
                    + "id integer primary key,"
                    + "content text not null"
                    + ");";

            statement.execute(createStackTraceTable);

            final String createDomTable =
                    "create table if not exists dom("
                    + "id integer primary key,"
                    + "content text not null"
                    + ");";

            statement.execute(createDomTable);

            final String createActionTable =
                    "create table if not exists action("
                    + "id integer primary key,"
                    + "commit_id text not null,"
                    + "stacktrace_id text not null,"
                    + "previous_stacktrace_id text not null,"
                    + "locator text not null,"
                    + "strategy text not null,"
                    + "url text not null,"
                    + "dom_id text not null,"
                    + "window_width integer not null,"
                    + "window_height integer not null,"
                    + "failure text not null"
                    + ");";

            statement.execute(createActionTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void store(Version version){
        try(Connection connection = DriverManager.getConnection(instance.url)){
            final String insertVersionSQL = "insert or ignore into version(id, project, date) values (?,?,?)";
            final PreparedStatement insertVersion = connection.prepareStatement(insertVersionSQL);
            insertVersion.setString(1, version.getId());
            insertVersion.setString(2, version.getProject());
            insertVersion.setString(3, version.getDate());
            insertVersion.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void store(Action action){
        try (Connection connection = DriverManager.getConnection(instance.url)) {
            insertDom(connection, action.getDom().getId(), action.getDom().getContent());
            insertStackTrace(connection, action.getStackTrace().getId(), action.getStackTrace().getContent());
            insertStackTrace(connection, action.getPreviousStackTrace().getId(), action.getPreviousStackTrace().getContent());

            final String actionSQL = "insert into action(commit_id,stacktrace_id,previous_stacktrace_id,locator,strategy,url,dom_id, window_width, window_height, failure) values(?,?,?,?,?,?,?,?,?,?)";
            final PreparedStatement insertAction = connection.prepareStatement(actionSQL);
            insertAction.setString(1, action.getCommitId());
            insertAction.setInt(2, action.getStackTrace().getId());
            insertAction.setInt(3, action.getPreviousStackTrace().getId());
            insertAction.setString(4, action.getLocator());
            insertAction.setString(5, action.getStrategy());
            insertAction.setString(6, action.getUrl());
            insertAction.setInt(7, action.getDom().getId());
            insertAction.setInt(8, action.getWindowWidth());
            insertAction.setInt(9, action.getWindowHeight());
            insertAction.setString(10, action.getFailure());
            insertAction.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDom(Connection connection, int id, String content) throws SQLException {
        final String insertDomSQL = "insert or ignore into dom(id,content) values(?,?)";
        final PreparedStatement insertDom = connection.prepareStatement(insertDomSQL);
        insertDom.setInt(1, id);
        insertDom.setString(2, content);
        insertDom.executeUpdate();

    }

    private static void insertStackTrace(Connection connection, int id, String content) throws SQLException {
        final String insertStackTraceSQL = "insert or ignore into stack_trace(id,content) values(?,?)";
        final PreparedStatement insertStackTrace = connection.prepareStatement(insertStackTraceSQL);
        insertStackTrace.setInt(1, id);
        insertStackTrace.setString(2, content);
        insertStackTrace.executeUpdate();
    }
}
