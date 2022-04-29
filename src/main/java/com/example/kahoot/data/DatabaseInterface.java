package com.example.kahoot.data;

import java.sql.ResultSet;

public interface DatabaseInterface {

    interface Executor extends DatabaseInterface {
        void execute(String sql, Object... args);

        ResultSet executeQuery(String sql);

        int executeUpdate(String sql, Object... args);

    }

    interface Cleaner {
        void clearDatabase();
    }

    interface Global extends Executor, Cleaner {
        void start(String url);

        void disconnect();
    }
}