package com.tracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tracker.model.Task;

public class TaskDAO {
	 private Connection connection;

	    public TaskDAO(Connection connection) {
	        this.connection = connection;
	    }
	    public Map<String, Double> getAverageWeeklyHoursByTask(String employeeName) throws SQLException {
	        String sql = "SELECT task_category, AVG(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600) AS avg_hours " +
	                     "FROM tasks WHERE employee_name = ? GROUP BY task_category";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                Map<String, Double> weeklyAverageHours = new HashMap<>();
	                while (resultSet.next()) {
	                    String taskCategory = resultSet.getString("task_category");
	                    double avgHours = resultSet.getDouble("avg_hours");
	                    weeklyAverageHours.put(taskCategory, avgHours);
	                }
	                return weeklyAverageHours;
	            }
	        }
	    }
	    public Map<String, Double> getAverageMonthlyHoursByTask(String employeeName) throws SQLException {
	        String sql = "SELECT task_category, AVG(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600) AS avg_hours " +
	                     "FROM tasks WHERE employee_name = ? GROUP BY task_category, MONTH(date)";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                Map<String, Double> monthlyAverageHours = new HashMap<>();
	                while (resultSet.next()) {
	                    String taskCategory = resultSet.getString("task_category");
	                    double avgHours = resultSet.getDouble("avg_hours");
	                    monthlyAverageHours.put(taskCategory, avgHours);
	                }
	                return monthlyAverageHours;
	            }
	        }
	    }


	    public void addTask(Task task) throws SQLException {
	        String query = "INSERT INTO tasks (employee_name, role, project, date, start_time, end_time, task_category, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, task.getEmployeeName());
	        stmt.setString(2, task.getRole());
	        stmt.setString(3, task.getProject());
	        stmt.setDate(4, task.getDate());
	        stmt.setTime(5, task.getStartTime());
	        stmt.setTime(6, task.getEndTime());
	        stmt.setString(7, task.getTaskCategory());
	        stmt.setString(8, task.getDescription());
	        stmt.setInt(9, task.getUserId());
	        stmt.executeUpdate();
	    }

	    public List<Task> getTasksByEmployeeName(String employeeName) throws SQLException {
	        String query = "SELECT * FROM tasks WHERE employee_name = ?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, employeeName);
	        ResultSet rs = stmt.executeQuery();
	        List<Task> tasks = new ArrayList<>();
	        while (rs.next()) {
	            Task task = new Task();
	            task.setId(rs.getInt("id"));
	            task.setEmployeeName(rs.getString("employee_name"));
	            task.setRole(rs.getString("role"));
	            task.setProject(rs.getString("project"));
	            task.setDate(rs.getDate("date"));
	            task.setStartTime(rs.getTime("start_time"));
	            task.setEndTime(rs.getTime("end_time"));
	            task.setTaskCategory(rs.getString("task_category"));
	            task.setDescription(rs.getString("description"));
	            task.setUserId(rs.getInt("user_id"));
	            tasks.add(task);
	        }
	        return tasks;
	    }
	    public void close() throws SQLException {
	    	connection.close();
	    }
	    public boolean checkTaskOverlap(String employeeName, Date date, Time startTime, Time endTime) throws SQLException {
	        // Implement SQL query to check for overlapping tasks
	        String query = "SELECT COUNT(*) FROM tasks " +
	                       "WHERE employee_name = ? " +
	                       "AND date = ? " +
	                       "AND ((start_time < ? AND end_time > ?) OR " +
	                       "(start_time >= ? AND start_time < ?) OR " +
	                       "(end_time > ? AND end_time <= ?))";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, employeeName);
	            statement.setDate(2, date);
	            statement.setTime(3, startTime);
	            statement.setTime(4, startTime);
	            statement.setTime(5, startTime);
	            statement.setTime(6, endTime);
	            statement.setTime(7, startTime);
	            statement.setTime(8, endTime);

	            ResultSet resultSet = statement.executeQuery();
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                return count > 0;
	            }
	        }

	        return false;
	    }
	    public boolean checkTaskOverlapUpdate(String employeeName, Date date, Time startTime, Time endTime,int id) throws SQLException {
	        // Implement SQL query to check for overlapping tasks
	        String query = "SELECT COUNT(*) FROM tasks " +
	                       "WHERE employee_name = ? " +
	                       "AND date = ? " +
	                       "AND ((start_time < ? AND end_time > ?) OR " +
	                       "(start_time >= ? AND start_time < ?) OR " +
	                       "(end_time > ? AND end_time <= ?)) and id!=?";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, employeeName);
	            statement.setDate(2, date);
	            statement.setTime(3, startTime);
	            statement.setTime(4, startTime);
	            statement.setTime(5, startTime);
	            statement.setTime(6, endTime);
	            statement.setTime(7, startTime);
	            statement.setTime(8, endTime);
	            statement.setInt(9, id);

	            ResultSet resultSet = statement.executeQuery();
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                return count > 0;
	            }
	        }

	        return false;
	    }
	    public double getTotalHoursForEmployeeOnDateExcludeCurrent(String employeeName, Date date,int id) throws SQLException {
	        double totalHours = 0;

	        String sql = "SELECT SUM(TIME_TO_SEC(TIMEDIFF(end_time, start_time))) / 3600 AS total_hours " +
	                     "FROM tasks " +
	                     "WHERE employee_name = ? AND date = ? and id!=?";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            statement.setDate(2, date);
	            statement.setInt(3, id);
	            ResultSet resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                totalHours = resultSet.getDouble("total_hours");
	            }
	        }

	        return totalHours;
	    }
	    public double getTotalHoursForEmployeeOnDate(String employeeName, Date date) throws SQLException {
	        double totalHours = 0;

	        String sql = "SELECT SUM(TIME_TO_SEC(TIMEDIFF(end_time, start_time))) / 3600 AS total_hours " +
	                     "FROM tasks " +
	                     "WHERE employee_name = ? AND date = ? ";

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            statement.setDate(2, date);
	            
	            ResultSet resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                totalHours = resultSet.getDouble("total_hours");
	            }
	        }

	        return totalHours;
	    }
	    public void deleteTask(int taskId) throws SQLException {
	        String query = "DELETE FROM tasks WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, taskId);
	            stmt.executeUpdate();
	        }
	    }
	    public Task getTaskById(int taskId) throws SQLException {
	        Task task = null;
	        String query = "SELECT * FROM tasks WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, taskId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    task = new Task();
	                    task.setId(rs.getInt("id"));
	                    task.setEmployeeName(rs.getString("employee_name"));
	                    task.setRole(rs.getString("role"));
	                    task.setProject(rs.getString("project"));
	                    task.setDate(rs.getDate("date"));
	                    task.setStartTime(rs.getTime("start_time"));
	                    task.setEndTime(rs.getTime("end_time"));
	                    task.setTaskCategory(rs.getString("task_category"));
	                    task.setDescription(rs.getString("description"));
	                    task.setUserId(rs.getInt("user_id"));
	                }
	            }
	        }
	        return task;
	    }
	    public void updateTask(Task task) throws SQLException {
	        String query = "UPDATE tasks SET project = ?, date = ?, start_time = ?, end_time = ?, " +
	                       "task_category = ?, description = ? WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, task.getProject());
	            stmt.setDate(2, task.getDate());
	            stmt.setTime(3, task.getStartTime());
	            stmt.setTime(4, task.getEndTime());
	            stmt.setString(5, task.getTaskCategory());
	            stmt.setString(6, task.getDescription());
	            stmt.setInt(7, task.getId());

	            stmt.executeUpdate();
	        }
	    }
	   
	    // Existing methods...

	    public List<Task> getDailyTasks(String employeeName) throws SQLException {
	        String sql = "SELECT * FROM tasks WHERE employee_name = ? AND date = CURDATE()";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            ResultSet resultSet = statement.executeQuery();
	            List<Task> tasks = new ArrayList<>();
	            while (resultSet.next()) {
	                tasks.add(mapRowToTask(resultSet));
	            }
	            return tasks;
	        }
	        
	    }
	    public Map<String, Double> getTasksByWeek(String employeeName) throws SQLException {
	        String sql = "SELECT YEARWEEK(date, 1) AS week, task_category, SUM(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600) AS total_hours " +
	                     "FROM tasks WHERE employee_name = ? GROUP BY YEARWEEK(date, 1), task_category";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                Map<String, Double> weeklyHours = new HashMap<>();
	                while (resultSet.next()) {
	                    String week = resultSet.getString("week");
	                    String taskCategory = resultSet.getString("task_category");
	                    double totalHours = resultSet.getDouble("total_hours");
	                    weeklyHours.put(week + "-" + taskCategory, totalHours);
	                }
	                return weeklyHours;
	            }
	        }
	    }
	    public Map<String, Double> getTasksByMonth(String employeeName) throws SQLException {
	        String sql = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, task_category, SUM(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600) AS total_hours " +
	                     "FROM tasks WHERE employee_name = ? GROUP BY DATE_FORMAT(date, '%Y-%m'), task_category";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                Map<String, Double> monthlyHours = new HashMap<>();
	                while (resultSet.next()) {
	                    String month = resultSet.getString("month");
	                    String taskCategory = resultSet.getString("task_category");
	                    double totalHours = resultSet.getDouble("total_hours");
	                    monthlyHours.put(month + "-" + taskCategory, totalHours);
	                }
	                return monthlyHours;
	            }
	        }
	    }



	    public List<Task> getWeeklyTasks(String employeeName) throws SQLException {
	        String sql = "SELECT * FROM tasks WHERE employee_name = ? AND YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1)";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            ResultSet resultSet = statement.executeQuery();
	            List<Task> tasks = new ArrayList<>();
	            while (resultSet.next()) {
	                tasks.add(mapRowToTask(resultSet));
	            }
	            return tasks;
	        }
	    }

	    public List<Task> getMonthlyTasks(String employeeName) throws SQLException {
	        String sql = "SELECT * FROM tasks WHERE employee_name = ? AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            ResultSet resultSet = statement.executeQuery();
	            List<Task> tasks = new ArrayList<>();
	            while (resultSet.next()) {
	                tasks.add(mapRowToTask(resultSet));
	            }
	            return tasks;
	        }
	    }

	    private Task mapRowToTask(ResultSet resultSet) throws SQLException {
	        Task task = new Task();
	        task.setId(resultSet.getInt("id"));
	        task.setEmployeeName(resultSet.getString("employee_name"));
	        task.setProject(resultSet.getString("project"));
	        task.setDate(resultSet.getDate("date"));
	        task.setStartTime(resultSet.getTime("start_time"));
	        task.setEndTime(resultSet.getTime("end_time"));
	        task.setTaskCategory(resultSet.getString("task_category"));
	        task.setDescription(resultSet.getString("description"));
	        return task;
	    }
	    public Map<String, Double> getTasksByCategory(String employeeName) throws SQLException {
	        String sql = "SELECT task_category, SUM(TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 3600) AS total_hours " +
	                     "FROM tasks WHERE employee_name = ? GROUP BY task_category";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setString(1, employeeName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                Map<String, Double> categoryHours = new HashMap<>();
	                while (resultSet.next()) {
	                    String category = resultSet.getString("task_category");
	                    double totalHours = resultSet.getDouble("total_hours");
	                    categoryHours.put(category, totalHours);
	                }
	                return categoryHours;
	            }
	        }
	    }

	    


}
