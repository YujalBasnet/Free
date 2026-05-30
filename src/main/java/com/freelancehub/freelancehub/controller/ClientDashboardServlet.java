package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
import com.freelancehub.freelancehub.dao.ContractDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClientDashboardServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final BidDAO bidDAO = new BidDAO();
    private final ContractDAO contractDAO = new ContractDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int clientId = (int) session.getAttribute("userId");
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            request.setAttribute("totalProjects", projectDAO.countProjectsByClient(connection, clientId));
            request.setAttribute("openProjects", projectDAO.countProjectsByClientAndStatus(connection, clientId, "open"));
            request.setAttribute("projectsInProgress", projectDAO.countProjectsByClientAndStatus(connection, clientId, "in_progress"));
            request.setAttribute("completedProjects", projectDAO.countProjectsByClientAndStatus(connection, clientId, "completed"));
            request.setAttribute("receivedBids", bidDAO.countBidsForClientProjects(connection, clientId));
            request.setAttribute("activeContracts", contractDAO.countContractsByClientAndStatus(connection, clientId, "active"));

            List<String> recentActivities = new ArrayList<>();
            recentActivities.addAll(bidDAO.findLatestBidActivities(connection, clientId, 2));
            String completedTitle = projectDAO.findLatestCompletedProjectTitle(connection, clientId);
            if (completedTitle != null) {
                recentActivities.add("Project completed: " + completedTitle);
            }
            if (recentActivities.isEmpty()) {
                recentActivities.add("No recent activity yet.");
            }
            request.setAttribute("recentActivities", recentActivities);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load client stats right now.");
        }

        request.getRequestDispatcher("/views/client/dashboard.jsp").forward(request, response);
    }
}

