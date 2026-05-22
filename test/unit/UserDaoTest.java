package test.unit;

import org.junit.Assert;
import org.junit.Test;

import dao.UserDAO;
import model.User;

public class UserDaoTest {
    UserDAO ud = new UserDAO();

    @Test
    public void testCheckLoginTrue() {
        User user = new User();
        user.setUsername("vietbnh");
        user.setPassword("123456");
        boolean result = ud.checkLogin(user);
        Assert.assertTrue(result);
        Assert.assertEquals("Bui Nguyen Hoang Viet", user.getFullName());
        Assert.assertEquals("Receptionist", user.getPosition());
    }

    @Test
    public void testCheckLoginFalse() {
        User user = new User();
        user.setUsername("wrong_user");
        user.setPassword("wrong_pass");
        boolean result = ud.checkLogin(user);
        Assert.assertFalse(result);
    }
}
