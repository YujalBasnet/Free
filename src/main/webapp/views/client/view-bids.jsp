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
    <title>Received Bids | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<div class="client-shell">
    <header class="client-header">
        <div>
            <p class="eyebrow">Client Workspace</p>
            <h1>Received Bids</h1>
            <p class="subtitle">Review proposals from freelancers and compare offers.</p>
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
            java.util.List<com.freelancehub.freelancehub.model.Bid> bids =
                    (java.util.List<com.freelancehub.freelancehub.model.Bid>) request.getAttribute("bids");
        %>
        <% if (bids == null || bids.isEmpty()) { %>
        <p class="empty-state">No bids received. Check back soon.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Freelancer</th>
                    <th>Proposal</th>
                    <th>Bid (NPR)</th>
                    <th>Status</th>
                    <th>Submitted</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Bid bid : bids) { %>
                <tr>
                    <td><%= bid.getProjectTitle() %></td>
                    <td><%= bid.getFreelancerName() %></td>
                    <td><%= bid.getProposal() %></td>
                    <td>NPR <%= String.format("%.2f", bid.getBidAmount()) %></td>
                    <td><span class="status-pill"><%= bid.getStatus() %></span></td>
                    <td><%= bid.getCreatedAt() == null ? "-" : bid.getCreatedAt().toLocalDate().toString() %></td>
                    <td>
                        <div class="action-buttons">
                            <form method="post" action="${pageContext.request.contextPath}/client/bids/accept">
                                <input type="hidden" name="bidId" value="<%= bid.getId() %>" />
                                <button class="btn-accept" type="submit">Accept</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/client/bids/reject">
                                <input type="hidden" name="bidId" value="<%= bid.getId() %>" />
                                <button class="btn-reject" type="submit">Reject</button>
                            </form>
                        </div>
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
