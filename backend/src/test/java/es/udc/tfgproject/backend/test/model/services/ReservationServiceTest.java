package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuItem;
import es.udc.tfgproject.backend.model.entities.MenuItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.Province;
import es.udc.tfgproject.backend.model.entities.ProvinceDao;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.Reserve.PeriodType;
import es.udc.tfgproject.backend.model.entities.ReserveDao;
import es.udc.tfgproject.backend.model.entities.ReserveItem;
import es.udc.tfgproject.backend.model.entities.ReserveItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.CompanyDoesntAllowReservesException;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.EmptyMenuException;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.MaximumCapacityExceededException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.exceptions.ReservationDateIsBeforeNowException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.ProductManagementService;
import es.udc.tfgproject.backend.model.services.ReservationService;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationServiceTest {

	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ProductManagementService productManagementService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private ProvinceDao provinceDao;

	@Autowired
	private ReserveDao reserveDao;

	@Autowired
	private MenuItemDao MenuItemDao;

	@Autowired
	private ReserveItemDao reserveItemDao;

	private User signUpUser(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "clienteDelivr@gmail.com", "123456789");

		try {
			userService.signUpBusinessman(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	private Reserve addReserve(User user, Company company, Product product, LocalDate date, Integer diners,
			PeriodType periodType) {

		Reserve reserve = new Reserve(user, company, date, diners, periodType, new BigDecimal(20), new BigDecimal(10));
		ReserveItem item = new ReserveItem(product, product.getPrice(), 1);

		reserveDao.save(reserve);
		reserve.addItem(item);
		reserveItemDao.save(item);

		return reserve;
	}

	@Test
	public void testAddToEmptyMenu() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity);

		Menu menu = user.getMenu();
		;
		Optional<MenuItem> item = menu.getItem(product.getId());

		assertEquals(1, menu.getItems().size());
		assertTrue(item.isPresent());
		assertEquals(quantity, item.get().getQuantity());

	}

	@Test
	public void testAddNewProductToNonEmptyMenu() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		int quantity1 = 1;
		int quantity2 = 2;

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product1.getId(), company.getId(),
				quantity1);
		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product2.getId(), company.getId(),
				quantity2);

		Menu menu = user.getMenu();
		;

		assertEquals(2, menu.getItems().size());

		Optional<MenuItem> item1 = menu.getItem(product1.getId());
		Optional<MenuItem> item2 = menu.getItem(product2.getId());

		assertTrue(item1.isPresent());
		assertEquals(item1.get().getQuantity(), quantity1);
		assertTrue(item2.isPresent());
		assertEquals(item2.get().getQuantity(), quantity2);

	}

	@Test
	public void testAddExistingProductToMenu() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity1 = 1;
		int quantity2 = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);
		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity1);
		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity2);

		Menu menu = user.getMenu();
		Optional<MenuItem> item = menu.getItem(product.getId());

		assertEquals(1, menu.getItems().size());
		assertTrue(item.isPresent());
		assertEquals(quantity1 + quantity2, item.get().getQuantity());

	}

	@Test
	public void testAddToNonExistingmenu() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> reservationService.addToMenu(user.getId(), NON_EXISTENT_ID, product.getId(), company.getId(), 1));

	}

	@Test
	public void testAddNonExistingProductTomenu() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(InstanceNotFoundException.class, () -> reservationService.addToMenu(user.getId(),
				user.getMenu().getId(), NON_EXISTENT_ID, company.getId(), 1));

	}

	@Test
	public void testAddToAnothermenu() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> reservationService.addToMenu(user1.getId(),
				user2.getMenu().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testAddToMenuWithNonExistentUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> reservationService.addToMenu(NON_EXISTENT_ID,
				user.getMenu().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testUpdateMenuItemQuantity() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity1 = 1;
		int quantity2 = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity1);
		reservationService.updateMenuItemQuantity(user.getId(), user.getMenu().getId(), product.getId(),
				company.getId(), quantity2);

		Optional<MenuItem> item = user.getMenu().getItem(product.getId());

		assertEquals(quantity2, item.get().getQuantity());

	}

	@Test
	public void testUpdateMenuItemQuantityWithNonExistentmenuId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class, () -> reservationService.updateMenuItemQuantity(user.getId(),
				NON_EXISTENT_ID, product.getId(), company.getId(), 2));

	}

	@Test
	public void testUpdateMenuItemQuantityWithNonExistentProductId() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(InstanceNotFoundException.class, () -> reservationService.updateMenuItemQuantity(user.getId(),
				user.getMenu().getId(), NON_EXISTENT_ID, company.getId(), 1));

	}

	@Test
	public void testUpdateMenuItemQuantityWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> reservationService.updateMenuItemQuantity(NON_EXISTENT_ID,
				user.getMenu().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testUpdateMenuItemQuantityToAnothermenu() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user2.getId(), user2.getMenu().getId(), product.getId(), company.getId(), 1);

		assertThrows(PermissionException.class, () -> reservationService.updateMenuItemQuantity(user1.getId(),
				user2.getMenu().getId(), product.getId(), company.getId(), 2));

	}

	@Test
	public void removeMenuItem() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product1.getId(), company.getId(), 1);
		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product2.getId(), company.getId(), 1);
		reservationService.removeMenuItem(user.getId(), user.getMenu().getId(), product1.getId(), company.getId());

		Menu menu = user.getMenu();

		assertFalse(menu.getItem(product1.getId()).isPresent());
		assertTrue(menu.getItem(product2.getId()).isPresent());
		assertFalse(MenuItemDao.findById(product1.getId()).isPresent());

	}

	@Test
	public void removeNonExistentMenuItem() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(InstanceNotFoundException.class, () -> reservationService.removeMenuItem(user.getId(),
				user.getMenu().getId(), NON_EXISTENT_ID, company.getId()));

	}

	@Test
	public void testRemoveMenuItemWithNonExistentUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> reservationService.removeMenuItem(NON_EXISTENT_ID,
				user.getMenu().getId(), product.getId(), company.getId()));

	}

	@Test
	public void removeItemFromAnothermenu() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> reservationService.removeMenuItem(user1.getId(),
				user2.getMenu().getId(), product.getId(), company.getId()));

	}

	@Test
	public void testFindmenuProducts() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		User user2 = signUpUser("user2");
		User client = signUpUser("client");
		int quantity = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		CompanyCategory category2 = new CompanyCategory("Americano");
		companyCategoryDao.save(category2);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company1 = businessService.addCompany(user.getId(), "TradFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));
		Company company2 = businessService.addCompany(user2.getId(), "TexasFood", 24, true, true, 5, category2.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product1 = productManagementService.addProduct(user.getId(), company1.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user2.getId(), company2.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());
		Product product3 = productManagementService.addProduct(user2.getId(), company2.getId(), "Hamburguesa simple",
				"Carne de ternera y queso", new BigDecimal(3.50), "othernewpath", pCategory.getId());

		reservationService.addToMenu(client.getId(), client.getMenu().getId(), product1.getId(), company1.getId(),
				quantity);
		reservationService.addToMenu(client.getId(), client.getMenu().getId(), product2.getId(), company2.getId(),
				quantity - 1);
		reservationService.addToMenu(client.getId(), client.getMenu().getId(), product3.getId(), company2.getId(),
				quantity + 1);

		Menu cart = reservationService.findMenuProducts(client.getId(), client.getMenu().getId(), company2.getId());

		assertEquals(2, cart.getItems().size());
		assertEquals(4, cart.getTotalQuantity());

		cart = reservationService.findMenuProducts(client.getId(), client.getMenu().getId(), company1.getId());

		assertEquals(1, cart.getItems().size());
		assertEquals(quantity, cart.getTotalQuantity());
	}

	@Test
	public void testReservationAndFindReserve()
			throws InstanceNotFoundException, PermissionException, EmptyMenuException, MaximumCapacityExceededException,
			ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		int quantity1 = 1;

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity1);

		Reserve reserve = reservationService.reservation(user.getId(), user.getMenu().getId(), company.getId(),
				LocalDate.of(2021, 10, 23), 15, PeriodType.DINER);

		Reserve expectedReserve = reservationService.findReserve(user.getId(), reserve.getId());

		assertEquals(expectedReserve, reserve);
		assertEquals(expectedReserve.getCompany().getId(), reserve.getCompany().getId());
		assertEquals(expectedReserve.getDate(), reserve.getDate());
		assertEquals(expectedReserve.getDeposit(), reserve.getDeposit());
		assertEquals(expectedReserve.getDiners(), reserve.getDiners());
		assertEquals(expectedReserve.getPeriodType(), reserve.getPeriodType());
		assertEquals(expectedReserve.getTotalPrice(), reserve.getTotalPrice());

		ReserveItem item1 = reserve.getItem(product.getId()).get();

		assertEquals(product.getPrice(), item1.getProductPrice());
		assertEquals(quantity1, item1.getQuantity());
		assertTrue(user.getMenu().isEmpty());

	}

	@Test
	public void testCancelReservation() throws InstanceNotFoundException, PermissionException, EmptyMenuException,
			MaximumCapacityExceededException, ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		int quantity1 = 1;

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), quantity1);

		Reserve reserve = reservationService.reservation(user.getId(), user.getMenu().getId(), company.getId(),
				LocalDate.of(2021, 10, 23), 15, PeriodType.DINER);

		reservationService.cancelReservation(user.getId(), reserve.getId());
		Block<Reserve> expectedBlock = new Block<>(Arrays.asList(), false);
		assertEquals(expectedBlock, reservationService.findUserReserves(user.getId(), 0, 2));
	}

	@Test
	public void testReservationWithNonExistingMenu() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(InstanceNotFoundException.class, () -> reservationService.reservation(user.getId(),
				NON_EXISTENT_ID, company.getId(), LocalDate.of(2021, 07, 23), 15, PeriodType.DINER));

	}

	@Test
	public void testReservationAnotherMenu() throws InstanceNotFoundException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(PermissionException.class, () -> reservationService.reservation(user1.getId(),
				user2.getMenu().getId(), company.getId(), LocalDate.of(2021, 07, 23), 15, PeriodType.DINER));

	}

	@Test
	public void testReservationWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(InstanceNotFoundException.class, () -> reservationService.reservation(NON_EXISTENT_ID,
				user.getMenu().getId(), company.getId(), LocalDate.of(2021, 07, 23), 15, PeriodType.DINER));

	}

	@Test
	public void testReservationEmptyMenu() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		assertThrows(EmptyMenuException.class, () -> reservationService.reservation(user.getId(),
				user.getMenu().getId(), company.getId(), LocalDate.of(2021, 07, 23), 15, PeriodType.DINER));

	}

	@Test
	public void testFindNonExistentReserve() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class,
				() -> reservationService.findReserve(user.getId(), NON_EXISTENT_ID));

	}

	@Test
	public void testFindReserveOfAnotherUser()
			throws InstanceNotFoundException, PermissionException, EmptyMenuException, MaximumCapacityExceededException,
			ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user1.getId(), user1.getMenu().getId(), product.getId(), company.getId(), 2);

		Reserve reserve = reservationService.reservation(user1.getId(), user1.getMenu().getId(), company.getId(),
				LocalDate.of(2021, 11, 23), 15, PeriodType.DINER);

		assertThrows(PermissionException.class, () -> reservationService.findReserve(user2.getId(), reserve.getId()));

	}

	@Test
	public void testFindReserveWithNonExistingUserId()
			throws InstanceNotFoundException, PermissionException, EmptyMenuException, MaximumCapacityExceededException,
			ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		reservationService.addToMenu(user.getId(), user.getMenu().getId(), product.getId(), company.getId(), 2);

		Reserve reserve = reservationService.reservation(user.getId(), user.getMenu().getId(), company.getId(),
				LocalDate.of(2021, 10, 23), 15, PeriodType.DINER);

		assertThrows(PermissionException.class, () -> reservationService.findReserve(NON_EXISTENT_ID, reserve.getId()));

	}

	@Test
	public void testFindNoUserReserves() {

		User user = signUpUser("user");
		Block<Reserve> expectedReserves = new Block<>(new ArrayList<>(), false);

		assertEquals(expectedReserves, reservationService.findUserReserves(user.getId(), 0, 1));

	}

	@Test
	public void testFindNoCompanyReserves() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));
		Block<Reserve> expectedOrders = new Block<>(new ArrayList<>(), false);

		assertEquals(expectedOrders, reservationService.findCompanyReserves(user.getId(), company.getId(),
				LocalDate.now(), PeriodType.DINER, 0, 2));

	}

	@Test
	public void testFindCompanyReserves() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId(),
				20, LocalTime.of(10, 0), LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		Reserve reserve1 = addReserve(user, company, product, LocalDate.of(2021, 07, 25), 3, PeriodType.DINER);
		Reserve reserve2 = addReserve(user, company, product, LocalDate.of(2021, 07, 28), 3, PeriodType.LUNCH);
		Reserve reserve3 = addReserve(user2, company, product, LocalDate.of(2021, 07, 25), 2, PeriodType.DINER);

		Block<Reserve> expectedBlock = new Block<>(Arrays.asList(reserve1, reserve3), false);
		assertEquals(expectedBlock, reservationService.findCompanyReserves(user.getId(), company.getId(),
				LocalDate.of(2021, 07, 25), PeriodType.DINER, 0, 2));

		expectedBlock = new Block<>(Arrays.asList(reserve2), false);
		assertEquals(expectedBlock, reservationService.findCompanyReserves(user.getId(), company.getId(),
				LocalDate.of(2021, 07, 28), PeriodType.LUNCH, 0, 2));

		expectedBlock = new Block<>(Arrays.asList(), false);
		assertEquals(expectedBlock, reservationService.findCompanyReserves(user.getId(), company.getId(),
				LocalDate.of(2021, 07, 28), PeriodType.DINER, 0, 2));
		assertEquals(expectedBlock, reservationService.findCompanyReserves(user.getId(), company.getId(),
				LocalDate.of(2021, 07, 27), PeriodType.LUNCH, 0, 2));
	}

}
