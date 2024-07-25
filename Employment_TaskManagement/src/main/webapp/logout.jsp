<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Invalidate the session and redirect to the login page
    session.removeAttribute("user");
    session.invalidate();

    response.sendRedirect("home.jsp");
%>