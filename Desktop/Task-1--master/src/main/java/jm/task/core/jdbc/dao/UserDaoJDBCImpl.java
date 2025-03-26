package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS public.usertable\n" +
                "(\n" +
                "    id bigint GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),\n" +
                "    name text COLLATE pg_catalog.\"default\",\n" +
                "    lastname text COLLATE pg_catalog.\"default\",\n" +
                "    age numeric,\n" +
                "    CONSTRAINT usertable_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "\n" +
                "TABLESPACE pg_default;\n" +
                "\n" +
                "ALTER TABLE IF EXISTS public.usertable\n" +
                "    OWNER to postgres;";

        try (Connection connection = Util.getConnection();
             PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.executeUpdate();
            System.out.println("Создана таблица пользователей");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE usertable";

        try (Connection connection = Util.getConnection();
             PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.executeUpdate();
            System.out.println("Удалена таблица пользователей");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO public.usertable (\n" +
                "name, lastname, age) VALUES (?,?,?)\n" +
                " returning id;";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeQuery();
            System.out.println("В базу данных добавлен пользователь: " + name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM public.usertable WHERE id = ?;";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Удален пользователь с id: " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM public.usertable;";
        List<User> list = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User smUser = new User(resultSet.getString("name"), resultSet.getString("lastname"), resultSet.getByte("age"));
                smUser.setId(resultSet.getLong("id"));
                list.add(smUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE public.usertable;";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Очищена таблица Пользователей");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
