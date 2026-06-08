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
    <title>Admin Dashboard | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
</head>
<body>
<div class="admin-shell">
    <header class="admin-header">
        <div>
            <p class="eyebrow">Control Center</p>
            <h1>Admin Dashboard</h1>
            <p class="subtitle">Monitor platform Activity and take quick action.</p>
        </div>
        <div class="admin-meta">
            <span class="admin-chip">Role: Admin</span>
            <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </header>

    <section class="card-grid">
        <article class="stat-card">
            <h3>Total Users</h3>
            <p class="stat">${totalUsers}</p>
        </article>
        <article class="stat-card">
            <h3>Total Clients</h3>
            <p class="stat">${totalClients}</p>
        </article>
        <article class="stat-card">
            <h3>Total Freelancers</h3>
            <p class="stat">${totalFreelancers}</p>
        </article>
        <article class="stat-card">
            <h3>Total Projects</h3>
            <p class="stat">${totalProjects}</p>
        </article>
        <article class="stat-card">
            <h3>Total Bids</h3>
            <p class="stat">${totalBids}</p>
        </article>
        <article class="stat-card">
            <h3>Total Contracts</h3>
            <p class="stat">${totalContracts}</p>
        </article>
        <article class="stat-card alert">
            <h3>Pending Reports</h3>
            <p class="stat">${pendingReports}</p>
        </article>
    </section>

    <section class="dashboard-grid">
        <div class="panel">
            <h2>Quick Actions</h2>
            <div class="action-grid">
                <a class="action-card" href="${pageContext.request.contextPath}/admin/users">Manage Users</a>
                <a class="action-card" href="${pageContext.request.contextPath}/admin/projects">Manage Projects</a>
                <a class="action-card" href="${pageContext.request.contextPath}/views/admin/reports.jsp">Manage Reports</a>
                <a class="action-card" href="${pageContext.request.contextPath}/views/admin/analytics.jsp">View Reviews</a>
            </div>
        </div>
        <div class="panel">
            <h2>Recent Activities</h2>
            <ul class="activity-list">
                <%
                    java.util.List<String> activities = (java.util.List<String>) request.getAttribute("recentActivities");
                    if (activities != null) {
                        for (String activity : activities) {
                %>
                <li><span class="dot"></span><%= activity %></li>
                <%
                        }
                    }
                %>
            </ul>
            <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>
        </div>
    </section>

    <section class="panel future">
        <h2>Future Features</h2>
        <div class="future-grid">
            <div class="future-card">Suspend User</div>
            <div class="future-card">Delete Project</div>
            <div class="future-card">Platform Analytics</div>
        </div>
    </section>
</div>
</body>
</html>
