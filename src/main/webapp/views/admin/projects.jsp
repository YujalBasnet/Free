<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Projects | FreelanceHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
</head>
<body>
<div class="admin-shell">
    <header class="admin-header">
        <div>
            <p class="eyebrow">Control Center</p>
            <h1>Manage Projects</h1>
            <p class="subtitle">Admin Oversees all projects on the platform.</p>
        </div>
        <div class="admin-meta">
            <a class="logout" href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a>
        </div>
    </header>

    <section class="panel">
        <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>

        <div class="search-filter-wrapper">
            <div class="search-box">
                <input type="text" id="projectSearch" class="search-input" placeholder="Search by Project Title or Client Name..." />
            </div>
            <div class="filter-group">
                <button class="filter-btn active" data-status="all">All</button>
                <button class="filter-btn" data-status="open">Open</button>
                <button class="filter-btn" data-status="in progress">In Progress</button>
                <button class="filter-btn" data-status="completed">Completed</button>
                <button class="filter-btn" data-status="closed">Closed</button>
            </div>
        </div>

        <%
            java.util.List<com.freelancehub.freelancehub.model.Project> projects =
                    (java.util.List<com.freelancehub.freelancehub.model.Project>) request.getAttribute("projects");
        %>
        <% if (projects == null || projects.isEmpty()) { %>
        <p class="empty-state">No projects found.</p>
        <% } else { %>
        <div class="table-wrapper">
            <table class="data-table" id="projectsTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Project</th>
                    <th>Client</th>
                    <th>Budget</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <% for (com.freelancehub.freelancehub.model.Project project : projects) { %>
                <tr data-title="<%= project.getTitle() == null ? "" : project.getTitle().replace("\"", "&quot;") %>"
                    data-client="<%= project.getClientName() == null ? "" : project.getClientName().replace("\"", "&quot;") %>"
                    data-status="<%= project.getStatus() == null ? "" : project.getStatus().toLowerCase() %>">
                    <td><%= project.getId() %></td>
                    <td>
                        <div class="project-title-cell">
                            <strong><%= project.getTitle() %></strong>
                            <span class="project-desc"><%= project.getDescription() != null && project.getDescription().length() > 60 ? project.getDescription().substring(0, 57) + "..." : project.getDescription() %></span>
                        </div>
                    </td>
                    <td><%= project.getClientName() == null ? "-" : project.getClientName() %></td>
                    <td class="budget-cell">$<%= String.format("%.0f", project.getBudget()) %></td>
                    <td>
                        <%
                            String status = project.getStatus() != null ? project.getStatus() : "";
                            String statusClass = "";
                            if ("open".equalsIgnoreCase(status)) {
                                statusClass = "status-open";
                            } else if ("in progress".equalsIgnoreCase(status)) {
                                statusClass = "status-progress";
                            } else if ("completed".equalsIgnoreCase(status)) {
                                statusClass = "status-completed";
                            } else if ("closed".equalsIgnoreCase(status)) {
                                statusClass = "status-closed";
                            }
                        %>
                        <span class="status-pill <%= statusClass %>"><%= status %></span>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <p class="no-results-msg" style="display: none;">No projects match your search criteria.</p>
        <% } %>
    </section>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const searchInput = document.getElementById('projectSearch');
        const filterBtns = document.querySelectorAll('.filter-btn');
        const table = document.getElementById('projectsTable');
        const noResultsMsg = document.querySelector('.no-results-msg');

        if (!table) return;

        const rows = table.querySelectorAll('tbody tr');

        let currentStatus = 'all';
        let currentQuery = '';

        function filterTable() {
            let visibleRowsCount = 0;

            rows.forEach(row => {
                const title = row.getAttribute('data-title').toLowerCase();
                const client = row.getAttribute('data-client').toLowerCase();
                const status = row.getAttribute('data-status').toLowerCase();

                const matchesSearch = title.includes(currentQuery) || client.includes(currentQuery);
                const matchesStatus = currentStatus === 'all' || status === currentStatus;

                if (matchesSearch && matchesStatus) {
                    row.style.display = '';
                    visibleRowsCount++;
                } else {
                    row.style.display = 'none';
                }
            });

            if (visibleRowsCount === 0) {
                table.style.display = 'none';
                noResultsMsg.style.display = 'block';
            } else {
                table.style.display = '';
                noResultsMsg.style.display = 'none';
            }
        }

        searchInput.addEventListener('input', function() {
            currentQuery = this.value.toLowerCase().trim();
            filterTable();
        });

        filterBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                filterBtns.forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                currentStatus = this.getAttribute('data-status').toLowerCase();
                filterTable();
            });
        });
    });
</script>
</body>
</html>
