package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Session session = Util.getSession();

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
        try {
            session.beginTransaction();

            session.createNativeQuery(sql).executeUpdate();

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getSession();

        String sql = "DROP TABLE IF EXISTS public.usertable";
        try {
            session.beginTransaction();

            session.createNativeQuery(sql).executeUpdate();

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getSession();
        try {
            session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSession();
        try {
            session.beginTransaction();

            User user = session.get(User.class,id);
            session.delete(user);

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getSession();
        List<User> list=new ArrayList<>();
        try {
            session.beginTransaction();

            list = session.createQuery("FROM User", User.class).getResultList();

            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM User").executeUpdate();

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
}
