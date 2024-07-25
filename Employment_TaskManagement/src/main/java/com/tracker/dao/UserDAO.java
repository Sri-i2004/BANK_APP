package com.tracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tracker.model.User;

public class UserDAO {
	 private Connection connection;

	    public UserDAO(Connection connection) {
	        this.connection = connection;
	    }

	    public User getUserByUsername(String username,String password) throws SQLException {
	        String query = "SELECT * FROM users WHERE username = ? and password=?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, username);
	        stmt.setString(2, password);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            User user = new User();
	            user.setId(rs.getInt("id"));
	            user.setUsername(rs.getString("username"));
	            user.setPassword(rs.getString("password"));
	            user.setRoleId(rs.getInt("role_id"));
	            return user;
	        }
	        return null;
	    }
	    public void addUser(User user) throws SQLException {
	        String query = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, user.getUsername());
	        stmt.setString(2, user.getPassword());
	        stmt.setInt(3, user.getRoleId());
	        stmt.executeUpdate();
	    }
	    public List<User> getAllEmployees() throws SQLException {
	        List<User> employees = new ArrayList<>();
	        String sql = "SELECT * FROM users WHERE role_id = 1"; // Assuming role_id 1 is for Associate
	        try (Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(sql)) {
	            while (resultSet.next()) {
	                User user = new User();
	                user.setId(resultSet.getInt("id"));
	                user.setUsername(resultSet.getString("username"));
	                user.setPassword(resultSet.getString("password"));
	                user.setRoleId(resultSet.getInt("role_id"));
	                employees.add(user);
	            }
	        }
	        return employees;
	    }
	    
}
