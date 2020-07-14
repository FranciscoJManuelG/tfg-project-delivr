package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.FavouriteAddressDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.User.RoleType;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	private final Long NON_EXISTENT_ID = new Long(-1);
	private final long NON_EXISTENT_CITY_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private FavouriteAddressDao favouriteAddressDao;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName", userName + "@" + userName + ".com", "675123098");
	}

	@Test
	public void testSignUpAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {

		User user = createUser("user");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		assertEquals(user, loggedInUser);
		assertEquals(RoleType.CLIENT, loggedInUser.getRole());

	}

	@Test
	public void testSignUpBusinessmanAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {

		User user = createUser("user");

		userService.signUpBusinessman(user);

		User loggedInUser = userService.loginFromId(user.getId());

		assertEquals(user, loggedInUser);
		assertEquals(RoleType.BUSINESSMAN, loggedInUser.getRole());

	}

	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException {

		User user = createUser("user");

		userService.signUp(user);
		assertThrows(DuplicateInstanceException.class, () -> userService.signUp(user));

	}

	@Test
	public void testLoginFromNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> userService.loginFromId(NON_EXISTENT_ID));
	}

	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {

		User user = createUser("user");
		String clearPassword = user.getPassword();

		userService.signUp(user);

		User loggedInUser = userService.login(user.getUserName(), clearPassword);

		assertEquals(user, loggedInUser);

	}

	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException {

		User user = createUser("user");
		String clearPassword = user.getPassword();

		userService.signUp(user);
		assertThrows(IncorrectLoginException.class, () -> userService.login(user.getUserName(), 'X' + clearPassword));

	}

	@Test
	public void testLoginWithNonExistentUserName() {
		assertThrows(IncorrectLoginException.class, () -> userService.login("X", "Y"));
	}

	@Test
	public void testUpdateProfile() throws InstanceNotFoundException, DuplicateInstanceException {

		User user = createUser("user");

		userService.signUp(user);

		user.setFirstName('X' + user.getFirstName());
		user.setLastName('X' + user.getLastName());
		user.setEmail('X' + user.getEmail());
		user.setPhone('X' + user.getPhone());

		userService.updateProfile(user.getId(), 'X' + user.getFirstName(), 'X' + user.getLastName(),
				'X' + user.getEmail(), 'X' + user.getPhone());

		User updatedUser = userService.loginFromId(user.getId());

		assertEquals(user, updatedUser);

	}

	@Test
	public void testUpdateProfileWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(NON_EXISTENT_ID, "X", "X", "X", "X"));
	}

	@Test
	public void testChangePassword() throws DuplicateInstanceException, InstanceNotFoundException,
			IncorrectPasswordException, IncorrectLoginException {

		User user = createUser("user");
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;

		userService.signUp(user);
		userService.changePassword(user.getId(), oldPassword, newPassword);
		userService.login(user.getUserName(), newPassword);

	}

	@Test
	public void testChangePasswordWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> userService.changePassword(NON_EXISTENT_ID, "X", "Y"));
	}

	@Test
	public void testChangePasswordWithIncorrectPassword() throws DuplicateInstanceException {

		User user = createUser("user");
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;

		userService.signUp(user);
		assertThrows(IncorrectPasswordException.class,
				() -> userService.changePassword(user.getId(), 'Y' + oldPassword, newPassword));

	}

	@Test
	public void testAddFavouriteAddress() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		userService.signUp(user);

		long numberOfAddresses = favouriteAddressDao.count();

		userService.addFavouriteAddress("Rosalia 18", "15700", city.getId(), user.getId());
		userService.addFavouriteAddress("Jorge 21", "15700", city.getId(), user.getId());

		long count = favouriteAddressDao.count();

		assertEquals(numberOfAddresses + 2, count);

	}

	@Test
	public void testNonExistingCity() throws InstanceNotFoundException {

		User user = createUser("user");

		assertThrows(InstanceNotFoundException.class,
				() -> userService.addFavouriteAddress("Rosalia 18", "15700", NON_EXISTENT_CITY_ID, user.getId()));
	}

	@Test
	public void testDeleteFavouriteAddress()
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		User user = createUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		userService.signUp(user);

		FavouriteAddress address1 = userService.addFavouriteAddress("Rosalia 18", "15700", city.getId(), user.getId());
		userService.addFavouriteAddress("Magan 23", "13456", city.getId(), user.getId());

		long numberOfFavouriteAddresses = favouriteAddressDao.count();

		userService.deleteFavouriteAddress(user.getId(), address1.getAddressId());

		assertEquals(numberOfFavouriteAddresses - 1, 1);

	}

	@Test
	public void testFindCompanyAddresses() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("user");
		User user2 = createUser("other");
		City city = new City("Lugo");
		cityDao.save(city);

		userService.signUp(user);
		userService.signUp(user2);

		FavouriteAddress address1 = userService.addFavouriteAddress("Rosalia 18", "15700", city.getId(), user.getId());
		FavouriteAddress address2 = userService.addFavouriteAddress("Manuel 36", "12760", city.getId(), user.getId());
		userService.addFavouriteAddress("Juan 48", "14900", city.getId(), user2.getId());

		Block<FavouriteAddress> expectedBlock = new Block<>(Arrays.asList(address1, address2), false);
		Block<FavouriteAddress> actual = userService.findFavouriteAddresses(user.getId(), 0, 10);

		assertEquals(expectedBlock, actual);

	}

}
