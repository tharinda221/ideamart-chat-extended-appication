package com.ideamart.sample.usermgt;

import com.ideamart.sample.common.Constants;
import com.ideamart.sample.database.DatabaseConnection;
import com.ideamart.sample.subcription.Subscription;

import java.io.IOException;
import java.sql.*;

/**
 * Created by tharinda on 10/20/16.
 */

public class UserDAO {

    private String tableName = Constants.ApplicationConstants.DATABASE_TRAFFIC_TABLE_NAME;
    private String tableUserName = Constants.ApplicationConstants.DATABASE_USER_TABLE_NAME;

    private Connection connection;
    private Statement stmt;

    public void AddUser(User user) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "INSERT INTO " + tableName + " VALUES (" + "\"" + user.getAddress() + "\"" + "," + "\"" + user.getMessage() + "\"" +
                "," + "\"" + user.getFlow() + "\"" + "," + "\"" + String.valueOf(user.getSubscription()) +
                "\"" + "," + "\"" + String.valueOf(user.getStatus()) + "\"" + ");";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();

    }

    public void updateCount(String address) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE " + tableName + " SET subcription= subcription + 1" + " WHERE address= " + "\"" + address + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();
    }

    public int getCount(String address) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from " + tableName + " where address= " + "\"" + address + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                connection.close();
                return resultSet.getInt("subcription");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return 0;
    }

    public boolean userAvailability(String address) {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from " + tableName + " where address =" + "\"" + address + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return false;

    }

    public boolean RegisterUserName(String address, String name) throws ClassNotFoundException {

        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String sql = "INSERT INTO " + tableUserName + " VALUES (" + "\"" + address + "\"" + "," + "\"" + "" + "\""
                    + "," + "\"" + name + "\"" + "," + "\"" + 0 + "\"" + "," + "\"" + "" + "\"" + ");";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    public void updateUserUseName(String address, String userName) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE " + tableUserName + " SET username= userName" + " WHERE address= " + "\"" + address + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();
    }

    public void updateUserAge(String address, String userName) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE " + tableUserName + " SET age = age" + " WHERE address= " + "\"" + address + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();
    }

    public void updateUserSex(String address, String userName) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE " + tableUserName + " SET sex = sex" + " WHERE address= " + "\"" + address + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();
    }

    public String getUserAddressByName(String name) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from " + tableUserName + " where name= " + "\"" + name + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getString("address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return null;
    }

    public String getUserNameByAddress(String address) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from " + tableUserName + " where address= " + "\"" + address + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "null";
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return "null";
    }

    public void updateUserStatus(String address, int status) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE " + tableName + " SET status = " + status + "" + " WHERE address= " + "\"" + address + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();
    }

    public int getUserStatus(String address) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from " + tableName + " where address= " + "\"" + address + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return 0;
    }

    public int getTotalUsers() {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select COUNT(*) AS total FROM " + tableName + "";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return 0;
    }

    public int getPendingUsers() {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select COUNT(*) AS total FROM " + tableName + " WHERE subcription=" + "1";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return 0;
    }

    public int[] getTotalSubscribers() {
        ResultSet resultSet = null;
        String address;
        int reg = 0, unReg = 0, pending = 0;
        int[] array = new int[3];
        Subscription subscription = new Subscription();
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select address from " + tableName + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                address = resultSet.getString("address");
                if (getUserStatus(address) == 1) {
                    reg++;

                } else if (getUserStatus(address) == 0) {
                    unReg++;
                } else {
                    pending++;
                }
            }
            array[0] = reg;
            array[1] = unReg;
            array[2] = pending;
            return array;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return array;
    }

}
