package lv.venta.ut;

import lv.venta.model.*;
import lv.venta.repo.*;
import lv.venta.service.impl.UserServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private IUserRepo userRepo;

	private final List<User> testUsers = new ArrayList<>();

	@BeforeEach
	void setupTestUsers() throws Exception {
		User user1 = new User("John Doe", "john.doe@example.com", Role.USER);
		User user2 = new User("Jane Smith", "jane.smith@example.com", Role.ADMINISTRATOR);

		testUsers.add(userRepo.save(user1));
		testUsers.add(userRepo.save(user2));
	}

	@AfterEach
	void cleanupTestUsers() throws Exception {
		for (User user : testUsers) {
			if (userRepo.existsById(user.getUserId())) {
				userService.deleteUserById(user.getUserId());
			}
		}
		testUsers.clear();
	}

	@Test
	void testSelectAllUsersContainsInsertedOnes() throws Exception {
		List<User> allUsers = userService.selectAllUsers();

		assertTrue(allUsers.stream().anyMatch(user -> user.getEmail().equals("john.doe@example.com")));
		assertTrue(allUsers.stream().anyMatch(user -> user.getEmail().equals("jane.smith@example.com")));
	}

	@Test
	void testSelectUserByIdReturnsCorrectUser() throws Exception {
		User userToFind = testUsers.get(0);

		User foundUser = userService.selectUserById(userToFind.getUserId());

		assertEquals(userToFind.getEmail(), foundUser.getEmail());
		assertEquals(userToFind.getFullName(), foundUser.getFullName());
	}

	@Test
	void testInsertNewUserSuccess() throws Exception {
		User newUser = new User("Alice Test", "alice.test@example.com", Role.LECTURER);

		User insertedUser = userService.insertNewUser(newUser);

		assertNotNull(insertedUser.getUserId());
		assertEquals("Alice Test", insertedUser.getFullName());

		userService.deleteUserById(insertedUser.getUserId());
	}

	@Test
	void testUpdateUserByIdSuccess() throws Exception {
		User userToUpdate = testUsers.get(1);
		User newData = new User("Updated Name", "updated.email@example.com", Role.ADMINISTRATOR);

		User updatedUser = userService.updateUserById(userToUpdate.getUserId(), newData);

		assertEquals("Updated Name", updatedUser.getFullName());
		assertEquals("updated.email@example.com", updatedUser.getEmail());
		assertEquals(Role.ADMINISTRATOR, updatedUser.getRole());
	}

	@Test
	void testDeleteUserById() throws Exception {
		User userToDelete = new User("Delete Me", "delete.me@example.com", Role.USER);
		userToDelete = userRepo.save(userToDelete);
		testUsers.add(userToDelete);

		userService.deleteUserById(userToDelete.getUserId());

		assertFalse(userRepo.existsById(userToDelete.getUserId()));
		testUsers.remove(userToDelete);
	}
}
