package Service;

import Dao.IUserDao;
import Dao.UserDao;
import Model.User;

public class UserService {
    private IUserDao userDao = new UserDao();

    public User findUser(String login, String password){
        return userDao.findUser(login, password);
    }

    public void deleteUser(User user){
        userDao.delete(user);
    }

    public void addUser(User user){
        userDao.add(user);
    }

}
