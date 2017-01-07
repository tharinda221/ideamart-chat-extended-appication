package com.ideamart.sample.dashboardMgt;

import com.ideamart.sample.common.Constants;
import com.ideamart.sample.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by tharinda on 12/28/16.
 */
public class DashboardDAO {

    private String dashboardName = Constants.ApplicationConstants.DATABASE_DASHBOARD_TABLE_NAME;

    private Connection connection;
    private Statement stmt;

    public void AddDashboard(Dashboard dashboard) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "INSERT INTO "+ dashboardName +" VALUES (" + "\"" + dashboard.getDate() + "\"" + "," + "\"" + dashboard.getReg() + "\"" +
                "," + "\"" + dashboard.getUnReg() + "\"" + "," + "\"" + String.valueOf(dashboard.getPending()) + "\"" + ");";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        connection.close();


    }

    public void updateParams(Dashboard dashboard) throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "UPDATE "+ dashboardName +" SET reg=" + "\"" + dashboard.getReg() + "\"" + " WHERE date= " + "\"" + dashboard.getDate() + "\"" + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);

        String sql2 = "UPDATE "+ dashboardName +" SET unReg=" + "\"" + dashboard.getUnReg() + "\"" + " WHERE date= " + "\"" + dashboard.getDate() + "\"" + ";";
        System.out.println(sql2);
        stmt.executeUpdate(sql2);

        String sql3 = "UPDATE "+ dashboardName +" SET pending=" + "\"" + dashboard.getPending() + "\"" + " WHERE date= " + "\"" + dashboard.getDate() + "\"" + ";";
        System.out.println(sql3);
        stmt.executeUpdate(sql3);

        connection.close();
    }

    public void deteleTable() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getDBInstance().getConnection();
        stmt = connection.createStatement();
        String sql = "DELETE FROM "+ dashboardName +";";
        System.out.println(sql);
        stmt.executeUpdate(sql);

        connection.close();

    }

    public boolean dateAvailable(String date) {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from "+ dashboardName +" where date =" + "\"" + date + "\"" + ";";
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

    public int getRegCount(String date) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from "+ dashboardName +" where date= " + "\"" + date + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("reg");
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

    public int getUnRegCount(String date) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from "+ dashboardName +" where date= " + "\"" + date + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("unReg");
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

    public int getPendingCount(String date) throws ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getDBInstance().getConnection();
            stmt = connection.createStatement();
            String query = "Select * from "+ dashboardName +" where date= " + "\"" + date + "\"" + ";";
            System.out.println(query);
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                return resultSet.getInt("pending");
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

    public int[] getDailyTraffic(String date) {
        int[] array = new int[3];
        try {
            if (dateAvailable(date)) {
                array[0] = getRegCount(date);
                array[1] = getUnRegCount(date);
                array[2] = getPendingCount(date);
            } else {
                deteleTable();
                Dashboard dasboardObj = new Dashboard();
                dasboardObj.setDate(date);
                dasboardObj.setReg(0);
                dasboardObj.setUnReg(0);
                dasboardObj.setPending(0);
                AddDashboard(dasboardObj);
                for (int i = 0; i < 3; i++) {
                    array[i] = 0;
                }
            }
            return array;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return array;
    }
}
