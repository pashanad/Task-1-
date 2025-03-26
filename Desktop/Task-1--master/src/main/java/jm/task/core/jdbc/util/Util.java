package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:postgresql://localhost:5432/newproject";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Justcaus20";
//    private static SessionFactory sessionFactory = getSessionFactory();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка загрузки драйвера PostgreSQL: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnections() {
        try (Connection connection = getConnection()) {
            System.out.println("Соединение установлено успешно");
        } catch (SQLException e) {
            System.out.println("Произошла ошибка соединения " + e.getMessage());
        }
    }

    public static Session getSession() {
        Configuration configuration = new Configuration().addAnnotatedClass(User.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory.openSession();
    }

//    public static Session getSession(){
//        return sessionFactory.openSession();
//    }
}