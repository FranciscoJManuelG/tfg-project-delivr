package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
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
import es.udc.tfgproject.backend.model.services.AddressService;
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
	private AddressService addressService;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private AddressDao addressDao;

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

		List<Address> addresses = new ArrayList<>();

		Address address1 = addressService.addAddress("Rosalia 18", "15700", city.getId());
		Address address2 = addressService.addAddress("Castelao 35", "15900", city.getId());

		addresses.add(address1);
		addresses.add(address2);

		Company company = companyService.addCompany(user.getId(), "Delivr", 27, true, true, 25, category.getId(),
				addresses);

		Optional<Company> expectedCompany = companyDao.findById(company.getId());

		assertEquals(expectedCompany.get(), company);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAddToNonExistingCompanyCategory() throws InstanceNotFoundException {

		User user = signUpUser("user");

		City city = new City("Lugo");
		cityDao.save(city);

		List<Address> addresses = new ArrayList<>();

		companyService.addCompany(user.getId(), "Delivr", 27, true, true, 25, NON_EXISTENT_COMPANY_CATEGORY_ID,
				addresses);

	}

	@Test
	public void testModifyCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		List<Address> addresses = new ArrayList<>();

		Address address1 = addressService.addAddress("Rosalia 18", "15700", city.getId());
		Address address2 = addressService.addAddress("Castelao 35", "15900", city.getId());

		addresses.add(address1);
		addresses.add(address2);

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId(),
				addresses);

		List<Address> newAddresses = new ArrayList<>();

		Company modifiedCompany = companyService.modifyCompany(user.getId(), company.getId(), "GreenFood", 40, true,
				false, 15, category.getId(), newAddresses);

		assertEquals(modifiedCompany.getName(), "GreenFood");
		assertEquals(modifiedCompany.getCapacity(), 40);
		assertEquals(modifiedCompany.getReserve(), true);
		assertEquals(modifiedCompany.getHomeSale(), false);
		assertEquals(modifiedCompany.getReservePercentage(), 15);
		assertEquals(modifiedCompany.getCompanyCategory().getId(), category.getId());

	}

	@Test
	public void testDeregisterCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		List<Address> addresses = new ArrayList<>();

		Address address1 = addressService.addAddress("Rosalia 18", "15700", city.getId());
		Address address2 = addressService.addAddress("Castelao 35", "15900", city.getId());

		addresses.add(address1);
		addresses.add(address2);

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId(),
				addresses);

		long numberOfCompanies = companyDao.count();
		long numberOfAddresses = addressDao.count();

		companyService.deregister(user.getId(), company.getId());

		/* Comprobamos que hay una fila menos en Company */
		assertEquals(numberOfCompanies - 1, companyDao.count());
		/*
		 * Comprobamos que las dos direcciones relacionadas con la empresa se han
		 * eliminado
		 */
		assertEquals(numberOfAddresses - 2, addressDao.count());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCompany() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		List<Address> newAddresses = new ArrayList<>();

		companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId(), newAddresses);

		companyService.modifyCompany(user.getId(), NON_EXISTENT_COMPANY_ID, "GreenFood", 40, true, false, 15,
				category.getId(), newAddresses);

	}

	@Test(expected = WrongUserException.class)
	public void testWrongUser() throws InstanceNotFoundException, WrongUserException {

		User user = signUpUser("user");
		User wrongUser = signUpUser("wrongUser");

		CompanyCategory category = new CompanyCategory("Vegetariano");
		companyCategoryDao.save(category);

		City city = new City("A Coruña");
		cityDao.save(city);

		List<Address> newAddresses = new ArrayList<>();

		Company company = companyService.addCompany(user.getId(), "GreenFood", 36, true, true, 10, category.getId(),
				newAddresses);

		companyService.modifyCompany(wrongUser.getId(), company.getId(), "GreenFood", 40, true, false, 15,
				category.getId(), newAddresses);

	}

}
