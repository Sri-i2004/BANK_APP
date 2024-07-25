<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<img src="ChartServlet?action=viewCharts&type=weekly&employeeName=<%= request.getParameter("employeeName") %>" alt="Weekly Bar Chart" /> 
</body>
</html> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Weekly Chart</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #eaeaea;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            color: #333;
        }

        .chart-wrapper {
            background-color: #fff;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            border-radius: 8px;
            max-width: 80%;
            width: 700px;
            text-align: center;
            margin: 20px;
        }

        h1 {
            font-size: 24px;
            color: #3498db;
            margin-bottom: 20px;
        }

        img {
            width: 100%;
            height: auto;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #3498db;
            border: none;
            border-radius: 5px;
            text-decoration: none;
        }

        .back-link:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <div class="chart-wrapper">
        <h1>Weekly Bar Chart</h1>
        <img src="ChartServlet?action=viewCharts&type=weekly&employeeName=<%= request.getParameter("employeeName") %>" alt="Weekly Bar Chart" />
        <a href="index.jsp" class="back-link">Back to Dashboard</a>
    </div>
</body>
</html>
