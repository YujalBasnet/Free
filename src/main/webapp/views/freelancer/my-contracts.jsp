<%--
  Created by IntelliJ IDEA.
  User: Yujal
  Date: 5/30/2026
  Time: 12:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Contracts | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelancer.css" />
</head>
<body>
<div class="freelancer-shell">
    <header class="freelancer-header">
        <div>
            <p class="eyebrow">Freelancer Workspace</p>
            <h1>My Contracts</h1>
            <p class="subtitle">Track active work and mark contracts completed.</p>
        </div>
        <div class="freelancer-meta">
            <a class="logout" href="${pageContext.request.contextPath}/freelancer/dashboard">Back to Dashboard</a>
        </div>
    </header>

    <section class="panel">
        <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        <%
            java.util.List<com.freelancehub.freelancehub.model.Contract> contracts =
                    (java.util.List<com.freelancehub.freelancehub.model.Contract>) request.getAttribute("contracts");
        %>
        <% if (contracts == null || contracts.isEmpty()) { %>
        <p class="empty-state">No contracts yet. Once a bid is accepted, it will appear here.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Client</th>
                    <th>Status</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Contract contract : contracts) { %>
                <tr>
                    <td><%= contract.getProjectTitle() == null ? "-" : contract.getProjectTitle() %></td>
                    <td><%= contract.getClientName() == null ? "-" : contract.getClientName() %></td>
                    <td><span class="status-pill"><%= contract.getStatus() %></span></td>
                    <td><%= contract.getStartDate() == null ? "-" : contract.getStartDate().toString() %></td>
                    <td><%= contract.getEndDate() == null ? "-" : contract.getEndDate().toString() %></td>
                    <td>
                        <% if ("active".equalsIgnoreCase(contract.getStatus())) { %>
                        <form method="post" action="${pageContext.request.contextPath}/freelancer/contracts/complete">
                            <input type="hidden" name="contractId" value="<%= contract.getId() %>" />
                            <button class="btn-accept" type="submit">Mark Completed</button>
                        </form>
                        <% } else { %>
                        <span>-</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } %>
    </section>
</div>
</body>
</html>
