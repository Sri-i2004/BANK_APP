<%@ page import="java.util.List" %>
<%@ page import="com.tracker.model.User" %>
<%@ page import="com.tracker.dao.UserDAO" %>
<%@ page import="com.tracker.dbconnection.DBConnection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRoleId() != 2) { // Assuming 2 is the Admin role
        response.sendRedirect("adminLogin.jsp");
        return;
    }

    UserDAO userDAO = new UserDAO(DBConnection.getConnection());
    List<User> employees = userDAO.getAllEmployees();
%>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h2, h3 {
            color: #333;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        a {
            text-decoration: none;
            color: #4CAF50;
        }

        a:hover {
            text-decoration: underline;
        }

        form {
            margin-top: 20px;
            text-align: center;
        }

        label {
            display: block;
            margin: 10px 0 5px;
            font-weight: bold;
        }

        select, input[type="submit"] {
            padding: 10px;
            margin: 5px 0;
            width: 100%;
            max-width: 300px;
            display: block;
            margin-left: auto;
            margin-right: auto;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #c0392b;
        }

        input[type="submit"].logout {
            background-color: #3498db;
        }

        input[type="submit"].logout:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<%
response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
if(session==null||session.getAttribute("user")==null){
    response.sendRedirect("home.jsp");
    return;
}
%>
    <h2>Welcome, Admin</h2>
    <h3>All Employees</h3>
    <table border="1">
        <tr>
            <th>Employee Name</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        <%
            for (User employee : employees) {
        %>
        <tr>
            <td><%= employee.getUsername() %></td>
            <td><%= employee.getRoleId() == 1 ? "Associate" : "Admin" %></td>
            <td><a href="viewEmployeeTasks.jsp?employeeName=<%= employee.getUsername() %>">View Tasks</a></td>
        </tr>
        <%
            }
        %>
    </table>
    <h2>Generate Chart</h2>
    <form action="ChartServlet1" method="post">
        <label for="project">Select Project:</label>
        <select name="project">
            <% 
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    conn = DBConnection.getConnection();
                    String query = "SELECT DISTINCT project FROM tasks";
                    ps = conn.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String project = rs.getString("project");
            %>
                        <option value="<%= project %>"><%= project %></option>
            <% 
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
                }
            %>
        </select>
        <br>
        <label for="employee">Select Employee:</label>
        <select name="employee">
            <% 
                try {
                    conn = DBConnection.getConnection();
                    String query = "SELECT DISTINCT employee_name FROM tasks";
                    ps = conn.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String employee = rs.getString("employee_name");
            %>
                        <option value="<%= employee %>"><%= employee %></option>
            <% 
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
                }
            %>
        </select>
        <br>
        <label for="timeframe">Select Timeframe:</label>
        <select name="timeframe">
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
        </select>
        <br>
        <input type="submit" value="Generate Chart">
    </form>
    <form action="logout.jsp" method="post">
        <input type="submit" value="Logout" class="logout">
    </form>
</body>
</html>