package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.model.Project;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class AdminProjectsServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Project> projects = projectDAO.listAllProjectsWithClientName(connection);
            request.setAttribute("projects", projects);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load projects.");
        }

        request.getRequestDispatcher("/views/admin/projects.jsp").forward(request, response);
    }
}
