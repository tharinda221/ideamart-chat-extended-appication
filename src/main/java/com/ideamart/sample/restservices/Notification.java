package com.ideamart.sample.restservices;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ideamart.sample.common.Constants;
import com.ideamart.sample.dashboardMgt.Dashboard;
import com.ideamart.sample.dashboardMgt.DashboardDAO;
import com.ideamart.sample.sms.send.SendMessage;
import com.ideamart.sample.usermgt.User;
import com.ideamart.sample.usermgt.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tharinda on 1/3/17.
 */
public class Notification extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Notification called");
        String result = null;
        try {
            result = httpServletRequestToString(request);
            System.out.println("notification status:");
            System.out.println(result);
            JsonElement jelement = new JsonParser().parse(result);
            JsonObject jobject = jelement.getAsJsonObject();
            String subscriptionStatus = String.valueOf(jobject.get("status")).replaceAll("['\"]", "");
            System.out.println("user status: " + subscriptionStatus);
            String address = "tel:" + String.valueOf(jobject.get("subscriberId")).replaceAll("['\"]", "");
            UserDAO userDAO = new UserDAO();
            if (!userDAO.userAvailability(address)) {
                User user = new User(address, null, "1", "notify", 1, 2);
                userDAO.AddUser(user);
            }
            DashboardTrafficUpdate(address, subscriptionStatus);
            DashboardDailyTrafficUpdate(subscriptionStatus);
        } catch (Exception e) {
            System.out.println(e.hashCode());
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    String httpServletRequestToString(HttpServletRequest request) throws Exception {

        ServletInputStream mServletInputStream = request.getInputStream();
        byte[] httpInData = new byte[request.getContentLength()];
        int retVal = -1;
        StringBuilder stringBuilder = new StringBuilder();

        while ((retVal = mServletInputStream.read(httpInData)) != -1) {
            for (int i = 0; i < retVal; i++) {
                stringBuilder.append(Character.toString((char) httpInData[i]));
            }
        }

        return stringBuilder.toString();
    }

    public void DashboardTrafficUpdate(String address, String status) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        if(status.equals("REGISTERED")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.SendMessage(Constants.MessageConstants.HELP_MENU, Constants.ApplicationConstants.APP_ID,
                    address, Constants.ApplicationConstants.PASSWORD, Constants.ApplicationConstants.SMS_URL);
            userDAO.updateUserStatus(address, 1);
        } else if(status.equals("UNREGISTERED")) {
            userDAO.updateUserStatus(address, 0);
        } else {
            userDAO.updateUserStatus(address, 2);
        }
    }

    public void DashboardDailyTrafficUpdate(String status) throws ClassNotFoundException, SQLException {
        DashboardDAO dashboardDAO = new DashboardDAO();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dateObj = new Date();
        String date = dateFormat.format(dateObj);
        if(dashboardDAO.dateAvailable(date)) {
            int reg = dashboardDAO.getRegCount(date);
            int unReg = dashboardDAO.getUnRegCount(date);
            int pending = dashboardDAO.getPendingCount(date);
            if(status.equals("REGISTERED")) {
                Dashboard dasboardObj = new Dashboard();
                dasboardObj.setDate(date);
                dasboardObj.setReg(reg+1);
                dasboardObj.setUnReg(unReg);
                dasboardObj.setPending(pending);
                dashboardDAO.updateParams(dasboardObj);
            } else if(status.equals("UNREGISTERED")) {
                Dashboard dasboardObj = new Dashboard();
                dasboardObj.setDate(date);
                dasboardObj.setReg(reg);
                dasboardObj.setUnReg(unReg+1);
                dasboardObj.setPending(pending);
                dashboardDAO.updateParams(dasboardObj);
            } else {
                Dashboard dasboardObj = new Dashboard();
                dasboardObj.setDate(date);
                dasboardObj.setReg(reg);
                dasboardObj.setUnReg(unReg);
                dasboardObj.setPending(pending+1);
                dashboardDAO.updateParams(dasboardObj);
            }
        } else {
            dashboardDAO.deteleTable();
            Dashboard dasboardObj = new Dashboard();
            dasboardObj.setDate(date);
            dasboardObj.setReg(1);
            dasboardObj.setUnReg(0);
            dasboardObj.setPending(0);
            dashboardDAO.AddDashboard(dasboardObj);
        }

    }
}
