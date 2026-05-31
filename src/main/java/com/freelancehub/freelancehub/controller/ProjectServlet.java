package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.model.Project;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ProjectServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/client/projects".equals(servletPath)) {
            handleClientProjects(request, response);
            return;
        }
        if ("/freelancer/projects".equals(servletPath)) {
            handleFreelancerProjects(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleClientProjects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "client")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Integer clientId = (Integer) session.getAttribute("userId");
        if (clientId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Project> projects = projectDAO.listProjectsByClient(connection, clientId);
            request.setAttribute("projects", projects);
            request.getRequestDispatcher("/views/client/my-projects.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load projects.");
            request.getRequestDispatcher("/views/client/my-projects.jsp").forward(request, response);
        }
    }

    private void handleFreelancerProjects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "freelancer")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Project> projects = projectDAO.listOpenProjects(connection);
            request.setAttribute("projects", projects);
            request.getRequestDispatcher("/views/freelancer/browse-projects.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load projects.");
            request.getRequestDispatcher("/views/freelancer/browse-projects.jsp").forward(request, response);
        }
    }

    private boolean hasRole(HttpSession session, String role) {
        if (session == null) {
            return false;
        }
        Object value = session.getAttribute("userRole");
        return value != null && role.equalsIgnoreCase(value.toString());
    }
}
