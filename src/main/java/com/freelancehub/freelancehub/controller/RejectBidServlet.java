package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

public class RejectBidServlet extends HttpServlet {
    private final BidDAO bidDAO = new BidDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleUpdate(request, response, "rejected");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, String status) throws IOException, ServletException {
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

        String bidIdValue = request.getParameter("bidId");
        if (bidIdValue == null || bidIdValue.trim().isEmpty()) {
            request.setAttribute("error", "Invalid bid selected.");
            request.getRequestDispatcher("/client/bids").forward(request, response);
            return;
        }

        int bidId;
        try {
            bidId = Integer.parseInt(bidIdValue.trim());
        } catch (NumberFormatException exception) {
            request.setAttribute("error", "Invalid bid selected.");
            request.getRequestDispatcher("/client/bids").forward(request, response);
            return;
        }

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            boolean updated = bidDAO.updateBidStatusForClient(connection, bidId, clientId, status);
            if (!updated) {
                request.setAttribute("error", "Unable to update bid status.");
            }
            response.sendRedirect(request.getContextPath() + "/client/bids");
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to update bid status.");
            request.getRequestDispatcher("/client/bids").forward(request, response);
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
