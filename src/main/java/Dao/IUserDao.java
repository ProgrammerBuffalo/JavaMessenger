package Dao;

import Model.User;

public interface IUserDao {
    User findUser(String login, String password);
    void add(User user);
    void delete(User user);
}
