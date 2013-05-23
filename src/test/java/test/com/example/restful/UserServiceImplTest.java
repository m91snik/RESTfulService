package test.com.example.restful;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.restful.dao.UserDao;
import com.example.restful.dom.User;
import com.example.restful.service.UserService;

public class UserServiceImplTest extends BaseTest {

	@Mock
	@Autowired
	UserDao userDao;

	@Mock
	@Autowired
	MailSender mailSender;

	@Autowired
	@InjectMocks
	UserService userService;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreate() {
		User user = createTestUser();
		when(userDao.create(user)).thenReturn(user);
		User createdUser = userService.create(user);
		Assert.assertEquals(user, createdUser);
	}

	@Test
	public void testRead() {
		User user = createTestUser();
		user.setId(1L);
		when(userDao.read(user.getId())).thenReturn(user);
		User readUser = userService.read(user.getId());
		Assert.assertEquals(user, readUser);
	}

	@Test
	public void testUpdate() {
		User user = createTestUser();
		user.setId(1L);
		User expectedUser = createTestUser("_exp");
		expectedUser.setId(user.getId());
		when(userDao.update(user)).thenReturn(expectedUser);
		User oldUser = userService.update(user);
		Assert.assertEquals(expectedUser, oldUser);
	}

	@Test
	public void testDelete() {
		User user = createTestUser();
		user.setId(1L);
		when(userDao.delete(user.getId())).thenReturn(user);
		User deletedUser = userService.delete(user.getId());
		Assert.assertEquals(user, deletedUser);
	}

	@Test
	public void testList() {
		User user = createTestUser("1");
		user.setId(1L);
		User user2 = createTestUser("2");
		user2.setId(2L);
		when(userDao.list()).thenReturn(Arrays.asList(user, user2));
		Collection<User> list = userService.list();
		Assert.assertEquals(2, list.size());
		Assert.assertTrue(list.contains(user));
		Assert.assertTrue(list.contains(user2));
	}

	@Test
	public void testForgotPassword() {
		User user = createTestUser();
		user.setId(1L);
		when(userDao.read(user.getId())).thenReturn(user);
		userService.forgotPassword(user.getId());
		verify(mailSender).send(notNull(SimpleMailMessage.class));
	}

	@Test
	public void testLoadUserByUsername() {
		User user = createTestUser();
		user.setId(1L);
		when(userDao.findUserByEmail(user.getEmail())).thenReturn(user);
		UserDetails userDetails = userService.loadUserByUsername(user
				.getEmail());
		Assert.assertEquals(user.getEmail(), userDetails.getUsername());
		Assert.assertEquals(user.getPassword(), userDetails.getPassword());
		Assert.assertTrue(userDetails.getAuthorities().contains(
				new SimpleGrantedAuthority("ROLE_USER")));
	}

}
