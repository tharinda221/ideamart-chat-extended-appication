<%--
  Created by IntelliJ IDEA.
  User: tharinda
  Date: 12/28/16
  Time: 12:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ideamart.sample.usermgt.UserDAO" %>
<%@ page import="com.ideamart.sample.dashboardMgt.DashboardDAO" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <style type="text/css">
        td, th {
            width: 4rem;
            height: 2rem;
            border: 1px solid #ccc;
            text-align: center;
        }

        th {
            background: lightblue;
            border-color: white;
        }

        body {
            padding: 1rem;
        }
    </style>
    <title>eeChat dashboard</title>
</head>
<body>
<h2>eeChat</h2><br>
<%
    UserDAO userDAO = new UserDAO();
    DashboardDAO dashboardDAO = new DashboardDAO();
    int total = userDAO.getTotalUsers();
    int [] array = userDAO.getTotalSubscribers();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date dateObj = new Date();
    String date = dateFormat.format(dateObj);
    int [] dailyTrafficArray = dashboardDAO.getDailyTraffic(date);
    int reg = array[0];
    int unReg = array[1];
    int pending = array[2];
    int regToday = dailyTrafficArray[0];
    int unRegToday = dailyTrafficArray[1];
    int pendingToday = dailyTrafficArray[2];

%>
<table>
    <tr>
        <th>Application Name</th>
        <th>Date</th>
        <th>Total Users</th>
        <th>Subscribed Users(Net Reg Base)</th>
        <th>UnSubscribed Users</th>
        <th>Pending Users</th>
        <th>Today Reg Users</th>
        <th>Today unReg Users</th>
        <th>Today Pending Users</th>

    </tr>
    <tr>
        <td>eeChat</td>
        <td><%=date%>
        <td><%=total%>
        <td><%=reg%></td>
        <td><%=unReg%></td>
        <td><%=pending%></td>
        <td><%=regToday%></td>
        <td><%=unRegToday%></td>
        <td><%=pendingToday%></td>
    </tr>

</table>
</body>
</html>
