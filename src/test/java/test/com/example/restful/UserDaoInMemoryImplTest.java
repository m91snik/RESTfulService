package test.com.example.restful;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.restful.dao.UserDaoInMemoryImpl;
import com.example.restful.dom.User;
import com.example.restful.exception.UserDaoException;

public class UserDaoInMemoryImplTest extends BaseTest {

	@Autowired
	UserDaoInMemoryImpl userDao;

	@Before
	public void beforeTest() {
		userDao.getUsersMap().clear();
	}

	@Test
	public void testCreate() {
		User user = createTestUser();
		User createdUser = userDao.create(user);
		Assert.assertEquals(user, createdUser);
		User storedUser = userDao.getUsersMap().get(user.getId());
		Assert.assertEquals(user, storedUser);
	}

	@Test(expected = UserDaoException.class)
	public void testCreateWithTheSameEmail() {
		User user = createTestUser();
		user.setId(1L);
		userDao.getUsersMap().put(user.getId(), user);
		User newUser = createTestUser();
		userDao.create(newUser);
	}

	@Test
	public void testRead() {
		User user = createTestUser();
		user.setId(2L);
		userDao.getUsersMap().put(user.getId(), user);
		User storedUser = userDao.read(user.getId());
		Assert.assertEquals(user, storedUser);
	}

	@Test
	public void testUpdate() {
		User user = createTestUser();
		user.setId(2L);
		userDao.getUsersMap().put(user.getId(), user);
		User newUser = new User();
		newUser.setId(user.getId());
		newUser.setEmail("ex@mail.com");
		newUser.setPassword("password1");
		newUser.setFirstName("first name1");
		newUser.setLastName("last name1");
		User oldUser = userDao.update(newUser);
		Assert.assertEquals(user, oldUser);
		User updatedUser = userDao.getUsersMap().get(newUser.getId());
		Assert.assertEquals(newUser, updatedUser);
	}

	@Test(expected = UserDaoException.class)
	public void testUpdateToOtherExistedEmail() {
		User user = createTestUser();
		user.setId(1L);
		userDao.getUsersMap().put(user.getId(), user);
		User newUser = createTestUser("2");
		newUser.setId(2L);
		userDao.getUsersMap().put(newUser.getId(), newUser);
		User userToUpdate = createTestUser();
		userToUpdate.setId(1L);
		userToUpdate.setEmail(newUser.getEmail());
		userDao.update(userToUpdate);
	}

	@Test
	public void testDelete() {
		User user = createTestUser();
		user.setId(2L);
		userDao.getUsersMap().put(user.getId(), user);
		userDao.delete(user.getId());
		Assert.assertNull(userDao.getUsersMap().get(user.getId()));
	}

	@Test
	public void testList() {
		User user1 = createTestUser();
		user1.setId(1L);
		userDao.getUsersMap().put(user1.getId(), user1);
		User user2 = createTestUser("2");
		user2.setId(2L);
		userDao.getUsersMap().put(user2.getId(), user2);
		Collection<User> list = userDao.list();
		Assert.assertEquals(2, list.size());
		Assert.assertTrue(list.contains(user1));
		Assert.assertTrue(list.contains(user2));
	}

	@Test
	public void testFindUserByEmail() {
		User user1 = createTestUser();
		user1.setId(1L);
		userDao.getUsersMap().put(user1.getId(), user1);
		User findUserByEmail = userDao.findUserByEmail(user1.getEmail());
		Assert.assertEquals(user1, findUserByEmail);
	}

}
