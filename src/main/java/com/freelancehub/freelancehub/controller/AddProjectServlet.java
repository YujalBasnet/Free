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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddProjectServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasRole(request.getSession(false), "client")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "client")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Integer clientId = (Integer) session.getAttribute("userId");
        String title = trim(request.getParameter("title"));
        String description = trim(request.getParameter("description"));
        String budgetValue = trim(request.getParameter("budget"));
        String deadlineValue = trim(request.getParameter("deadline"));

        if (clientId == null || title == null || description == null || budgetValue == null || deadlineValue == null) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetValue);
        } catch (NumberFormatException exception) {
            request.setAttribute("error", "Budget must be a valid number.");
            request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
            return;
        }

        LocalDate deadline;
        try {
            deadline = LocalDate.parse(deadlineValue);
        } catch (DateTimeParseException exception) {
            request.setAttribute("error", "Deadline must be a valid date.");
            request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
            return;
        }

        Project project = new Project();
        project.setClientId(clientId);
        project.setTitle(title);
        project.setDescription(description);
        project.setBudget(budget);
        project.setDeadline(deadline);
        project.setStatus("open");

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            int newId = projectDAO.createProject(connection, project);
            if (newId <= 0) {
                request.setAttribute("error", "Failed to save project. Please try again.");
                request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/client/projects");
        } catch (Exception exception) {
            request.setAttribute("error", "Failed to save project. Please try again.");
            request.getRequestDispatcher("/views/client/post-project.jsp").forward(request, response);
        }
    }

    private boolean hasRole(HttpSession session, String role) {
        if (session == null) {
            return false;
        }
        Object value = session.getAttribute("userRole");
        return value != null && role.equalsIgnoreCase(value.toString());
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
