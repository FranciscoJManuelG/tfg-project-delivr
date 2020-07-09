package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessCatalogService;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BusinessCatalogServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private BusinessCatalogService businessCatalogService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CityDao cityDao;

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

	@Test
	public void testFindCompaniesByCompanyCategory() {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);

		Block<Company> expectedBlock = new Block<>(Arrays.asList(company1), false);

		Block<Company> actualBlock = businessCatalogService.findCompanies(category1.getId(), null, null, null, 0, 1);

		assertEquals(expectedBlock, actualBlock);

	}

	/*
	 * @Test public void testFindCompaniesByStreet() throws
	 * InstanceNotFoundException {
	 * 
	 * User user = signUpUser("user"); City city = new City("Lugo");
	 * cityDao.save(city);
	 * 
	 * CompanyCategory category1 = new CompanyCategory("Tradicional");
	 * CompanyCategory category2 = new CompanyCategory("Vegano"); Company company1 =
	 * createCompany(user, "Company1", category1); Company company2 =
	 * createCompany(user, "Company2", category2);
	 * 
	 * companyCategoryDao.save(category1); companyCategoryDao.save(category2);
	 * companyDao.save(company1); companyDao.save(company2);
	 * 
	 * businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(),
	 * company1.getId()); businessService.addCompanyAddress("Jorge 21", "15700",
	 * city.getId(), company2.getId());
	 * 
	 * Block<Company> expectedBlock = new Block<>(Arrays.asList(company1), false);
	 * 
	 * assertEquals(expectedBlock, businessCatalogService.findCompanies(null, null,
	 * "Rosalia 28", null, 0, 1));
	 * 
	 * }
	 */

	@Test
	public void testFindCompaniesByCity() throws InstanceNotFoundException {

		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);

		businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company1.getId());
		businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());

		Block<Company> expectedBlock = new Block<>(Arrays.asList(company1, company2), false);

		Block<Company> actualBlock = businessCatalogService.findCompanies(null, city.getId(), null, null, 0, 1);

		assertEquals(expectedBlock, actualBlock);

	}

}
