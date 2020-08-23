package tech.ikora.socket.client.database;

import tech.ikora.socket.client.model.Action;

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

            final String createStackTraceTable =
                    "create table if not exists stack_traces("
                    + "id integer primary key,"
                    + "content text not null"
                    + ");";

            statement.execute(createStackTraceTable);

            final String createDomTable =
                    "create table if not exists doms("
                    + "id integer primary key,"
                    + "content text not null"
                    + ");";

            statement.execute(createDomTable);

            final String createActionTable =
                    "create table if not exists actions("
                    + "id integer primary key,"
                    + "project text not null,"
                    + "commit_id text not null,"
                    + "stacktrace_id text not null,"
                    + "locator text not null,"
                    + "strategy text not null,"
                    + "url text not null,"
                    + "dom_id text not null,"
                    + "failure text not null"
                    + ");";

            statement.execute(createActionTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void store(Action action){
        try (Connection connection = DriverManager.getConnection(instance.url)) {
            final String insertDomSQL = "insert or ignore into doms(id,content) values(?,?)";
            final PreparedStatement insertDom = connection.prepareStatement(insertDomSQL);
            insertDom.setInt(1, action.getDom().getId());
            insertDom.setString(2, action.getDom().getContent());
            insertDom.executeUpdate();

            final String insertStackTraceSQL = "insert or ignore into stack_traces(id,content) values(?,?)";
            final PreparedStatement insertStackTrace = connection.prepareStatement(insertStackTraceSQL);
            insertStackTrace.setInt(1, action.getStackTrace().getId());
            insertStackTrace.setString(2, action.getStackTrace().getContent());
            insertStackTrace.executeUpdate();

            final String actionSQL = "insert into actions(project,commit_id,stacktrace_id,locator,strategy,url,dom_id, failure) values(?,?,?,?,?,?,?,?)";
            final PreparedStatement insertAction = connection.prepareStatement(actionSQL);
            insertAction.setString(1, action.getProject());
            insertAction.setString(2, action.getCommitId());
            insertAction.setInt(3, action.getStackTrace().getId());
            insertAction.setString(4, action.getLocator());
            insertAction.setString(5, action.getStrategy());
            insertAction.setString(6, action.getUrl());
            insertAction.setInt(7, action.getDom().getId());
            insertAction.setString(8, action.getFailure());
            insertAction.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
