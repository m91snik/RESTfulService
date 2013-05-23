package test.com.example.restful;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.restful.dom.User;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class BaseTest {

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
}
