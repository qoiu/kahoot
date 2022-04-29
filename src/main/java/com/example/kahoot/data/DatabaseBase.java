package com.example.kahoot.data;

import java.sql.*;

public class DatabaseBase implements DatabaseInterface.Global {

    private static Connection connection;

    public DatabaseBase(String url) {
        start(url);
    }

    public void execute(String sql, Object... args) {
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            Statement state = connection.createStatement();
            state.setQueryTimeout(30);
            return state.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String sql, Object... args) {
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            int id = -1;
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
                id = rs.getInt(1);
            statement.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private PreparedStatement statementWithArgs(String sql, Object... args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        if (args.length != 0) {
            int count = 1;
            for (Object arg : args) {
                if (arg instanceof Integer) {
                    statement.setInt(count, (Integer) arg);
                    count++;
                }
                if (arg instanceof String) {
                    statement.setString(count, (String) arg);
                    count++;
                }
                if (arg instanceof Long) {
                    statement.setLong(count, (Long) arg);
                    count++;
                }
            }
        }
        return statement;
    }

    public void start(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void clearDatabase() {
        clearTable("userInGame");
        clearTable("game");
        clearTable("users");
        clearTable("lostMessages");
    }

    private void clearTable(String str) {
        execute("DELETE FROM " + str);
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}