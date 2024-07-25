<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.tracker.model.Task" %>
<%@ page import="java.util.List" %>
<%
response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
if(session==null||session.getAttribute("user")==null){
	response.sendRedirect("home.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Tasks</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .action-links {
            text-decoration: none;
            padding: 4px 8px;
            border: 1px solid #ccc;
            background-color: #f1f1f1;
            color: #333;
            border-radius: 4px;
        }
        .action-links:hover {
            background-color: #ddd;
        }
    </style>
</head>
<body>
    <h1>Tasks for <%= request.getParameter("employeeName") %></h1>
    <table border="1">
        <tr>
            <th>Project</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Task Category</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        <%
            List<Task> tasks = (List<Task>) request.getAttribute("tasks");
            for (Task task : tasks) {
        %>
        <tr>
            <td><%= task.getProject() %></td>
            <td><%= task.getDate() %></td>
            <td><%= task.getStartTime() %></td>
            <td><%= task.getEndTime() %></td>
            <td><%= task.getTaskCategory() %></td>
            <td><%= task.getDescription() %></td>
            <td>
                <a href="editTask.jsp?id=<%= task.getId() %>" class="action-links">Edit</a>
                <form action="TaskServlet" method="post" >
                <input type="hidden" name="id" value="<%=task.getId() %>">
                <input type="hidden" name="employeeName" value="<%= request.getParameter("employeeName") %>">
                <input type="hidden" name="action" value="delete">
                <input type="submit" value="delete">
                </form>
                <a href="TaskServlet?action=delete&id=<%= task.getId() %>" method="post"class="action-links">Delete</a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <br>
    <a href="index.jsp">Back to Dashboard</a>
</body>
</html> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.tracker.model.Task" %>
<%@ page import="java.util.List" %>
<%
response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
if(session==null||session.getAttribute("user")==null){
    response.sendRedirect("home.jsp");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
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

        .action-links, .action-form input[type="submit"] {
            text-decoration: none;
            padding: 8px 12px;
            border: none;
            background-color: #2980b9;
            color: white;
            border-radius: 4px;
            cursor: pointer;
            margin: 4px 2px;
            display: inline-block;
        }

        .action-links:hover, .action-form input[type="submit"]:hover {
            background-color: #2c3e50;
        }

        .action-form {
            display: inline;
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
    <h1>Tasks for <%= request.getParameter("employeeName") %></h1>
    <table>
        <tr>
            <th>Project</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Task Category</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        <%
            List<Task> tasks = (List<Task>) request.getAttribute("tasks");
            if (tasks != null) {
                for (Task task : tasks) {
        %>
        <tr>
            <td><%= task.getProject() %></td>
            <td><%= task.getDate() %></td>
            <td><%= task.getStartTime() %></td>
            <td><%= task.getEndTime() %></td>
            <td><%= task.getTaskCategory() %></td>
            <td><%= task.getDescription() %></td>
            <td>
                <a href="editTask.jsp?id=<%= task.getId() %>" class="action-links">Edit</a>
                <form action="TaskServlet" method="post" class="action-form">
                    <input type="hidden" name="id" value="<%= task.getId() %>">
                    <input type="hidden" name="employeeName" value="<%= request.getParameter("employeeName") %>">
                    <input type="hidden" name="action" value="delete">
                    <input type="submit" value="Delete" class="action-links">
                </form>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="7" style="text-align: center;">No tasks available</td>
        </tr>
        <%
            }
        %>
    </table>
    <a href="index.jsp" class="back-link">Back to Dashboard</a>
</body>
</html>

