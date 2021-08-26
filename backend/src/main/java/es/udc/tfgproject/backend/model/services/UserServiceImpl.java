package es.udc.tfgproject.backend.model.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.FavouriteAddressDao;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.User.RoleType;
import es.udc.tfgproject.backend.model.entities.UserDao;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private FavouriteAddressDao favouriteAddressDao;

	@Autowired
	private ShoppingCartDao shoppingCartDao;

	@Autowired
	private MenuDao menuDao;

	@Override
	public void signUp(User user) throws DuplicateInstanceException {

		if (userDao.existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		ShoppingCart shoppingCart = new ShoppingCart(user);
		Menu menu = new Menu(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(RoleType.CLIENT);
		user.setShoppingCart(shoppingCart);
		user.setMenu(menu);
		user.setRenewDate(LocalDate.now().plusDays(Constantes.THREE_MONTHS_IN_DAYS));

		userDao.save(user);
		shoppingCartDao.save(shoppingCart);
		menuDao.save(menu);

	}

	@Override
	public void signUpBusinessman(User user) throws DuplicateInstanceException {

		if (userDao.existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		ShoppingCart shoppingCart = new ShoppingCart(user);
		Menu menu = new Menu(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(RoleType.BUSINESSMAN);
		user.setShoppingCart(shoppingCart);
		user.setMenu(menu);
		user.setRenewDate(LocalDate.now().plusDays(Constantes.THREE_MONTHS_IN_DAYS));

		userDao.save(user);
		shoppingCartDao.save(shoppingCart);
		menuDao.save(menu);

	}

	@Override
	@Transactional(readOnly = true)
	public User login(String userName, String password) throws IncorrectLoginException {

		Optional<User> userOptional = userDao.findByUserName(userName);

		if (!userOptional.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}

		if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}

		User user = userOptional.get();
		if (user.getRenewDate().isBefore(LocalDate.now())) {
			user.setFeePaid(false);
		}

		return user;

	}

	@Override
	@Transactional(readOnly = true)
	public User loginFromId(Long id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	@Override
	public User updateProfile(Long id, String firstName, String lastName, String email, String phone)
			throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(id);

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPhone(phone);

		return user;

	}

	@Override
	public void changePassword(Long id, String oldPassword, String newPassword)
			throws InstanceNotFoundException, IncorrectPasswordException {

		User user = permissionChecker.checkUser(id);

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IncorrectPasswordException();
		} else {
			user.setPassword(passwordEncoder.encode(newPassword));
		}

	}

	@Override
	public FavouriteAddress addFavouriteAddress(String street, String cp, Long cityId, Long userId)
			throws InstanceNotFoundException {

		City city = checkCity(cityId);
		User user = permissionChecker.checkUser(userId);

		FavouriteAddress favouriteAddress = new FavouriteAddress();

		favouriteAddress.setUser(user);
		favouriteAddress.setCity(city);
		favouriteAddress.setCp(cp);
		favouriteAddress.setStreet(street);

		favouriteAddressDao.save(favouriteAddress);

		return favouriteAddress;
	}

	@Override
	public void deleteFavouriteAddress(Long userId, Long addressId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkFavouriteAddressExistsAndBelongsToUser(addressId, userId);

		favouriteAddressDao.delete(favouriteAddressDao.findByAddressId(addressId).get());

	}

	@Override
	public Block<FavouriteAddress> findFavouriteAddresses(Long userId, int page, int size) {

		Slice<FavouriteAddress> slice = favouriteAddressDao.findByUserId(userId, PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

	@Override
	public FavouriteAddress findFavAddress(Long userId, Long addressId)
			throws InstanceNotFoundException, PermissionException {
		return permissionChecker.checkFavouriteAddressExistsAndBelongsToUser(addressId, userId);
	}

	/*
	 * Método privado que comprueba que la ciudad está registrada en el sistema
	 */
	private City checkCity(Long cityId) throws InstanceNotFoundException {
		Optional<City> cityOpt = cityDao.findById(cityId);
		City city = null;

		if (!cityOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);
		} else
			city = cityOpt.get();

		return city;
	}

	@Override
	public Block<FavouriteAddress> findFavouriteAddressesByCity(Long userId, Long cityId, int page, int size) {
		Slice<FavouriteAddress> slice = favouriteAddressDao.findByUserIdAndCityId(userId, cityId,
				PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

}
