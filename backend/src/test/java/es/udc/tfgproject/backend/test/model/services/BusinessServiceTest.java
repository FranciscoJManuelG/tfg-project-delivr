package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BusinessServiceTest {

	private final long NON_EXISTENT_COMPANY_CATEGORY_ID = new Long(-1);
	private final long NON_EXISTENT_COMPANY_ID = new Long(-1);
	private final long NON_EXISTENT_CITY_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAddressDao companyAddressDao;

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
	public void testAddCompany() throws InstanceNotFoundException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId());

		Company expectedCompany = companyDao.findById(company.getId()).get();

		assertEquals(expectedCompany, company);
		assertEquals(expectedCompany.getName(), "Delivr");
		assertEquals(expectedCompany.getCapacity(), 27);
		assertEquals(expectedCompany.getReserve(), true);
		assertEquals(expectedCompany.getHomeSale(), true);
		assertEquals(expectedCompany.getReservePercentage(), 25);
		assertEquals(expectedCompany.getCompanyCategory().getId(), category.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAddToNonExistingCompanyCategory() throws InstanceNotFoundException {

		User user = signUpUser("user");

		City city = new City("Lugo");
		cityDao.save(city);

		businessService.addCompany(user.getId(), "Delivr", 27, true, true, 25, NON_EXISTENT_COMPANY_CATEGORY_ID);

	}

	@Test
	public void testModifyCompany() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		CompanyCategory category2 = new CompanyCategory("Vegano");
		companyCategoryDao.save(category2);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Company modifiedCompany = businessService.modifyCompany(user.getId(), company.getId(), "VegFood", 40, false,
				false, 15, category2.getId());

		assertEquals(modifiedCompany.getName(), "VegFood");
		assertEquals(modifiedCompany.getCapacity(), 40);
		assertEquals(modifiedCompany.getReserve(), false);
		assertEquals(modifiedCompany.getHomeSale(), false);
		assertEquals(modifiedCompany.getReservePercentage(), 15);
		assertEquals(modifiedCompany.getCompanyCategory().getId(), category2.getId());

	}

	@Test
	public void testBlockAndUnlockCompany() throws InstanceNotFoundException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		Company blockedCompany = businessService.blockCompany(user.getId(), company.getId());

		assertTrue(blockedCompany.getBlock());
		assertEquals(blockedCompany.getId(), company.getId());

		Company unlockedCompany = businessService.unlockCompany(user.getId(), company.getId());

		assertFalse(unlockedCompany.getBlock());
		assertEquals(unlockedCompany.getId(), company.getId());

	}

	@Test
	public void testDeregisterCompany() throws InstanceNotFoundException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		businessService.addCompanyAddress("Rosalia 18", "15700", city.getId(), company.getId());
		businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company.getId());

		long numberOfCompanies = companyDao.count();
		long numberOfCompanyAddresses = companyAddressDao.count();

		businessService.deregister(user.getId(), company.getId());

		/* Comprobamos que hay una fila menos en Company */
		assertEquals(numberOfCompanies - 1, companyDao.count());
		/*
		 * Comprobamos que las dos direcciones relacionadas con la empresa se han
		 * eliminado
		 */
		assertEquals(numberOfCompanyAddresses - 2, companyAddressDao.count());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCompany() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("Lugo");
		cityDao.save(city);

		businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		businessService.modifyCompany(user.getId(), NON_EXISTENT_COMPANY_ID, "GreenFood", 40, true, false, 15,
				category.getId());

	}

	@Test(expected = PermissionException.class)
	public void testWrongUser() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		User wrongUser = signUpUser("wrongUser");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("Lugo");
		cityDao.save(city);

		Company company = businessService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		businessService.modifyCompany(wrongUser.getId(), company.getId(), "GreenFood", 40, true, false, 15,
				category.getId());

	}

	@Test
	public void testFindAllCategories() {

		CompanyCategory category1 = new CompanyCategory("category1");
		CompanyCategory category2 = new CompanyCategory("category2");

		companyCategoryDao.save(category2);
		companyCategoryDao.save(category1);

		assertEquals(Arrays.asList(category1, category2), businessService.findAllCompanyCategories());

	}

	@Test
	public void testAddCompanyAddress() throws InstanceNotFoundException {
		User user = signUpUser("user");
		City city = new City("Lugo");
		cityDao.save(city);
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);

		long numberOfAddresses = companyAddressDao.count();

		Company company = new Company(user, "Delivr", 13, true, true, 10, false, category);
		companyDao.save(company);

		businessService.addCompanyAddress("Rosalia 18", "15700", city.getId(), company.getId());
		businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company.getId());

		long count = companyAddressDao.count();

		assertEquals(numberOfAddresses + 2, count);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCity() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company = new Company(user, "Delivr", 13, true, true, 10, false, category);
		companyDao.save(company);

		businessService.addCompanyAddress("Rosalia 18", "15700", NON_EXISTENT_CITY_ID, company.getId());
	}

	@Test
	public void testDeleteCompanyAddress() throws InstanceNotFoundException, PermissionException {
		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company = new Company(user, "Delivr", 13, true, true, 10, false, category);
		companyDao.save(company);
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyAddress address1 = businessService.addCompanyAddress("Rosalia 18", "15700", city.getId(),
				company.getId());
		businessService.addCompanyAddress("Magan 23", "13456", city.getId(), company.getId());

		long numberOfAddresses = companyAddressDao.count();
		long numberOfCompanyAddresses = companyAddressDao.count();

		businessService.deleteCompanyAddress(user.getId(), address1.getAddressId());

		assertEquals(numberOfAddresses - 1, 1);
		assertEquals(numberOfCompanyAddresses - 1, 1);

	}

	@Test
	public void testFindCompanyAddresses() throws InstanceNotFoundException {
		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company1 = new Company(user, "Delivr", 13, true, true, 10, false, category);
		companyDao.save(company1);
		Company company2 = new Company(user, "Delivr", 13, true, true, 10, false, category);
		companyDao.save(company2);
		City city = new City("Lugo");
		cityDao.save(city);

		CompanyAddress address1 = businessService.addCompanyAddress("Rosalia 18", "15700", city.getId(),
				company1.getId());
		CompanyAddress address2 = businessService.addCompanyAddress("Manuel 36", "12760", city.getId(),
				company1.getId());
		businessService.addCompanyAddress("Juan 48", "14900", city.getId(), company2.getId());

		Block<CompanyAddress> expectedBlock = new Block<>(Arrays.asList(address1, address2), false);
		Block<CompanyAddress> actual = businessService.findCompanyAddresses(company1.getId(), 0, 10);

		assertEquals(expectedBlock, actual);

	}

	@Test
	public void testFindAllCities() {

		City city1 = new City("Barcelona");
		City city2 = new City("Madrid");

		cityDao.save(city1);
		cityDao.save(city2);

		assertEquals(Arrays.asList(city1, city2), businessService.findAllCities());

	}

}
