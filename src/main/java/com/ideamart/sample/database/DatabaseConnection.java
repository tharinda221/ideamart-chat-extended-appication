package com.ideamart.sample.database;

import com.ideamart.sample.common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by tharinda on 10/20/16.
 */
public class DatabaseConnection {

    private static DatabaseConnection databaseConnection;


    protected DatabaseConnection() {
    }

    ;

    public static DatabaseConnection getDBInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    public Connection getConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        try {
            System.out.println("Connecting database...");
            connection = DriverManager.getConnection(Constants.ApplicationConstants.JDBC_URL,
                    Constants.ApplicationConstants.DATABASE_USERNAME, Constants.ApplicationConstants.DATABASE_PASSWORD);
            System.out.println("Database connected!");
            return connection;

        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }
}
