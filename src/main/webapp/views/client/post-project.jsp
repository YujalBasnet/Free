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
    <title>Post Project | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
</head>
<body>
<div class="client-shell">
    <header class="client-header">
        <div>
            <p class="eyebrow">Client Workspace</p>
            <h1>Post a New Project</h1>
            <p class="subtitle">Share the Details and start receiving bids.</p>
        </div>
        <div class="client-meta">
            <a class="logout" href="${pageContext.request.contextPath}/client/dashboard">Back to Dashboard</a>
        </div>
    </header>

    <section class="panel">
        <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        <form class="form-grid" method="post" action="${pageContext.request.contextPath}/client/projects/new">
            <label>
                Project Title
                <input type="text" name="title" required />
            </label>
            <label>
                Description
                <textarea name="description" rows="5" required></textarea>
            </label>
            <label>
                Budget
                <input type="number" name="budget" step="0.01" min="0" required />
            </label>
            <label>
                Deadline
                <input type="date" name="deadline" required />
            </label>
            <button class="primary-btn" type="submit">Save Project</button>
        </form>
    </section>
</div>
</body>
</html>
