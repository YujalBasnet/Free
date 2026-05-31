package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.dao.UserDAO;
import com.freelancehub.freelancehub.model.Project;
import com.freelancehub.freelancehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Project> projects = projectDAO.listOpenProjects(connection, 3);
            List<User> freelancers = userDAO.listUsersByRole(connection, "freelancer", 3);

            request.setAttribute("projects", projects);
            request.setAttribute("spotlightProject", projects.isEmpty() ? null : projects.get(0));
            request.setAttribute("freelancers", freelancers);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load live data: " + exception.getMessage());
        }

        request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }
}

