package com.tracker.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import com.tracker.dao.TaskDAO;
import com.tracker.model.Task;
import com.tracker.model.User;

/**
 * Servlet implementation class TaskServlet
 */
public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
        
	private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        Connection connection = com.tracker.dbconnection.DBConnection.getConnection();
		taskDAO = new TaskDAO(connection);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
    	String action=request.getParameter("action");
    	try {
            switch (action) {
                case "add":
                    addTask(request, response);
                    break;
                case "update":
                    updateTask(request, response);
                    break;
                case "delete":
                    deleteTask(request, response);
                    break;
                default:
                    listTasks(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    private void updateTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int taskId = Integer.parseInt(request.getParameter("id"));
HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String employeeName=user.getUsername();
        String project = request.getParameter("project");
        String dateStr = request.getParameter("date");
        String startTimeStr = request.getParameter("start_time");
        String endTimeStr = request.getParameter("end_time");
        String taskCategory = request.getParameter("task_category");
        String description = request.getParameter("description");
        Date date=null;
        Time startTime=null;
        Time endTime=null;
       
        try {
        	
        	  date = Date.valueOf(dateStr);
             startTime = Time.valueOf(startTimeStr +":00");
              endTime = Time.valueOf(endTimeStr +":00");
        }catch(Exception e) {
        	e.printStackTrace();
        	return;
        }
        boolean isOverlap = false;
        try {
            isOverlap = taskDAO.checkTaskOverlapUpdate(employeeName, date, startTime, endTime,taskId);
        } catch (SQLException e) {
            throw new ServletException("Error checking task overlap", e);
        }

        if (isOverlap) {
            // Task overlaps, handle error (e.g., show error message to user)
            // Redirect back to task entry page or show error message
            response.sendRedirect("addTask.jsp?error=overlap");
            return;
        }
        long durationMillis = endTime.getTime() - startTime.getTime();
        long durationHours = durationMillis / (1000 * 60 * 60);

        // Fetch total hours worked by the employee on the given date
        try {
            double totalHoursToday = taskDAO.getTotalHoursForEmployeeOnDateExcludeCurrent(employeeName, date,taskId);
            totalHoursToday=Math.abs(totalHoursToday);

            // Check if adding this task exceeds 8 hours for the day
            if (totalHoursToday + durationHours > 8) {
                request.setAttribute("error", "Employee cannot work more than 8 hours per day.");
                request.getRequestDispatcher("addTask.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
       

        Task task = new Task();
        task.setId(taskId);
        task.setProject(project);
        task.setDate(date);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setTaskCategory(taskCategory);
        task.setDescription(description);
        try {
            taskDAO.updateTask(task);
            response.sendRedirect("TaskServlet?employeeName=" + employeeName);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    private void deleteTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));
        taskDAO.deleteTask(taskId);
        response.sendRedirect("TaskServlet?employeeName=" + request.getParameter("employeeName"));
    }
    private void listTasks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String employeeName = request.getParameter("employeeName");
        List<Task> tasks = taskDAO.getTasksByEmployeeName(employeeName);
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("viewTasks.jsp").forward(request, response);
    }

    

    protected void addTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String employeeName = user.getUsername();
        String role = user.getRoleId() == 1 ? "Associate" : "Admin";
        String project = request.getParameter("project");
        String dateStr = request.getParameter("date");
        String startTimeStr = request.getParameter("start_time");
        String endTimeStr =request.getParameter("end_time");
        String taskCategory = request.getParameter("task_category");
        String description = request.getParameter("description");
        Date date=null;
        Time startTime=null;
        Time endTime=null;
        try {
            date = Date.valueOf(dateStr);
            startTime = Time.valueOf(startTimeStr+":00");
            endTime = Time.valueOf(endTimeStr+":00");
        } catch (IllegalArgumentException e) {
            // Handle invalid date or time format
            e.printStackTrace(); // Log the exception or handle as needed
            // Optionally redirect or show error message to user
            return; // Exit method if parsing fails
        }
        boolean isOverlap = false;
        try {
            isOverlap = taskDAO.checkTaskOverlap(employeeName, date, startTime, endTime);
        } catch (SQLException e) {
            throw new ServletException("Error checking task overlap", e);
        }

        if (isOverlap) {
            // Task overlaps, handle error (e.g., show error message to user)
            // Redirect back to task entry page or show error message
            response.sendRedirect("addTask.jsp?error=overlap");
            return;
        }
        long durationMillis = endTime.getTime() - startTime.getTime();
        long durationHours = durationMillis / (1000 * 60 * 60);

        // Fetch total hours worked by the employee on the given date
        try {
            double totalHoursToday = taskDAO.getTotalHoursForEmployeeOnDate(employeeName, date);
            totalHoursToday=Math.abs(totalHoursToday);

            // Check if adding this task exceeds 8 hours for the day
            if (totalHoursToday + durationHours > 8) {
                request.setAttribute("error", "Employee cannot work more than 8 hours per day.");
                request.getRequestDispatcher("addTask.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
           e.printStackTrace();
           request.setAttribute("error", "Employee cannot work more than 8 hours per day.");
           request.getRequestDispatcher("addTask.jsp").forward(request, response);
           return;
        }
        Task task = new Task();
        task.setEmployeeName(employeeName);
        task.setRole(role);
        task.setProject(project);
        task.setDate(date);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setTaskCategory(taskCategory);
        task.setDescription(description);
        task.setUserId(user.getId());

        try {
            taskDAO.addTask(task);
            response.sendRedirect("TaskServlet?employeeName=" + employeeName);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeName = request.getParameter("employeeName");
        try {
            List<Task> tasks = taskDAO.getTasksByEmployeeName(employeeName);
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("viewTasks.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    public void destroy() {
        super.destroy(); // Optional, call if you have cleanup in super class

        try {
            if (taskDAO != null) {
                taskDAO.close(); // Close database connection
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any error during cleanup
        }
    }
}
