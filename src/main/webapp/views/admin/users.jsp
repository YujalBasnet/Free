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
    <title>Manage Users | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
</head>
<body>
<div class="admin-shell">
    <header class="admin-header">
        <div>
            <p class="eyebrow">Control Center</p>
            <h1>Manage Users</h1>
            <p class="subtitle">View and Manage all non-admin accounts on the platform.</p>
        </div>
        <div class="admin-meta">
            <a class="logout" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
        </div>
    </header>

    <section class="panel">
        <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        <%
            java.util.List<com.freelancehub.freelancehub.model.User> users =
                    (java.util.List<com.freelancehub.freelancehub.model.User>) request.getAttribute("users");
        %>
        <% if (users == null || users.isEmpty()) { %>
        <p class="empty-state">No users found.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Joined</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.User user : users) { %>
                <tr>
                    <td><%= user.getId() %></td>
                    <td><%= user.getName() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><span class="status-pill"><%= user.getRole() %></span></td>
                    <td><%= user.getCreatedAt() == null ? "-" : user.getCreatedAt().toLocalDate().toString() %></td>
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
