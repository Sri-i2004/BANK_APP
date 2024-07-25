package com.tracker.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.tracker.dao.TaskDAO;
import com.tracker.model.Task;

/**
 * Servlet implementation class ChartServlet
 */
public class ChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        Connection connection = com.tracker.dbconnection.DBConnection.getConnection();
		taskDAO = new TaskDAO(connection);
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");
	        String employeeName = request.getParameter("employeeName");
	        String chartType = request.getParameter("type");

	        try {
	            if ("viewCharts".equals(action)) {
	                if ("daily".equals(chartType)) {
	                    generateDailyPieChart(response, employeeName);
	                } else if ("weekly".equals(chartType)) {
	                	generateAverageWeeklyHoursBarChart(response, employeeName);
	                } else if ("monthly".equals(chartType)) {
	                	generateAverageMonthlyHoursBarChart(response, employeeName);
	                }else if("category".equals(chartType)) {
	                	generateTasksByCategoryBarChart(response,employeeName);
	                }
	               
	            } else {
	                
	            }
	        } catch (SQLException e) {
	            throw new ServletException(e);
	        }
	}
	 private void generateDailyPieChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        List<Task> dailyTasks = taskDAO.getDailyTasks(employeeName);
	        DefaultPieDataset dataset = new DefaultPieDataset();

	        for (Task task : dailyTasks) {
	            long duration = task.getEndTime().getTime() - task.getStartTime().getTime();
	            double hours = duration / (1000 * 60 * 60);
	            dataset.setValue(task.getTaskCategory(), hours);
	        }

	        JFreeChart chart = ChartFactory.createPieChart("Daily Tasks", dataset, true, true, false);
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 400, 400);
	    }

	    private void generateWeeklyBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        List<Task> weeklyTasks = taskDAO.getWeeklyTasks(employeeName);
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	        for (Task task : weeklyTasks) {
	            long duration = task.getEndTime().getTime() - task.getStartTime().getTime();
	            double hours = duration / (1000 * 60 * 60);
	            dataset.addValue(hours, "Hours", task.getDate().toString());
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Weekly Tasks", "Date", "Hours", dataset, PlotOrientation.VERTICAL, true, true, false);
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 400, 400);
	    }

	    private void generateMonthlyBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        List<Task> monthlyTasks = taskDAO.getMonthlyTasks(employeeName);
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	        for (Task task : monthlyTasks) {
	            long duration = task.getEndTime().getTime() - task.getStartTime().getTime();
	            double hours = duration / (1000 * 60 * 60);
	            dataset.addValue(hours, "Hours", task.getDate().toString());
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Monthly Tasks", "Date", "Hours", dataset, PlotOrientation.VERTICAL, true, true, false);
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 400, 400);
	    }
	   
	    private void generateTasksByCategoryBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        List<Task> tasks = taskDAO.getTasksByEmployeeName(employeeName);

	        Map<String, Double> categoryHours = new HashMap<>();
	        for (Task task : tasks) {
	            long durationMillis = task.getEndTime().getTime() - task.getStartTime().getTime();
	            double durationHours = (double) durationMillis / (1000 * 60 * 60);
	            categoryHours.put(task.getTaskCategory(), categoryHours.getOrDefault(task.getTaskCategory(), 0.0) + durationHours);
	        }

	        for (Map.Entry<String, Double> entry : categoryHours.entrySet()) {
	            dataset.addValue(entry.getValue(), entry.getKey(), "Categories");
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Tasks by Category", "Category", "Hours", dataset, PlotOrientation.VERTICAL, true, true, false);
	        response.setContentType("image/png");
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);
	    }
	    
	    private void generateTasksByWeekBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        Map<String, Double> weeklyHours = taskDAO.getTasksByWeek(employeeName);

	        for (Map.Entry<String, Double> entry : weeklyHours.entrySet()) {
	            String[] parts = entry.getKey().split("-");
	            String week = parts[0];
	            String taskCategory = parts[1];
	            dataset.addValue(entry.getValue(), taskCategory, week);
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Tasks by Week", "Week", "Hours", dataset);
	        response.setContentType("image/png");
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);
	    }
	    private void generateTasksByMonthBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        Map<String, Double> monthlyHours = taskDAO.getTasksByMonth(employeeName);

	        for (Map.Entry<String, Double> entry : monthlyHours.entrySet()) {
	            String[] parts = entry.getKey().split("-");
	            String month = parts[0];
	            String taskCategory = parts[1];
	            dataset.addValue(entry.getValue(), taskCategory, month);
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Tasks by Month", "Month", "Hours", dataset);
	        response.setContentType("image/png");
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);
	    }
	    private void generateAverageWeeklyHoursBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        Map<String, Double> averageWeeklyHours = taskDAO.getAverageWeeklyHoursByTask(employeeName);

	        for (Map.Entry<String, Double> entry : averageWeeklyHours.entrySet()) {
	            String taskCategory = entry.getKey();
	            double avgHours = entry.getValue();
	            dataset.addValue(avgHours, taskCategory, "Average Hours");
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Average Weekly Hours by Task", "Task Category", "Average Hours", dataset);
	        response.setContentType("image/png");
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);
	    }
	    private void generateAverageMonthlyHoursBarChart(HttpServletResponse response, String employeeName) throws SQLException, IOException {
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        Map<String, Double> averageMonthlyHours = taskDAO.getAverageMonthlyHoursByTask(employeeName);

	        for (Map.Entry<String, Double> entry : averageMonthlyHours.entrySet()) {
	            String taskCategory = entry.getKey();
	            double avgHours = entry.getValue();
	            dataset.addValue(avgHours, taskCategory, "Average Hours");
	        }

	        JFreeChart chart = ChartFactory.createBarChart("Average Monthly Hours by Task", "Task Category", "Average Hours", dataset);
	        response.setContentType("image/png");
	        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);
	    }




}
