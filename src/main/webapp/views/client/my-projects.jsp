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
    <title>My Projects | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<div class="client-shell">
    <header class="client-header">
        <div>
            <p class="eyebrow">Client Workspace</p>
            <h1>Mine Projects</h1>
            <p class="subtitle">Track every project you have posted.</p>
        </div>
        <div class="client-meta">
            <a class="logout" href="${pageContext.request.contextPath}/client/projects/new">Post Project</a>
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
        <p class="empty-state">No projects yet. Post your first project to get started.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Status</th>
                    <th>Budget</th>
                    <th>Deadline</th>
                    <th>Created</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Project project : projects) { %>
                <tr>
                    <td><%= project.getTitle() %></td>
                    <td><%= project.getStatus() %></td>
                    <td>$<%= String.format("%.2f", project.getBudget()) %></td>
                    <td><%= project.getDeadline() == null ? "-" : project.getDeadline().toString() %></td>
                    <td><%= project.getCreatedAt() == null ? "-" : project.getCreatedAt().toLocalDate().toString() %></td>
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
