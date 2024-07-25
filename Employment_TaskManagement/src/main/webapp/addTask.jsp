<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tracker.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
            color: #2c3e50;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-bottom: 5px;
            color: #333;
        }

        input[type="text"], input[type="date"], input[type="time"], textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            padding: 10px 20px;
            background-color: #2980b9;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover {
            background-color: #2c3e50;
        }

        .error {
            color: red;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Add Task</h1>
        <span class="error">
            <% if (request.getAttribute("error") != null) { %>
                <%= request.getAttribute("error") %>
            <% } %>
        </span>
        <form action="TaskServlet" method="post">
            <label for="project">Project:</label>
            <input type="text" id="project" name="project" required>
            
            <label for="date">Date:</label>
            <input type="date" id="date" name="date" required>
            
            <label for="start_time">Start Time:</label>
            <input type="time" id="start_time" name="start_time" required>
            
            <label for="end_time">End Time:</label>
            <input type="time" id="end_time" name="end_time" required>
            
            <label for="task_category">Task Category:</label>
            <input type="text" id="task_category" name="task_category" required>
            
            <label for="description">Description:</label>
            <textarea id="description" name="description" required></textarea>
            
            <input type="hidden" name="action" value="add">
            <button type="submit">Add Task</button>
        </form>
    </div>
</body>
</html>

<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.tracker.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Task</title>
</head>
<body>
    <h1>Add Task</h1>
    <br>
        <span style="color: red;">
            <% if (request.getAttribute("error") != null) { %>
                <%= request.getAttribute("error") %>
            <% } %>
        </span>
        <br>
    <form action="TaskServlet" method="post">
        Project: <input type="text" name="project" required><br>
        Date: <input type="date" name="date" required><br>
        Start Time: <input type="time" name="start_time" required><br>
        End Time: <input type="time" name="end_time" required><br>
        Task Category: <input type="text" name="task_category" required><br>
        Description: <textarea name="description" required></textarea><br>
        <input type="hidden" name="action" value="add">
        <button type="submit">Add Task</button>
    </form>
</body>
</html>
 --%>