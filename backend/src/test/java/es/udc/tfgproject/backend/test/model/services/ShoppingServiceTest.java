package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.OrderItem;
import es.udc.tfgproject.backend.model.entities.OrderItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.ProductManagementService;
import es.udc.tfgproject.backend.model.services.ShoppingService;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShoppingServiceTest {

	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private ShoppingService shoppingService;

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
	private ShoppingCartItemDao shoppingCartItemDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	private User signUpUser(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "email@gmail.com", "123456789");

		try {
			userService.signUpBusinessman(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	private Order addOrder(User user, Company company, Product product, LocalDateTime date) {

		String postalAddress = "Postal Address";
		String postalCode = "12345";
		Order order = new Order(user, company, date, true, postalAddress, postalCode);
		OrderItem item = new OrderItem(product, product.getPrice(), 1);

		orderDao.save(order);
		order.addItem(item);
		orderItemDao.save(item);

		return order;

	}

	@Test
	public void testAddToEmptyShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity);

		ShoppingCart shoppingCart = user.getShoppingCart();
		Optional<ShoppingCartItem> item = shoppingCart.getItem(product.getId());

		assertEquals(1, shoppingCart.getItems().size());
		assertTrue(item.isPresent());
		assertEquals(quantity, item.get().getQuantity());

	}

	@Test
	public void testAddNewProductToNonEmptyShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		int quantity1 = 1;
		int quantity2 = 2;

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product1.getId(),
				company.getId(), quantity1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product2.getId(),
				company.getId(), quantity2);

		ShoppingCart shoppingCart = user.getShoppingCart();

		assertEquals(2, shoppingCart.getItems().size());

		Optional<ShoppingCartItem> item1 = shoppingCart.getItem(product1.getId());
		Optional<ShoppingCartItem> item2 = shoppingCart.getItem(product2.getId());

		assertTrue(item1.isPresent());
		assertEquals(item1.get().getQuantity(), quantity1);
		assertTrue(item2.isPresent());
		assertEquals(item2.get().getQuantity(), quantity2);

	}

	@Test
	public void testAddExistingProductToShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity1 = 1;
		int quantity2 = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity2);

		ShoppingCart shoppingCart = user.getShoppingCart();
		Optional<ShoppingCartItem> item = shoppingCart.getItem(product.getId());

		assertEquals(1, shoppingCart.getItems().size());
		assertTrue(item.isPresent());
		assertEquals(quantity1 + quantity2, item.get().getQuantity());

	}

	@Test
	public void testAddToNonExistingShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.addToShoppingCart(user.getId(),
				NON_EXISTENT_ID, product.getId(), company.getId(), 1));

	}

	@Test
	public void testAddNonExistingProductToShoppingCart() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.addToShoppingCart(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID, company.getId(), 1));

	}

	@Test
	public void testAddToAnotherShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.addToShoppingCart(user1.getId(),
				user2.getShoppingCart().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testAddToShoppingCartWithNonExistentUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.addToShoppingCart(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testUpdateShoppingCartItemQuantity() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity1 = 1;
		int quantity2 = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity1);
		shoppingService.updateShoppingCartItemQuantity(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity2);

		Optional<ShoppingCartItem> item = user.getShoppingCart().getItem(product.getId());

		assertEquals(quantity2, item.get().getQuantity());

	}

	@Test
	public void testUpdateShoppingCartItemQuantityWithNonExistentShoppingCartId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.updateShoppingCartItemQuantity(user.getId(),
				NON_EXISTENT_ID, product.getId(), company.getId(), 2));

	}

	@Test
	public void testUpdateShoppingCartItemQuantityWithNonExistentProductId() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.updateShoppingCartItemQuantity(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID, company.getId(), 1));

	}

	@Test
	public void testUpdateShoppingCartItemQuantityWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.updateShoppingCartItemQuantity(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId(), company.getId(), 1));

	}

	@Test
	public void testUpdateShoppingCartItemQuantityToAnotherShoppingCart()
			throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user2.getId(), user2.getShoppingCart().getId(), product.getId(),
				company.getId(), 1);

		assertThrows(PermissionException.class, () -> shoppingService.updateShoppingCartItemQuantity(user1.getId(),
				user2.getShoppingCart().getId(), product.getId(), company.getId(), 2));

	}

	@Test
	public void removeShoppingCartItem() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product1.getId(),
				company.getId(), 1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product2.getId(),
				company.getId(), 1);
		shoppingService.removeShoppingCartItem(user.getId(), user.getShoppingCart().getId(), product1.getId(),
				company.getId());

		ShoppingCart shoppingCart = user.getShoppingCart();

		assertFalse(shoppingCart.getItem(product1.getId()).isPresent());
		assertTrue(shoppingCart.getItem(product2.getId()).isPresent());
		assertFalse(shoppingCartItemDao.findById(product1.getId()).isPresent());

	}

	@Test
	public void removeNonExistentShoppingCartItem() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.removeShoppingCartItem(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID, company.getId()));

	}

	@Test
	public void testRemoveShoppingCartItemWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.removeShoppingCartItem(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId(), company.getId()));

	}

	@Test
	public void removeItemFromAnotherShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.removeShoppingCartItem(user1.getId(),
				user2.getShoppingCart().getId(), product.getId(), company.getId()));

	}

	@Test
	public void testFindShoppingCartProducts() throws InstanceNotFoundException, PermissionException {

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

		City city = new City("Lugo");
		cityDao.save(city);

		Company company1 = businessService.addCompany(user.getId(), "TradFood", 36, true, true, 10, category1.getId());
		Company company2 = businessService.addCompany(user2.getId(), "TexasFood", 24, true, true, 5, category2.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company1.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user2.getId(), company2.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());
		Product product3 = productManagementService.addProduct(user2.getId(), company2.getId(), "Hamburguesa simple",
				"Carne de ternera y queso", new BigDecimal(3.50), "othernewpath", pCategory.getId());

		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product1.getId(),
				company1.getId(), quantity);
		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product2.getId(),
				company2.getId(), quantity - 1);
		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product3.getId(),
				company2.getId(), quantity + 1);

		ShoppingCart cart = shoppingService.findShoppingCartProducts(client.getId(), client.getShoppingCart().getId(),
				company2.getId());

		assertEquals(2, cart.getItems().size());
		assertEquals(4, cart.getTotalQuantity());

		cart = shoppingService.findShoppingCartProducts(client.getId(), client.getShoppingCart().getId(),
				company1.getId());

		assertEquals(1, cart.getItems().size());
		assertEquals(quantity, cart.getTotalQuantity());
	}

	@Test
	public void testChangeShoppingCartHomeSale() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company1 = businessService.addCompany(user.getId(), "TradFood", 36, true, true, 10, category1.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company1.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product1.getId(),
				company1.getId(), quantity);

		ShoppingCart cart = shoppingService.changeShoppingCartHomeSale(user.getId(), user.getShoppingCart().getId(),
				company1.getId(), true);

		assertEquals(cart.getHomeSale(), true);

		cart = shoppingService.changeShoppingCartHomeSale(user.getId(), user.getShoppingCart().getId(),
				company1.getId(), false);

		assertEquals(cart.getHomeSale(), false);

	}

	@Test
	public void testBuyAndFindOrder()
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de jamón",
				"Jamón serrano de bellota", new BigDecimal(5.50), "otherpath", pCategory.getId());

		int quantity1 = 1;
		int quantity2 = 2;
		String postalAddress = "Postal Address";
		String postalCode = "12345";

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), quantity1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product2.getId(),
				company.getId(), quantity2);

		Order order = shoppingService.buy(user.getId(), user.getShoppingCart().getId(), company.getId(), true,
				postalAddress, postalCode);
		Order foundOrder = shoppingService.findOrder(user.getId(), order.getId());

		assertEquals(order, foundOrder);
		assertEquals(user, order.getUser());
		assertEquals(postalAddress, order.getStreet());
		assertEquals(postalCode, order.getCp());
		assertEquals(company, order.getCompany());
		assertEquals(2, order.getItems().size());

		OrderItem item1 = order.getItem(product.getId()).get();
		OrderItem item2 = order.getItem(product2.getId()).get();

		assertEquals(product.getPrice(), item1.getProductPrice());
		assertEquals(quantity1, item1.getQuantity());
		assertEquals(product2.getPrice(), item2.getProductPrice());
		assertEquals(quantity2, item2.getQuantity());
		assertTrue(user.getShoppingCart().isEmpty());

	}

	@Test
	public void testBuyWithNonExistingShoppingCart() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.buy(user.getId(), NON_EXISTENT_ID,
				company.getId(), true, "Postal Address", "12345"));

	}

	@Test
	public void testBuyAnotherShoppingCart() throws InstanceNotFoundException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(PermissionException.class, () -> shoppingService.buy(user1.getId(),
				user2.getShoppingCart().getId(), company.getId(), true, "Postal Address", "12345"));

	}

	@Test
	public void testBuyWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(PermissionException.class, () -> shoppingService.buy(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), company.getId(), true, "Postal Address", "12345"));

	}

	@Test
	public void testBuyEmptyShoppingCart() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		assertThrows(EmptyShoppingCartException.class, () -> shoppingService.buy(user.getId(),
				user.getShoppingCart().getId(), company.getId(), true, "Postal Address", "12345"));

	}

	@Test
	public void testFindNonExistentOrder() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.findOrder(user.getId(), NON_EXISTENT_ID));

	}

	@Test
	public void testFindOrderOfAnotherUser()
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user1.getId(), user1.getShoppingCart().getId(), product.getId(),
				company.getId(), 1);

		Order order = shoppingService.buy(user1.getId(), user1.getShoppingCart().getId(), company.getId(), true,
				"Postal Address", "12345");

		assertThrows(PermissionException.class, () -> shoppingService.findOrder(user2.getId(), order.getId()));

	}

	@Test
	public void testFindOrderWithNonExistingUserId()
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(),
				company.getId(), 1);

		Order order = shoppingService.buy(user.getId(), user.getShoppingCart().getId(), company.getId(), true,
				"Postal Address", "12345");

		assertThrows(PermissionException.class, () -> shoppingService.findOrder(NON_EXISTENT_ID, order.getId()));

	}

	@Test
	public void testFindNoUserOrders() {

		User user = signUpUser("user");
		Block<Order> expectedOrders = new Block<>(new ArrayList<>(), false);

		assertEquals(expectedOrders, shoppingService.findUserOrders(user.getId(), 0, 1));

	}

	@Test
	public void testFindUserOrders() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		Order order1 = addOrder(user, company, product, LocalDateTime.of(2017, 10, 1, 10, 2, 3));
		Order order2 = addOrder(user, company, product, LocalDateTime.of(2018, 11, 1, 10, 2, 3));
		Order order3 = addOrder(user, company, product, LocalDateTime.of(2018, 12, 1, 10, 2, 3));

		Block<Order> expectedBlock = new Block<>(Arrays.asList(order3, order2), true);
		assertEquals(expectedBlock, shoppingService.findUserOrders(user.getId(), 0, 2));

		expectedBlock = new Block<>(Arrays.asList(order1), false);
		assertEquals(expectedBlock, shoppingService.findUserOrders(user.getId(), 1, 2));

	}

	@Test
	public void testFindNoCompanyOrders() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());
		Block<Order> expectedOrders = new Block<>(new ArrayList<>(), false);

		assertEquals(expectedOrders, shoppingService.findCompanyOrders(user.getId(), company.getId(), 0, 1));

	}

	@Test
	public void testFindCompanyOrders() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());
		Company company2 = businessService.addCompany(user2.getId(), "TRadFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user2.getId(), company2.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla y pimientos", new BigDecimal(4.50), "otherpath", pCategory.getId());

		Order order1 = addOrder(user, company, product, LocalDateTime.of(2017, 10, 1, 10, 2, 3));
		Order order2 = addOrder(user, company, product, LocalDateTime.of(2018, 11, 1, 10, 2, 3));
		Order order3 = addOrder(user, company, product, LocalDateTime.of(2018, 12, 1, 10, 2, 3));
		Order order4 = addOrder(user, company2, product2, LocalDateTime.of(2018, 12, 1, 10, 2, 3));

		Block<Order> expectedBlock = new Block<>(Arrays.asList(order3, order2), true);
		assertEquals(expectedBlock, shoppingService.findCompanyOrders(user.getId(), company.getId(), 0, 2));

		expectedBlock = new Block<>(Arrays.asList(order1), false);
		assertEquals(expectedBlock, shoppingService.findCompanyOrders(user.getId(), company.getId(), 1, 2));

		expectedBlock = new Block<>(Arrays.asList(order4), false);
		assertEquals(expectedBlock, shoppingService.findCompanyOrders(user2.getId(), company2.getId(), 0, 1));
	}

}
