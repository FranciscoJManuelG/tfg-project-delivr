package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.Image;
import es.udc.tfgproject.backend.model.entities.ImageDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.ProductCatalogService;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductCatalogServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductCatalogService productCatalogService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private ImageDao imageDao;

	private User signUpUser(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "email@gmail.com", "123456789");

		try {
			userService.signUpBusinessman(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	private Company createCompany(User user, String name, CompanyCategory companyCategory) {
		return new Company(user, name, 20, true, true, 15, false, companyCategory);
	}

	private Product createProduct(String name, ProductCategory productCategory, Company company, Image image) {
		return new Product(name, "description", new BigDecimal(12), false, productCategory, company, image);
	}

	@Test
	public void testFindProductsByProductCategory() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		Company company1 = createCompany(user, "Company1", category1);

		companyCategoryDao.save(category1);
		companyDao.save(company1);

		Image image1 = new Image("path1");
		Image image2 = new Image("path2");
		ProductCategory pCategory1 = new ProductCategory("Bocadillos");
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		Product product1 = createProduct("Bocadillo de bacon", pCategory1, company1, image1);
		Product product2 = createProduct("Ensalada césar", pCategory2, company1, image2);

		productCategoryDao.save(pCategory1);
		productCategoryDao.save(pCategory2);
		imageDao.save(image1);
		imageDao.save(image2);
		productDao.save(product1);
		productDao.save(product2);

		Block<Product> actualBlock = productCatalogService.findProducts(pCategory2.getId(), null, 0, 1);

		List<Product> list = new ArrayList<>();
		list.add(product2);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindProductsByKeywords() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		Company company1 = createCompany(user, "Company1", category1);

		companyCategoryDao.save(category1);
		companyDao.save(company1);

		Image image1 = new Image("path1");
		Image image2 = new Image("path2");
		ProductCategory pCategory1 = new ProductCategory("Bocadillos");
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		Product product1 = createProduct("Bocadillo de bacon", pCategory1, company1, image1);
		Product product2 = createProduct("Ensalada césar", pCategory2, company1, image2);

		productCategoryDao.save(pCategory1);
		productCategoryDao.save(pCategory2);
		imageDao.save(image1);
		imageDao.save(image2);
		productDao.save(product1);
		productDao.save(product2);

		Block<Product> actualBlock = productCatalogService.findProducts(null, "bocaDiLLo", 0, 1);

		List<Product> list = new ArrayList<>();
		list.add(product1);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindProductsByAllCriteria() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		Company company1 = createCompany(user, "Company1", category1);

		companyCategoryDao.save(category1);
		companyDao.save(company1);

		Image image1 = new Image("path1");
		Image image2 = new Image("path2");
		ProductCategory pCategory1 = new ProductCategory("Bocadillos");
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		Product product1 = createProduct("Bocadillo de bacon", pCategory1, company1, image1);
		Product product2 = createProduct("Ensalada césar", pCategory2, company1, image2);

		productCategoryDao.save(pCategory1);
		productCategoryDao.save(pCategory2);
		imageDao.save(image1);
		imageDao.save(image2);
		productDao.save(product1);
		productDao.save(product2);

		Block<Product> actualBlock = productCatalogService.findProducts(pCategory1.getId(), "BacOn", 0, 1);

		List<Product> list = new ArrayList<>();
		list.add(product1);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindAllProducts() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		Company company1 = createCompany(user, "Company1", category1);

		companyCategoryDao.save(category1);
		companyDao.save(company1);

		Image image1 = new Image("path1");
		Image image2 = new Image("path2");
		ProductCategory pCategory1 = new ProductCategory("Bocadillos");
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		Product product1 = createProduct("Bocadillo de bacon", pCategory1, company1, image1);
		Product product2 = createProduct("Ensalada césar", pCategory2, company1, image2);

		productCategoryDao.save(pCategory1);
		productCategoryDao.save(pCategory2);
		imageDao.save(image1);
		imageDao.save(image2);
		productDao.save(product1);
		productDao.save(product2);

		Block<Product> actualBlock = productCatalogService.findProducts(null, null, 0, 2);

		List<Product> list = new ArrayList<>();
		list.add(product1);
		list.add(product2);

		assertEquals(list, actualBlock.getItems());

		actualBlock = productCatalogService.findProducts(null, "", 0, 2);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindCompanyProductCategories() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		Company company1 = createCompany(user, "Company1", category1);

		companyCategoryDao.save(category1);
		companyDao.save(company1);

		Image image1 = new Image("path1");
		Image image2 = new Image("path2");
		ProductCategory pCategory1 = new ProductCategory("Bocadillos");
		ProductCategory pCategory2 = new ProductCategory("Ensaladas");
		Product product1 = createProduct("Bocadillo de bacon", pCategory1, company1, image1);
		Product product2 = createProduct("Ensalada césar", pCategory2, company1, image2);
		Product product3 = createProduct("Ensalada mixta", pCategory2, company1, image2);

		productCategoryDao.save(pCategory1);
		productCategoryDao.save(pCategory2);
		imageDao.save(image1);
		imageDao.save(image2);
		productDao.save(product1);
		productDao.save(product2);
		productDao.save(product3);

		assertEquals(Arrays.asList(pCategory1, pCategory2),
				productCatalogService.findCompanyProductCategories(company1.getId()));

	}

}
