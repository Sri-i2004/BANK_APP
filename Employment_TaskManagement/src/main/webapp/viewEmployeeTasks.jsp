<%-- <%@ page import="java.util.List" %>
<%@ page import="com.tracker.model.Task" %>
<%@ page import="com.tracker.model.User" %>
<%@ page import="com.tracker.dao.TaskDAO" %>
<%@ page import="com.tracker.dbconnection.DBConnection" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRoleId() != 2) { // Assuming 2 is the Admin role
        response.sendRedirect("adminLogin.jsp");
        return;
    }

    String employeeName = request.getParameter("employeeName");
    TaskDAO taskDAO = new TaskDAO(DBConnection.getConnection());
    List<Task> tasks = taskDAO.getTasksByEmployeeName(employeeName);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Tasks</title>
</head>
<body>
    <h2>Tasks for <%= employeeName %></h2>
    <table border="1">
        <tr>
            <th>Employee Name</th>
            <th>Role</th>
            <th>Project</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Task Category</th>
            <th>Description</th>
        </tr>
        <%
            for (Task task : tasks) {
        %>
        <tr>
            <td><%= task.getEmployeeName() %></td>
            <td><%= task.getRole() %></td>
            <td><%= task.getProject() %></td>
            <td><%= task.getDate() %></td>
            <td><%= task.getStartTime() %></td>
            <td><%= task.getEndTime() %></td>
            <td><%= task.getTaskCategory() %></td>
            <td><%= task.getDescription() %></td>
        </tr>
        <%
            }
        %>
    </table>
    <a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html> --%>
<%@ page import="java.util.List" %>
<%@ page import="com.tracker.model.Task" %>
<%@ page import="com.tracker.model.User" %>
<%@ page import="com.tracker.dao.TaskDAO" %>
<%@ page import="com.tracker.dbconnection.DBConnection" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRoleId() != 2) { // Assuming 2 is the Admin role
        response.sendRedirect("adminLogin.jsp");
        return;
    }

    String employeeName = request.getParameter("employeeName");
    TaskDAO taskDAO = new TaskDAO(DBConnection.getConnection());
    List<Task> tasks = taskDAO.getTasksByEmployeeName(employeeName);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h2 {
            color: #2c3e50;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #2980b9;
            font-weight: bold;
        }

        .back-link:hover {
            color: #2c3e50;
        }
    </style>
</head>
<body>
    <h2>Tasks for <%= employeeName %></h2>
    <table>
        <tr>
            <th>Employee Name</th>
            <th>Role</th>
            <th>Project</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Task Category</th>
            <th>Description</th>
        </tr>
        <%
            for (Task task : tasks) {
        %>
        <tr>
            <td><%= task.getEmployeeName() %></td>
            <td><%= task.getRole() %></td>
            <td><%= task.getProject() %></td>
            <td><%= task.getDate() %></td>
            <td><%= task.getStartTime() %></td>
            <td><%= task.getEndTime() %></td>
            <td><%= task.getTaskCategory() %></td>
            <td><%= task.getDescription() %></td>
        </tr>
        <%
            }
        %>
    </table>
    <a href="adminDashboard.jsp" class="back-link">Back to Dashboard</a>
</body>
</html>

