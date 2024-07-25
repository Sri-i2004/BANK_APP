package com.tracker.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.tracker.dbconnection.*;

@WebServlet("/ChartServlet1")
public class ChartServlet1 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String project = request.getParameter("project");
        String employee = request.getParameter("employee");
        String timeframe = request.getParameter("timeframe");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        String query = buildQuery(project, employee, timeframe);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                double hours = resultSet.getDouble("hours");
                dataset.addValue(hours, category, category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Task Duration Chart",
                "Category",
                "Hours",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        response.setContentType("image/png");
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), barChart, 800, 600);
    }

    private String buildQuery(String project, String employee, String timeframe) {
        StringBuilder query = new StringBuilder("SELECT task_category AS category, SUM(TIMESTAMPDIFF(HOUR, start_time, end_time)) AS hours FROM tasks WHERE 1=1");

        if (project != null && !project.isEmpty()) {
            query.append(" AND project = '").append(project).append("'");
        }
        if (employee != null && !employee.isEmpty()) {
            query.append(" AND employee_name = '").append(employee).append("'");
        }
        switch (timeframe) {
            case "daily":
                query.append(" AND date = CURDATE()");
                break;
            case "weekly":
                query.append(" AND YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1)");
                break;
            case "monthly":
                query.append(" AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())");
                break;
        }
        query.append(" GROUP BY task_category");
        return query.toString();
    }
}