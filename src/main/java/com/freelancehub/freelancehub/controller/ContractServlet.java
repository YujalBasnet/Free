package com.freelancehub.freelancehub.controller;

import com.freelancehub.freelancehub.dao.ContractDAO;
import com.freelancehub.freelancehub.dao.DBConnection;
import com.freelancehub.freelancehub.dao.ProjectDAO;
import com.freelancehub.freelancehub.model.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ContractServlet extends HttpServlet {
	private final ContractDAO contractDAO = new ContractDAO();
	private final ProjectDAO projectDAO = new ProjectDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String role = String.valueOf(session.getAttribute("userRole"));
		int userId = (int) session.getAttribute("userId");

		try (Connection connection = DBConnection.getConnection(getServletContext())) {
			if ("client".equalsIgnoreCase(role)) {
				List<Contract> contracts = contractDAO.listContractsForClient(connection, userId);
				request.setAttribute("contracts", contracts);
				request.getRequestDispatcher("/views/client/contracts.jsp").forward(request, response);
				return;
			}
			if ("freelancer".equalsIgnoreCase(role)) {
				try {
					contractDAO.backfillContractsForFreelancer(connection, userId);
				} catch (Exception exception) {
					request.setAttribute("error", "Unable to sync accepted bids into contracts: " + exception.getMessage());
					exception.printStackTrace();
				}
				List<Contract> contracts = contractDAO.listContractsForFreelancer(connection, userId);
				request.setAttribute("contracts", contracts);
				request.getRequestDispatcher("/views/freelancer/my-contracts.jsp").forward(request, response);
				return;
			}
		} catch (Exception exception) {
			request.setAttribute("error", "Unable to load contracts: " + exception.getMessage());
			exception.printStackTrace();
			if ("client".equalsIgnoreCase(role)) {
				request.getRequestDispatcher("/views/client/contracts.jsp").forward(request, response);
				return;
			}
			if ("freelancer".equalsIgnoreCase(role)) {
				request.getRequestDispatcher("/views/freelancer/my-contracts.jsp").forward(request, response);
				return;
			}
		}

		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String role = String.valueOf(session.getAttribute("userRole"));
		if (!"freelancer".equalsIgnoreCase(role)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		int freelancerId = (int) session.getAttribute("userId");
		String contractIdValue = request.getParameter("contractId");
		if (contractIdValue == null || contractIdValue.trim().isEmpty()) {
			request.setAttribute("error", "Invalid contract selected.");
			request.getRequestDispatcher("/freelancer/contracts").forward(request, response);
			return;
		}

		int contractId;
		try {
			contractId = Integer.parseInt(contractIdValue.trim());
		} catch (NumberFormatException exception) {
			request.setAttribute("error", "Invalid contract selected.");
			request.getRequestDispatcher("/freelancer/contracts").forward(request, response);
			return;
		}

		try (Connection connection = DBConnection.getConnection(getServletContext())) {
			Contract contract = contractDAO.findContractForFreelancer(connection, contractId, freelancerId);
			if (contract == null) {
				request.setAttribute("error", "Contract not found.");
				request.getRequestDispatcher("/freelancer/contracts").forward(request, response);
				return;
			}

			boolean updated = contractDAO.markContractCompleted(connection, contractId, freelancerId);
			if (updated) {
				projectDAO.updateProjectStatus(connection, contract.getProjectId(), "completed");
				response.sendRedirect(request.getContextPath() + "/freelancer/contracts");
				return;
			}

			request.setAttribute("error", "Unable to complete the contract.");
			request.getRequestDispatcher("/freelancer/contracts").forward(request, response);
		} catch (Exception exception) {
			request.setAttribute("error", "Unable to complete the contract.");
			request.getRequestDispatcher("/freelancer/contracts").forward(request, response);
		}
	}
}
