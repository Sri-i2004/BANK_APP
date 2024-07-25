
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Task Charts</title>
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
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        form {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fafafa;
        }

        form h2 {
            margin: 0 0 10px;
            color: #333;
        }

        input[type="submit"] {
            padding: 10px 20px;
            background-color: #2980b9;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #3498db;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            text-decoration: none;
            background-color: #2c3e50;
            color: white;
            border-radius: 4px;
            font-size: 16px;
        }

        a:hover {
            background-color: #34495e;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Task Charts for <%= request.getParameter("employeeName") %></h1>
        <form action="daily.jsp">
            <h2>See where you have spent most of your time today</h2>
            <input type="hidden" name="action" value="viewCharts">
            <input type="hidden" name="type" value="daily">
            <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
            <input type="submit" value="Daily Task">
        </form>
   
        <form action="weekly.jsp">
            <h2>See where you have spent most of your time this week</h2>
            <input type="hidden" name="action" value="viewCharts">
            <input type="hidden" name="type" value="weekly">
            <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
            <input type="submit" value="Weekly Task">
        </form>
        
        <form action="monthly.jsp">
            <h2>See where you have spent most of your time this month</h2>
            <input type="hidden" name="action" value="viewCharts">
            <input type="hidden" name="type" value="monthly">
            <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
            <input type="submit" value="Monthly Task">
        </form>
    
        <a href="index.jsp">Back</a>
    </div>
</body>
</html>

<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Task Charts</title>
</head>
<body>
    <h1>Task Charts for <%= request.getParameter("employeeName") %></h1>
    <form action="daily.jsp">
    <h2>To see Where you have spent your most of the time Today
    </h2>
    <input type="hidden" name="action" value="viewCharts">
    <input type="hidden" name="type" value="daily">
    <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
    <input type="submit" value="DailyTask">
    </form>
   
    <form action="weekly.jsp">
    <h2>To see Where you have spent your most of the time in this Week Click below
    </h2>
    <input type="hidden" name="action" value="viewCharts">
    <input type="hidden" name="type" value="weekly">
    <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
    <input type="submit" value="WeeklyTask">
    </form>
    <form action="monthly.jsp">
    <h2>To see Where you have spent your most of the time in this Month Click below
    </h2>
    <input type="hidden" name="action" value="viewCharts">
    <input type="hidden" name="type" value="monthly">
    <input type="hidden" name="employeeName" value="<%=request.getParameter("employeeName") %>">
    <input type="submit" value="MonthlyTask">
    </form>
    
    
    

    <a href="index.jsp">Back</a>
</body>
</html>

</body>
</html> --%>