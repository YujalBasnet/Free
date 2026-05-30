package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
import com.freelancehub.freelancehub.dao.ContractDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.dao.ReportDAO;
import com.freelancehub.freelancehub.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AdminServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final BidDAO bidDAO = new BidDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            request.setAttribute("totalUsers", userDAO.countUsers(connection));
            request.setAttribute("totalClients", userDAO.countUsersByRole(connection, "client"));
            request.setAttribute("totalFreelancers", userDAO.countUsersByRole(connection, "freelancer"));
            request.setAttribute("totalProjects", projectDAO.countProjects(connection));
            request.setAttribute("totalBids", bidDAO.countBids(connection));
            request.setAttribute("totalContracts", contractDAO.countContracts(connection));
            request.setAttribute("pendingReports", reportDAO.countPendingReports(connection));

            List<String> recentActivities = new ArrayList<>();
            String latestUser = userDAO.findLatestUserName(connection);
            if (latestUser != null) {
                recentActivities.add("New user registered: " + latestUser);
            }
            String latestProject = projectDAO.findLatestProjectTitle(connection);
            if (latestProject != null) {
                recentActivities.add("New project posted: " + latestProject);
            }
            Integer latestReportId = reportDAO.findLatestReportId(connection);
            if (latestReportId != null) {
                recentActivities.add("New report submitted: #" + latestReportId);
            }
            Integer latestContractId = contractDAO.findLatestContractId(connection);
            if (latestContractId != null) {
                recentActivities.add("New contract created: #" + latestContractId);
            }
            if (recentActivities.isEmpty()) {
                recentActivities.add("No recent activity yet.");
            }
            request.setAttribute("recentActivities", recentActivities);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load admin stats right now.");
        }

        request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
    }
}
