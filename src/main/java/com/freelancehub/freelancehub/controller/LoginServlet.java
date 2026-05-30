package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.UserDAO;
import com.freelancehub.freelancehub.model.User;
import com.freelancehub.freelancehub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = trim(request.getParameter("email"));
        String password = trim(request.getParameter("password"));

        if (email == null || password == null) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            User user = userDAO.findByEmail(connection, email);
            if (user == null || !PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                request.setAttribute("error", "Invalid email or password.");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());

            response.sendRedirect(request.getContextPath() + dashboardPath(user.getRole()));
        } catch (Exception exception) {
            request.setAttribute("error", "Login failed. Please try again.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private String dashboardPath(String role) {
        if ("admin".equalsIgnoreCase(role)) {
            return "/admin/dashboard";
        }
        if ("client".equalsIgnoreCase(role)) {
            return "/views/client/dashboard.jsp";
        }
        return "/views/freelancer/dashboard.jsp";
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
