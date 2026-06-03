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
    <title>Client's Dashboard | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<div class="client-shell">
    <header class="client-header">
        <div>
            <p class="eyebrow">Client Workspace</p>
            <h1>Your Project Hub</h1>
            <p class="subtitle">Track your projects, bids, and contracts at a glance.</p>
        </div>
        <div class="client-meta">
            <span class="client-chip">Welcome, ${sessionScope.userName}</span>
            <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </header>

    <section class="card-grid">
        <article class="stat-card">
            <h3>Projects Posted</h3>
            <p class="stat">${totalProjects}</p>
        </article>
        <article class="stat-card">
            <h3>Open Projects</h3>
            <p class="stat">${openProjects}</p>
        </article>
        <article class="stat-card">
            <h3>Projects In Progress</h3>
            <p class="stat">${projectsInProgress}</p>
        </article>
        <article class="stat-card">
            <h3>Completed Projects</h3>
            <p class="stat">${completedProjects}</p>
        </article>
        <article class="stat-card highlight">
            <h3>Received Bids</h3>
            <p class="stat">${receivedBids}</p>
        </article>
        <article class="stat-card">
            <h3>Active Contracts</h3>
            <p class="stat">${activeContracts}</p>
        </article>
    </section>

    <section class="dashboard-grid">
        <div class="panel">
            <h2>Quick Actions</h2>
            <div class="action-grid">
                <a class="action-card" href="${pageContext.request.contextPath}/client/projects/new">Post New Project</a>
                <a class="action-card" href="${pageContext.request.contextPath}/client/projects">View My Projects</a>
                <a class="action-card" href="${pageContext.request.contextPath}/client/bids">View Received Bids</a>
                <a class="action-card" href="${pageContext.request.contextPath}/views/client/contracts.jsp">Active Contracts</a>
            </div>
        </div>
        <div class="panel">
            <h2>Recent Activity</h2>
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
</div>
</body>
</html>
