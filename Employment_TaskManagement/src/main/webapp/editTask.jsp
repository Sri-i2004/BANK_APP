
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.tracker.dao.TaskDAO" %>
<%@ page import="com.tracker.dbconnection.DBConnection" %>
<%@ page import="com.tracker.model.Task" %>
<%
    int taskId = Integer.parseInt(request.getParameter("id"));
    Connection connection = DBConnection.getConnection();
    TaskDAO taskDAO = new TaskDAO(connection);
    Task task = taskDAO.getTaskById(taskId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        form {
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            width: 100%;
            max-width: 600px;
        }

        p {
            margin: 10px 0;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }

        input[type="text"], input[type="date"], input[type="time"], textarea {
            width: 100%;
            padding: 10px;
            margin: 5px 0 15px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
            min-height: 100px;
        }

        input[type="submit"] {
            padding: 10px 20px;
            background-color: #2980b9;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #2c3e50;
        }
    </style>
</head>
<body>
    <h2>Edit Task</h2>
    <form action="TaskServlet" method="post">
        <input type="hidden" name="id" value="<%= task.getId() %>">
        <input type="hidden" name="employeeName" value="<%= request.getParameter("employeeName") %>">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="taskId" value="<%= task.getId() %>">
        
        <p>
            <label for="project">Project:</label>
            <input type="text" id="project" name="project" value="<%= task.getProject() %>">
        </p>
        <p>
            <label for="date">Date:</label>
            <input type="date" id="date" name="date" value="<%= task.getDate() %>">
        </p>
        <p>
            <label for="start_time">Start Time:</label>
            <input type="time" id="start_time" name="start_time" value="<%= String.valueOf(task.getStartTime()).substring(0,5) %>">
        </p>
        <p>
            <label for="end_time">End Time:</label>
            <input type="time" id="end_time" name="end_time" value="<%= String.valueOf(task.getEndTime()).substring(0,5) %>">
        </p>
        <p>
            <label for="task_category">Task Category:</label>
            <input type="text" id="task_category" name="task_category" value="<%= task.getTaskCategory() %>">
        </p>
        <p>
            <label for="description">Description:</label><br>
            <textarea id="description" name="description"><%= task.getDescription() %></textarea>
        </p>
        <p>
            <input type="submit" value="Update Task">
        </p>
    </form>
</body>
</html>
 