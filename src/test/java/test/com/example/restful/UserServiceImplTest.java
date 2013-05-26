package test.com.example.restful;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.restful.dom.User;
import com.example.restful.exception.UserServiceException;
import com.example.restful.repository.UserRepository;
import com.example.restful.service.UserServiceImpl;

// integration test for user service
public class UserServiceImplTest extends BaseTest {

	@Autowired
	UserRepository userRepository;

	@Mock
	MailSender mailSender;

	private final UserServiceImpl userService = new UserServiceImpl();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		userRepository.deleteAll();
		userService.setMailSender(mailSender);
		userService.setUserRepository(userRepository);
	}

	// NOTE: assume that spring crud repository works fine

	@Test
	public void testCreate() {
		User user = createTestUser();
		User createdUser = userService.create(user);
		User storedUser = userRepository.findOne(createdUser.getId());
		Assert.assertEquals(storedUser, createdUser);
		assertUsersEqualsExceptId(user, storedUser);
	}

	@Test
	public void testRead() {
		User user = createTestUser();
		User savedUser = userRepository.save(user);
		User readUser = userService.find(savedUser.getId());
		Assert.assertEquals(savedUser, readUser);
	}

	@Test
	public void testUpdate() {
		User user = createTestUser();
		User savedUser = userRepository.save(user);
		User expectedUser = createTestUser("_exp");
		expectedUser.setId(savedUser.getId());
		User updatedUser = userService.update(expectedUser);
		Assert.assertEquals(expectedUser, updatedUser);
	}

	@Test(expected = UserServiceException.class)
	public void testUpdateWithDuplicatedEmail() {
		User user = createTestUser("1");
		User savedUser = userRepository.save(user);
		User user2 = createTestUser("2");
		User savedUser2 = userRepository.save(user2);
		savedUser2.setEmail(savedUser.getEmail());
		userService.update(savedUser2);
	}

	@Test
	public void testDelete() {
		User user = createTestUser();
		User savedUser = userRepository.save(user);
		userService.delete(savedUser.getId());
		Assert.assertNull(userRepository.findOne(savedUser.getId()));
	}

	@Test
	public void testList() {
		User user = createTestUser("1");
		User user2 = createTestUser("2");
		List<User> savedUsers = userRepository.save(Arrays.asList(user, user2));
		Collection<User> list = userService.list();
		Assert.assertEquals(savedUsers, list);
	}

	@Test
	public void testForgotPassword() {
		User user = createTestUser();
		userRepository.save(user);
		userService.forgotPassword(user.getEmail());
		verify(mailSender).send(notNull(SimpleMailMessage.class));
	}

	@Test
	public void testLoadUserByUsername() {
		User user = createTestUser();
		userRepository.save(user);
		UserDetails userDetails = userService.loadUserByUsername(user
				.getEmail());
		Assert.assertEquals(user.getEmail(), userDetails.getUsername());
		Assert.assertEquals(user.getPassword(), userDetails.getPassword());
		Assert.assertTrue(userDetails.getAuthorities().contains(
				new SimpleGrantedAuthority("ROLE_USER")));
	}

	@Test
	public void testFindByUsername() {
		User user = createTestUser();
		User savedUser = userRepository.save(user);
		User foundUser = userService.findByUsername(user.getEmail());
		Assert.assertEquals(savedUser, foundUser);
	}

}
