<%--
  Created by IntelliJ IDEA.
  User: Yujal
  Date: 5/30/2026
  Time: 12:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Browse Projects | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelancer.css" />
</head>
<body>
<div class="freelancer-shell">
    <header class="freelancer-header">
        <div>
            <p class="eyebrow">Freelancer Workspace</p>
            <h1>Browse Available Projects</h1>
            <p class="subtitle">Find opportunities and place your bids.</p>
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
            java.util.List<com.freelancehub.freelancehub.model.Project> projects =
                    (java.util.List<com.freelancehub.freelancehub.model.Project>) request.getAttribute("projects");
        %>
        <% if (projects == null || projects.isEmpty()) { %>
        <p class="empty-state">No open projects right now. Check back soon.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Budget</th>
                    <th>Deadline</th>
                    <th>Place Bid</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Project project : projects) { %>
                <tr>
                    <td><%= project.getTitle() %></td>
                    <td><%= project.getDescription() %></td>
                    <td>NPR <%= String.format("%.2f", project.getBudget()) %></td>
                    <td><%= project.getDeadline() == null ? "-" : project.getDeadline().toString() %></td>
                    <td>
                        <form class="bid-form" method="post" action="${pageContext.request.contextPath}/freelancer/bids/new">
                            <input type="hidden" name="projectId" value="<%= project.getId() %>" />
                            <input type="number" name="bidAmount" step="0.01" min="0" placeholder="Your bid (NPR)" required />
                            <textarea name="proposal" rows="3" placeholder="Short proposal" required></textarea>
                            <button class="primary-btn" type="submit">Submit Bid</button>
                        </form>
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
