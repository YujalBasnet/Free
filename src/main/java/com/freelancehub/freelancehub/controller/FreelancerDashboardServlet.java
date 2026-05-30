package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
import com.freelancehub.freelancehub.dao.ContractDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.dao.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FreelancerDashboardServlet extends HttpServlet {
    private final BidDAO bidDAO = new BidDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int freelancerId = (int) session.getAttribute("userId");
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            request.setAttribute("projectsApplied", bidDAO.countBidsByFreelancer(connection, freelancerId));
            request.setAttribute("pendingBids", bidDAO.countBidsByFreelancerAndStatus(connection, freelancerId, "pending"));
            request.setAttribute("acceptedBids", bidDAO.countBidsByFreelancerAndStatus(connection, freelancerId, "accepted"));
            request.setAttribute("completedContracts", contractDAO.countContractsByFreelancerAndStatus(connection, freelancerId, "completed"));
            request.setAttribute("averageRating", reviewDAO.findAverageRatingForFreelancer(connection, freelancerId));

            List<String> recentActivities = new ArrayList<>();
            String acceptedBid = bidDAO.findLatestAcceptedBidActivity(connection, freelancerId);
            if (acceptedBid != null) {
                recentActivities.add(acceptedBid);
            }
            String matchingProject = projectDAO.findLatestOpenProjectTitle(connection);
            if (matchingProject != null) {
                recentActivities.add("New project matching your skills: " + matchingProject);
            }
            String completedContract = contractDAO.findLatestCompletedContractTitle(connection, freelancerId);
            if (completedContract != null) {
                recentActivities.add("Contract completed: " + completedContract);
            }
            if (recentActivities.isEmpty()) {
                recentActivities.add("No recent activity yet.");
            }
            request.setAttribute("recentActivities", recentActivities);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load freelancer stats right now.");
        }

        request.getRequestDispatcher("/views/freelancer/dashboard.jsp").forward(request, response);
    }
}

