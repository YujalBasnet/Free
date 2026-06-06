<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>FreelanceHub | Hire & Work Smarter</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<header class="site-header">
    <div class="container header-row">
        <div class="logo">FreelanceHub</div>
        <nav class="nav">
            <a href="#categories">Categories</a>
            <a href="#how">How it Works</a>
            <a href="#projects">Projects</a>
            <a href="#talent">Top Talent</a>
        </nav>
        <div class="header-actions">
            <a class="link" href="${pageContext.request.contextPath}/login">Login</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Join Now</a>
        </div>
    </div>
</header>

<section class="hero">
    <div class="container hero-grid">
        <div>
            <p class="eyebrow">Marketplace for clients & freelancers</p>
            <h1>Build teams and ship projects faster than ever</h1>
            <p class="hero-subtitle">FreelanceHub connects you with verified freelancers for design, development, marketing, and more.</p>
            <form class="search-bar" action="${pageContext.request.contextPath}/login" method="get">
                <input type="text" name="q" placeholder="Try: Full-stack developer, UI designer, SEO" />
                <button class="btn btn-dark" type="submit">Search Talent</button>
            </form>
            <div class="hero-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Post a Project</a>
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/register">Become a Freelancer</a>
            </div>
            <div class="trusted">
                <span>Trusted by teams of all sizes</span>
                <div class="trusted-logos">
                    <span>Local startups</span>
                    <span>Agencies</span>
                    <span>Product teams</span>
                </div>
            </div>
            <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>
        </div>
        <div class="hero-card">
            <div class="hero-card-header">
                <h3>Project Spotlight</h3>
                <span class="status">Open</span>
            </div>
            <%
                com.freelancehub.freelancehub.model.Project spotlight =
                        (com.freelancehub.freelancehub.model.Project) request.getAttribute("spotlightProject");
                if (spotlight == null) {
            %>
            <h2>No open projects yet</h2>
            <p>Post the first project to feature it here.</p>
            <div class="meta">
                <div>
                    <p>Budget</p>
                    <strong>NPR -</strong>
                </div>
                <div>
                    <p>Deadline</p>
                    <strong>-</strong>
                </div>
                <div>
                    <p>Bids</p>
                    <strong>-</strong>
                </div>
            </div>
            <a class="btn btn-dark" href="${pageContext.request.contextPath}/register">Post Project</a>
            <%
                } else {
            %>
            <h2><%= spotlight.getTitle() %></h2>
            <p><%= spotlight.getDescription() %></p>
            <div class="meta">
                <div>
                    <p>Budget</p>
                    <strong>NPR <%= String.format("%.0f", spotlight.getBudget()) %></strong>
                </div>
                <div>
                    <p>Deadline</p>
                    <strong><%= spotlight.getDeadline() == null ? "-" : spotlight.getDeadline().toString() %></strong>
                </div>
                <div>
                    <p>Status</p>
                    <strong><%= spotlight.getStatus() %></strong>
                </div>
            </div>
            <a class="btn btn-dark" href="${pageContext.request.contextPath}/login">View Project</a>
            <%
                }
            %>
        </div>
    </div>
</section>

<section id="categories" class="section">
    <div class="container">
        <div class="section-header">
            <h2>Explore categories</h2>
            <p>Find specialists across every skillset.</p>
        </div>
        <div class="card-grid">
            <div class="info-card">
                <h3>Web Development</h3>
                <p>Frontend, backend, and full-stack experts.</p>
            </div>
            <div class="info-card">
                <h3>Design & Branding</h3>
                <p>UI/UX, logos, and product design.</p>
            </div>
            <div class="info-card">
                <h3>Digital Marketing</h3>
                <p>SEO, paid ads, content, and growth.</p>
            </div>
            <div class="info-card">
                <h3>Writing & Translation</h3>
                <p>Copywriting, technical docs, localization.</p>
            </div>
            <div class="info-card">
                <h3>Data & Analytics</h3>
                <p>Dashboards, automation, reporting.</p>
            </div>
            <div class="info-card">
                <h3>Video & Motion</h3>
                <p>Editing, animation, explainer videos.</p>
            </div>
        </div>
    </div>
</section>

<section id="how" class="section alt">
    <div class="container">
        <div class="section-header">
            <h2>How FreelanceHub works</h2>
            <p>Everything you need to go from idea to delivery.</p>
        </div>
        <div class="steps">
            <div class="step">
                <span class="step-number">01</span>
                <h3>Post a project</h3>
                <p>Define scope, budget, and timeline in minutes.</p>
            </div>
            <div class="step">
                <span class="step-number">02</span>
                <h3>Review bids</h3>
                <p>Compare proposals and chat with shortlisted freelancers.</p>
            </div>
            <div class="step">
                <span class="step-number">03</span>
                <h3>Manage delivery</h3>
                <p>Track milestones, payments, and feedback in one place.</p>
            </div>
        </div>
    </div>
</section>

<section id="projects" class="section">
    <div class="container">
        <div class="section-header">
            <h2>Projects being hired right now</h2>
            <p>Live projects from your database.</p>
        </div>
        <%
            java.util.List<com.freelancehub.freelancehub.model.Project> projects =
                    (java.util.List<com.freelancehub.freelancehub.model.Project>) request.getAttribute("projects");
        %>
        <% if (projects == null || projects.isEmpty()) { %>
        <p class="empty-state">No open projects right now. Post a project to get started.</p>
        <% } else { %>
        <div class="project-grid">
            <% for (com.freelancehub.freelancehub.model.Project project : projects) { %>
            <div class="project-card">
                <div class="badge"><%= project.getStatus() %></div>
                <h3><%= project.getTitle() %></h3>
                <p><%= project.getDescription() %></p>
                <div class="project-meta">
                    <span>NPR <%= String.format("%.0f", project.getBudget()) %></span>
                    <span><%= project.getDeadline() == null ? "-" : project.getDeadline().toString() %></span>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>
</section>

<section id="talent" class="section alt">
    <div class="container">
        <div class="section-header">
            <h2>Top freelancers ready to help</h2>
            <p>Live freelancer accounts from your database.</p>
        </div>
        <%
            java.util.List<com.freelancehub.freelancehub.model.User> freelancers =
                    (java.util.List<com.freelancehub.freelancehub.model.User>) request.getAttribute("freelancers");
        %>
        <% if (freelancers == null || freelancers.isEmpty()) { %>
        <p class="empty-state">No freelancers registered yet.</p>
        <% } else { %>
        <div class="talent-grid">
            <% for (com.freelancehub.freelancehub.model.User freelancer : freelancers) { %>
            <div class="talent-card">
                <div class="avatar"><%= freelancer.getName().isEmpty() ? "FH" : freelancer.getName().substring(0, 1).toUpperCase() %></div>
                <div>
                    <h3><%= freelancer.getName() %></h3>
                    <p>Freelancer</p>
                </div>
                <span class="rate">Active</span>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="cta">
            <div>
                <h2>Ready to hire or get hired?</h2>
                <p>Create an account and start matching with the right talent today.</p>
            </div>
            <div class="cta-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Get Started</a>
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/login">Sign In</a>
            </div>
        </div>
    </div>
</section>

<footer class="footer">
    <div class="container footer-grid">
        <div>
            <h3>FreelanceHub</h3>
            <p>Smart marketplace for modern project teams.</p>
        </div>
        <div>
            <h4>For Clients</h4>
            <a href="${pageContext.request.contextPath}/register">Post a Project</a>
            <a href="${pageContext.request.contextPath}/login">Manage Contracts</a>
        </div>
        <div>
            <h4>For Freelancers</h4>
            <a href="${pageContext.request.contextPath}/register">Create Profile</a>
            <a href="${pageContext.request.contextPath}/login">Browse Projects</a>
        </div>
        <div>
            <h4>Company</h4>
            <a href="#how">How it Works</a>
            <a href="#categories">Categories</a>
        </div>
    </div>
    <div class="footer-bottom">&copy; <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) %> FreelanceHub. All rights reserved.</div>
</footer>
</body>
</html>
