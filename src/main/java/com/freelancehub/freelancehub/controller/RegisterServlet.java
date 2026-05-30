package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.UserDAO;
import com.freelancehub.freelancehub.model.User;
import com.freelancehub.freelancehub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = trim(request.getParameter("name"));
        String email = trim(request.getParameter("email"));
        String password = trim(request.getParameter("password"));
        String role = trim(request.getParameter("role"));

        if (name == null || email == null || password == null || role == null) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (!isValidRole(role)) {
            request.setAttribute("error", "Role must be admin, client, or freelancer.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            if (userDAO.emailExists(connection, email)) {
                request.setAttribute("error", "Email already registered.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setRole(role.toLowerCase());
            user.setPasswordHash(PasswordUtil.hashPassword(password));

            int newId = userDAO.createUser(connection, user);
            if (newId <= 0) {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception exception) {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    private boolean isValidRole(String role) {
        return "admin".equalsIgnoreCase(role)
                || "client".equalsIgnoreCase(role)
                || "freelancer".equalsIgnoreCase(role);
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
