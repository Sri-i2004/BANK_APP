<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<img src="ChartServlet?action=viewCharts&type=daily&employeeName=<%= request.getParameter("employeeName") %>" alt="Daily Pie Chart" />
</body>
</html> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Daily Pie Chart</title>
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

        img {
            max-width: 90%;
            height: auto;
            border: 2px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .back-link {
            display: block;
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
    <h1>Daily Pie Chart</h1>
    <img src="ChartServlet?action=viewCharts&type=daily&employeeName=<%= request.getParameter("employeeName") %>" alt="Daily Pie Chart" />
    <a href="index.jsp" class="back-link">Back to Dashboard</a>
</body>
</html>
