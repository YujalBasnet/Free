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
    <title>Freelancer Dashboard | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/freelancer.css" />
</head>
<body>
<div class="freelancer-shell">
    <header class="freelancer-header">
        <div>
            <p class="eyebrow">Freelancer Workspace</p>
            <h1>Opportunities & Contracts</h1>
            <p class="subtitle">Track bids, contracts, and Your performance.</p>
        </div>
        <div class="freelancer-meta">
            <span class="freelancer-chip">Welcome, ${sessionScope.userName}</span>
            <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </header>

    <section class="card-grid">
        <article class="stat-card">
            <h3>Projects Applied</h3>
            <p class="stat">${projectsApplied}</p>
        </article>
        <article class="stat-card">
            <h3>Pending Bids</h3>
            <p class="stat">${pendingBids}</p>
        </article>
        <article class="stat-card">
            <h3>Accepted Bids</h3>
            <p class="stat">${acceptedBids}</p>
        </article>
        <article class="stat-card">
            <h3>Completed Contracts</h3>
            <p class="stat">${completedContracts}</p>
        </article>
        <article class="stat-card highlight">
            <h3>Average Rating</h3>
            <p class="stat">
                <%= request.getAttribute("averageRating") == null ? "-" : String.format("%.1f", (Double) request.getAttribute("averageRating")) %>
            </p>
        </article>
    </section>

    <section class="dashboard-grid">
        <div class="panel">
            <h2>Quick Actions</h2>
            <div class="action-grid">
                <a class="action-card" href="${pageContext.request.contextPath}/freelancer/projects">Browse Projects</a>
                <a class="action-card" href="${pageContext.request.contextPath}/views/freelancer/my-bids.jsp">My Bids</a>
                <a class="action-card" href="${pageContext.request.contextPath}/freelancer/contracts">My Contracts</a>
                <a class="action-card" href="${pageContext.request.contextPath}/views/freelancer/profile.jsp">Edit Profile</a>
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
