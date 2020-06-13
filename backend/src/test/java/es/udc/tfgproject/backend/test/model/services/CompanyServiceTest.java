package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;

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
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;
import es.udc.tfgproject.backend.model.services.CompanyService;
import es.udc.tfgproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CompanyServiceTest {

	private final long NON_EXISTENT_COMPANY_CATEGORY_ID = new Long(-1);
	private final long NON_EXISTENT_COMPANY_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private CompanyDao companyDao;

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

		Company company = companyService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId());

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

		companyService.addCompany(user.getId(), "Delivr", 27, true, true, 25, NON_EXISTENT_COMPANY_CATEGORY_ID);

	}

	@Test
	public void testModifyCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category1 = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category1);
		CompanyCategory category2 = new CompanyCategory("Vegano");
		companyCategoryDao.save(category2);

		City city = new City("A Coruña");
		cityDao.save(city);

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category1.getId());

		Company modifiedCompany = companyService.modifyCompany(user.getId(), company.getId(), "VegFood", 40, false,
				false, 15, category2.getId());

		assertEquals(modifiedCompany.getName(), "VegFood");
		assertEquals(modifiedCompany.getCapacity(), 40);
		assertEquals(modifiedCompany.getReserve(), false);
		assertEquals(modifiedCompany.getHomeSale(), false);
		assertEquals(modifiedCompany.getReservePercentage(), 15);
		assertEquals(modifiedCompany.getCompanyCategory().getId(), category2.getId());

	}

	/*
	 * TODO : Falta añadir direcciones. En métodos de Address se realiza la
	 * asignación address con company
	 */
	@Test
	public void testDeregisterCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		long numberOfCompanies = companyDao.count();
		// long numberOfAddresses = addressDao.count();

		companyService.deregister(user.getId(), company.getId());

		/* Comprobamos que hay una fila menos en Company */
		assertEquals(numberOfCompanies - 1, companyDao.count());
		/*
		 * Comprobamos que las dos direcciones relacionadas con la empresa se han
		 * eliminado
		 */
		// assertEquals(numberOfAddresses - 2, addressDao.count());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		companyService.modifyCompany(user.getId(), NON_EXISTENT_COMPANY_ID, "GreenFood", 40, true, false, 15,
				category.getId());

	}

	@Test(expected = WrongUserException.class)
	public void testWrongUser() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");
		User wrongUser = signUpUser("wrongUser");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId());

		companyService.modifyCompany(wrongUser.getId(), company.getId(), "GreenFood", 40, true, false, 15,
				category.getId());

	}

	@Test
	public void testFindAllCategories() {

		CompanyCategory category1 = new CompanyCategory("category1");
		CompanyCategory category2 = new CompanyCategory("category2");

		companyCategoryDao.save(category2);
		companyCategoryDao.save(category1);

		assertEquals(Arrays.asList(category1, category2), companyService.findAllCompanyCategories());

	}

}
