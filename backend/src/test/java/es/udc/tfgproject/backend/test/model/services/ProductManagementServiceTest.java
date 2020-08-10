package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.ProductManagementService;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductManagementServiceTest {

	private final long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private ProductManagementService productManagementService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private ProductDao productDao;

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
	public void testAddProduct() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		Product expectedProduct = productDao.findById(product.getId()).get();

		assertEquals(expectedProduct, product);
		assertEquals(expectedProduct.getName(), "Bocadillo de tortilla");
		assertEquals(expectedProduct.getDescription(), "Tortilla con cebolla");
		assertEquals(expectedProduct.getPrice(), new BigDecimal(3.50));
		assertEquals(expectedProduct.getCompany(), company);
		assertEquals(expectedProduct.getProductCategory(), pCategory);
		assertEquals(expectedProduct.getImage().getPath(), "/img/path");
	}

	@Test
	public void testAddToNonExistingProductCategory() throws InstanceNotFoundException {

		User user = signUpUser("user");

		City city = new City("Lugo");
		cityDao.save(city);
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);

		Company company = businessService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId());

		assertThrows(InstanceNotFoundException.class,
				() -> productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
						"Tortilla con cebolla", new BigDecimal(3.50), "path", NON_EXISTENT_ID));
	}

	@Test
	public void testAddWrongUser() throws InstanceNotFoundException {

		User user = signUpUser("user");
		User userWrong = signUpUser("userWrong");

		City city = new City("Lugo");
		cityDao.save(city);
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		Company company = businessService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId());

		assertThrows(PermissionException.class,
				() -> productManagementService.addProduct(userWrong.getId(), company.getId(), "Bocadillo de tortilla",
						"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId()));
	}

	@Test
	public void testAddToNonExistingCompany() throws InstanceNotFoundException {

		User user = signUpUser("user");

		City city = new City("Lugo");
		cityDao.save(city);
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		assertThrows(InstanceNotFoundException.class,
				() -> productManagementService.addProduct(user.getId(), NON_EXISTENT_ID, "Bocadillo de tortilla",
						"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId()));
	}

	@Test
	public void testEditProduct() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
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

		Product editedProduct = productManagementService.editProduct(user.getId(), company.getId(), product.getId(),
				"Ensalada césar", "Tomate, lechuga, cebolla, espárragos", new BigDecimal(4.75), "newPath",
				pCategory2.getId());

		assertEquals(editedProduct.getName(), "Ensalada césar");
		assertEquals(editedProduct.getDescription(), "Tomate, lechuga, cebolla, espárragos");
		assertEquals(editedProduct.getPrice(), new BigDecimal(4.75));
		assertEquals(editedProduct.getProductCategory(), pCategory2);
		assertEquals(editedProduct.getImage().getPath(), "/img/newPath");
	}

	@Test
	public void testRemoveProduct() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "burger.jpg", pCategory.getId());

		long numberOfProducts = productDao.count();

		productManagementService.removeProduct(user.getId(), company.getId(), product.getId());

		/* Comprobamos que hay una fila menos en Product */
		assertEquals(numberOfProducts - 1, 0);
	}

	@Test
	public void testBlockAndUnlockProduct() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		Product blockedProduct = productManagementService.blockProduct(user.getId(), company.getId(), product.getId());

		assertTrue(blockedProduct.getBlock());
		assertEquals(blockedProduct.getId(), product.getId());

		Product unlockedProduct = productManagementService.unlockProduct(user.getId(), company.getId(),
				product.getId());

		assertFalse(unlockedProduct.getBlock());

	}

	@Test
	public void testFindProduct() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user1");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		Product foundProduct = productManagementService.findProduct(user.getId(), product.getId());

		assertEquals(product, foundProduct);
		assertEquals(company, foundProduct.getCompany());
		assertEquals(user, foundProduct.getCompany().getUser());
		assertEquals("Bocadillo de tortilla", foundProduct.getName());
		assertEquals("Tortilla con cebolla", foundProduct.getDescription());
		assertEquals(new BigDecimal(3.50), foundProduct.getPrice());
		assertEquals("/img/path", foundProduct.getImage().getPath());
		assertEquals(pCategory, foundProduct.getProductCategory());

	}

	@Test
	public void testFindNonExistentProduct() {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class,
				() -> productManagementService.findProduct(user.getId(), NON_EXISTENT_ID));

	}

	@Test
	public void testFindProductOfAnotherUser() throws InstanceNotFoundException, PermissionException {

		User user1 = signUpUser("user1");
		User user2 = signUpUser("user2");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user1.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Product product = productManagementService.addProduct(user1.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class,
				() -> productManagementService.findProduct(user2.getId(), product.getId()));

	}

	@Test
	public void testFindProductWithNonExistingUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user1");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Product product = productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class,
				() -> productManagementService.findProduct(NON_EXISTENT_ID, product.getId()));

	}

	@Test
	public void testFindAllProductCategories() {

		ProductCategory category1 = new ProductCategory("category1");
		ProductCategory category2 = new ProductCategory("category2");

		productCategoryDao.save(category2);
		productCategoryDao.save(category1);

		assertEquals(Arrays.asList(category1, category2), productManagementService.findAllProductCategories());

	}

	@Test
	public void testFindAllCompanyProducts() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
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

		Product product2 = productManagementService.addProduct(user.getId(), company.getId(), "Ensalada césar",
				"Tomate, lechuga, cebolla, espárragos", new BigDecimal(4.75), "newPath", pCategory2.getId());

		assertEquals(Arrays.asList(product1, product2),
				productManagementService.findAllCompanyProducts(user.getId(), company.getId()));

	}

	@Test
	public void testFindNonExistingCompany() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		assertThrows(InstanceNotFoundException.class,
				() -> productManagementService.findAllCompanyProducts(user.getId(), NON_EXISTENT_ID));

	}

	@Test
	public void testFindAllCompanyProductsOfAnotherUser() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		User user2 = signUpUser("user2");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class,
				() -> productManagementService.findAllCompanyProducts(user2.getId(), company.getId()));

	}

	@Test
	public void testFindAllCompanyProductsWithNoExistingUserId() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		ProductCategory pCategory = new ProductCategory("Bocadillos");
		productCategoryDao.save(pCategory);
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		productCategoryDao.save(pCategory2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		productManagementService.addProduct(user.getId(), company.getId(), "Bocadillo de tortilla",
				"Tortilla con cebolla", new BigDecimal(3.50), "path", pCategory.getId());

		assertThrows(PermissionException.class,
				() -> productManagementService.findAllCompanyProducts(NON_EXISTENT_ID, company.getId()));

	}

}
