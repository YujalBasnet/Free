<%--
  Created by IntelliJ IDEA.
  User: Yujal
  Date: 5/30/2026
  Time: 12:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
<h2>Create Account</h2>
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color:red;"><%= error %></p>
<%
    }
%>
<form method="post" action="<%= request.getContextPath() %>/register">
    <div>
        <label>Name</label>
        <input type="text" name="name" required />
    </div>
    <div>
        <label>Email</label>
        <input type="email" name="email" required />
    </div>
    <div>
        <label>Password</label>
        <input type="password" name="password" required />
    </div>
    <div>
        <label>Role</label>
        <select name="role" required>
            <option value="client">Client</option>
            <option value="freelancer">Freelancer</option>
            <option value="admin">Admin</option>
        </select>
    </div>
    <button type="submit">Register</button>
</form>
<p>
    Already have an account? <a href="<%= request.getContextPath() %>/login">Login here</a>
</p>
</body>
</html>
