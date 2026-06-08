<%--
  Created by IntelliJ IDEA.
  User: Yujal
  Date: 5/30/2026
  Time: 12:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contracts | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<div class="client-shell">
    <header class="client-header">
        <div>
            <p class="eyebrow">Client Workspace</p>
            <h1>Active Contracts</h1>
            <p class="subtitle">Track Progress, deadlines, and completion status.</p>
        </div>
        <div class="client-meta">
            <a class="logout" href="${pageContext.request.contextPath}/client/dashboard">Back to Dashboard</a>
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
        <p class="empty-state">No contracts yet. Accept a bid to start one.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Freelancer</th>
                    <th>Status</th>
                    <th>Start</th>
                    <th>End</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Contract contract : contracts) { %>
                <tr>
                    <td><%= contract.getProjectTitle() == null ? "-" : contract.getProjectTitle() %></td>
                    <td><%= contract.getFreelancerName() == null ? "-" : contract.getFreelancerName() %></td>
                    <td><span class="status-pill"><%= contract.getStatus() %></span></td>
                    <td><%= contract.getStartDate() == null ? "-" : contract.getStartDate().toString() %></td>
                    <td><%= contract.getEndDate() == null ? "-" : contract.getEndDate().toString() %></td>
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
