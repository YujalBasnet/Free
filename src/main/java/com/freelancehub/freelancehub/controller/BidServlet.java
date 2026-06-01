package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
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

public class BidServlet extends HttpServlet {
    private final BidDAO bidDAO = new BidDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "freelancer")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Integer freelancerId = (Integer) session.getAttribute("userId");
        if (freelancerId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String projectIdValue = trim(request.getParameter("projectId"));
        String proposal = trim(request.getParameter("proposal"));
        String bidAmountValue = trim(request.getParameter("bidAmount"));

        if (projectIdValue == null || proposal == null || bidAmountValue == null) {
            forwardWithError(request, response, "All bid fields are required.");
            return;
        }

        int projectId;
        double bidAmount;
        try {
            projectId = Integer.parseInt(projectIdValue);
        } catch (NumberFormatException exception) {
            forwardWithError(request, response, "Invalid project selected.");
            return;
        }

        try {
            bidAmount = Double.parseDouble(bidAmountValue);
        } catch (NumberFormatException exception) {
            forwardWithError(request, response, "Bid amount must be a valid number.");
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            if (bidDAO.hasBidForProject(connection, projectId, freelancerId)) {
                forwardWithError(request, response, "You already placed a bid on this project.");
                return;
            }

            int newId = bidDAO.createBid(connection, projectId, freelancerId, proposal, bidAmount);
            if (newId <= 0) {
                forwardWithError(request, response, "Unable to place bid. Please try again.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/freelancer/projects");
        } catch (Exception exception) {
            forwardWithError(request, response, "Unable to place bid. Please try again.");
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Project> projects = projectDAO.listOpenProjects(connection);
            request.setAttribute("projects", projects);
        } catch (Exception ignored) {
        }
        request.getRequestDispatcher("/views/freelancer/browse-projects.jsp").forward(request, response);
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
