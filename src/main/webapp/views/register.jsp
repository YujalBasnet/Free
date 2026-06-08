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
    <title>Register | FreelanceHub</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #f5f7ff, #e8f0ff);
            color: #1f2937;
        }
        .page {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }
        .card {
            width: 100%;
            max-width: 460px;
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.12);
            padding: 28px;
        }
        .brand {
            font-size: 20px;
            font-weight: 700;
            color: #2563eb;
        }
        .title {
            margin: 8px 0 4px;
            font-size: 24px;
            font-weight: 700;
        }
        .subtitle {
            margin: 0 0 20px;
            color: #6b7280;
            font-size: 14px;
        }
        .form-group {
            margin-bottom: 16px;
        }
        label {
            display: block;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 6px;
            color: #374151;
        }
        input, select {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 10px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }
        input:focus, select:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.2);
        }
        .button {
            width: 100%;
            border: none;
            border-radius: 10px;
            padding: 12px;
            font-size: 14px;
            font-weight: 600;
            color: #ffffff;
            background: #2563eb;
            cursor: pointer;
            transition: background 0.2s ease;
        }
        .button:hover {
            background: #1d4ed8;
        }
        .error {
            background: #fee2e2;
            border: 1px solid #fecaca;
            color: #b91c1c;
            padding: 10px 12px;
            border-radius: 10px;
            font-size: 13px;
            margin-bottom: 16px;
        }
        .footer {
            text-align: center;
            margin-top: 16px;
            font-size: 13px;
            color: #6b7280;
        }
        .footer a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 600;
        }
    </style>
</head>
<body>
<div class="page">
    <div class="card">
        <div class="brand">FreelanceHub</div>
        <div class="title">Create account</div>
        <div class="subtitle">Join and start hiring or freelancing today.</div>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error"><%= error %></div>
        <%
            }
        %>
        <form method="post" action="<%= request.getContextPath() %>/register">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" required />
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required />
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required />
            </div>
            <div class="form-group">
                <label>Role</label>
                <select name="role" required>
                    <option value="client">Client</option>
                    <option value="freelancer">Freelancer</option>
                    <option value="admin">Admin</option>
                </select>
            </div>
            <button class="button" type="submit">Create Account</button>
        </form>
        <div class="footer">
            Already have an account? <a href="<%= request.getContextPath() %>/login">Login here</a>
        </div>
    </div>
</div>
</body>
</html>
