package com.freelancehub.freelancehub.dao;

public class DBConnection {
    public static java.sql.Connection getConnection(jakarta.servlet.ServletContext context) throws java.sql.SQLException {
        String url = context.getInitParameter("db.url");
        String user = context.getInitParameter("db.user");
        String password = context.getInitParameter("db.password");

        if (isBlank(url)) {
            url = System.getenv("DB_URL");
        }
        if (isBlank(user)) {
            user = System.getenv("DB_USER");
        }
        if (isBlank(password)) {
            password = System.getenv("DB_PASSWORD");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }

        return java.sql.DriverManager.getConnection(url, user, password);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
