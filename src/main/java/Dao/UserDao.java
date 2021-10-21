package Dao;

import Model.User;
import Util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao implements IUserDao{
    @Override
    public User findUser(String login, String password) {
        return (User)HibernateSessionFactory.getSessionFactory().openSession().
                createQuery("FROM User users WHERE users.login = :login and users.password = :password").
                setParameter("login", login).
                setParameter("password", password).list().stream().findFirst().get();

    }

    @Override
    public void add(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(user);
        transaction.commit();
        session.close();
    }
}
