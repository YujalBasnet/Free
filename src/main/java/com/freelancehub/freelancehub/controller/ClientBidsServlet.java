package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.BidDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.model.Bid;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ClientBidsServlet extends HttpServlet {
    private final BidDAO bidDAO = new BidDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            List<Bid> bids = bidDAO.listBidsForClient(connection, clientId);
            request.setAttribute("bids", bids);
            request.getRequestDispatcher("/views/client/view-bids.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Unable to load bids.");
            request.getRequestDispatcher("/views/client/view-bids.jsp").forward(request, response);
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

