package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
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
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
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

	private User signUpUser(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "email@gmail.com", "123456789");

		try {
			userService.signUpBusinessman(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	@Test
	public void testAddToEmptyShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		int quantity = 2;

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(), quantity);

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		int quantity1 = 1;
		int quantity2 = 2;

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product1.getId(), quantity1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product2.getId(), quantity2);

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(), quantity1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(), quantity2);

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> shoppingService.addToShoppingCart(user.getId(), NON_EXISTENT_ID, product.getId(), 1));

	}

	@Test
	public void testAddNonExistingProductToShoppingCart() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.addToShoppingCart(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID, 1));

	}

	@Test
	public void testAddToAnotherShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.addToShoppingCart(user1.getId(),
				user2.getShoppingCart().getId(), product.getId(), 1));

	}

	@Test
	public void testAddToShoppingCartWithNonExistentUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.addToShoppingCart(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId(), 1));

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product.getId(), quantity1);
		shoppingService.updateShoppingCartItemQuantity(user.getId(), user.getShoppingCart().getId(), product.getId(),
				quantity2);

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.updateShoppingCartItemQuantity(user.getId(),
				NON_EXISTENT_ID, product.getId(), 2));

	}

	@Test
	public void testUpdateShoppingCartItemQuantityWithNonExistentProductId() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.updateShoppingCartItemQuantity(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID, 1));

	}

	@Test
	public void testUpdateShoppingCartItemQuantityWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.updateShoppingCartItemQuantity(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId(), 1));

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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		shoppingService.addToShoppingCart(user2.getId(), user2.getShoppingCart().getId(), product.getId(), 1);

		assertThrows(PermissionException.class, () -> shoppingService.updateShoppingCartItemQuantity(user1.getId(),
				user2.getShoppingCart().getId(), product.getId(), 2));

	}

	@Test
	public void removeShoppingCartItem() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product1 = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());
		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Hamburguesa con bacon",
				"Carne de ternera, bacon y queso", new BigDecimal(6.50), "otherpath", pCategory.getId());

		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product1.getId(), 1);
		shoppingService.addToShoppingCart(user.getId(), user.getShoppingCart().getId(), product2.getId(), 1);
		shoppingService.removeShoppingCartItem(user.getId(), user.getShoppingCart().getId(), product1.getId());

		ShoppingCart shoppingCart = user.getShoppingCart();

		assertFalse(shoppingCart.getItem(product1.getId()).isPresent());
		assertTrue(shoppingCart.getItem(product2.getId()).isPresent());
		assertFalse(shoppingCartItemDao.findById(product1.getId()).isPresent());

	}

	@Test
	public void removeNonExistentShoppingCartItem() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class, () -> shoppingService.removeShoppingCartItem(user.getId(),
				user.getShoppingCart().getId(), NON_EXISTENT_ID));

	}

	@Test
	public void testRemoveShoppingCartItemWithNonExistentUserId()
			throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.removeShoppingCartItem(NON_EXISTENT_ID,
				user.getShoppingCart().getId(), product.getId()));

	}

	@Test
	public void removeItemFromAnotherShoppingCart() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");
		CompanyCategory category1 = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class, () -> shoppingService.removeShoppingCartItem(user1.getId(),
				user2.getShoppingCart().getId(), product.getId()));

	}

	// @Test
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
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

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

		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product1.getId(), quantity);
		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product2.getId(),
				quantity - 1);
		shoppingService.addToShoppingCart(client.getId(), client.getShoppingCart().getId(), product3.getId(),
				quantity + 1);

		List<ShoppingCartItem> items = shoppingService.findShoppingCartProducts(client.getId(),
				client.getShoppingCart().getId(), company2.getId());

		assertEquals(2, items.size());
		assertEquals(product2, items.get(0).getProduct());
		assertEquals(product3, items.get(1).getProduct());

	}

}
