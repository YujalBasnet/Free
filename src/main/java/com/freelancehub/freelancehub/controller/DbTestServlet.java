package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class DbTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String ctxUrl = getServletContext().getInitParameter("db.url");
        String ctxUser = getServletContext().getInitParameter("db.user");
        String envUser = System.getenv("DB_USER");
        String envUrl = System.getenv("DB_URL");

        response.getWriter().write("db.url (context): " + safe(ctxUrl) + "\n");
        response.getWriter().write("db.user (context): " + safe(ctxUser) + "\n");
        response.getWriter().write("DB_URL env set: " + (envUrl != null) + "\n");
        response.getWriter().write("DB_USER env set: " + (envUser != null) + "\n\n");

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            if (connection == null || connection.isClosed()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("DB connection is not available.");
                return;
            }
            response.getWriter().write("DB connection OK.");
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("DB connection failed: " + exception.getMessage());
        }
    }

    private String safe(String value) {
        return value == null ? "<null>" : value;
    }
}
