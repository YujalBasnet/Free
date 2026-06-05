package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.UserDAO;
import com.freelancehub.freelancehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class AdminUsersServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<User> users = userDAO.listNonAdminUsers(connection);
            request.setAttribute("users", users);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load users.");
        }

        request.getRequestDispatcher("/views/admin/users.jsp").forward(request, response);
    }
}

