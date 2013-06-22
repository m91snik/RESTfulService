package test.com.example.restful;

import com.example.restful.dom.User;
import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public abstract class BaseTest {

    protected User createTestUser(String postfix) {
        User user = new User();
        user.setEmail("ex@mail.com" + postfix);
        user.setPassword("password" + postfix);
        user.setFirstName("first name" + postfix);
        user.setLastName("last name" + postfix);
        return user;
    }

    protected User createTestUser() {
        return createTestUser("");
    }

    protected void assertUsersEqualsExceptId(User expected, User real) {
        Assert.assertEquals(expected.getEmail(), real.getEmail());
        Assert.assertEquals(expected.getPassword(), real.getPassword());
        Assert.assertEquals(expected.getFirstName(), real.getFirstName());
        Assert.assertEquals(expected.getLastName(), real.getLastName());
    }
}
