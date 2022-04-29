package com.example.kahoot.data;

import java.sql.ResultSet;

public interface DatabaseInterface {

    interface Executor extends DatabaseInterface {
        void execute(String sql, Object... args);

        ResultSet executeQuery(String sql);

        int executeUpdate(String sql, Object... args);

        class Test implements Executor {

            @Override
            public void execute(String sql, Object... args) {

            }

            @Override
            public ResultSet executeQuery(String sql) {
                return null;
            }

            @Override
            public int executeUpdate(String sql, Object... args) {
                return 0;
            }
        }

    }

    interface Cleaner {
        void clearDatabase();
    }

    interface Global extends Executor, Cleaner {
        void start(String url);

        void disconnect();
    }
}