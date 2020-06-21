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

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.PostalAddressService;
import es.udc.tfgproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PostalAddressServiceTest {

	private final long NON_EXISTENT_ADDRESS_ID = new Long(-1);
	private final long NON_EXISTENT_CITY_ID = new Long(-1);

	@Autowired
	private PostalAddressService postalAddressService;

	@Autowired
	private UserService userService;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

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
	public void testAddAddress() throws InstanceNotFoundException {
		User user = signUpUser("user");
		City city = new City("A Coruña");
		cityDao.save(city);
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);

		long numberOfAddresses = addressDao.count();

		Company company = new Company(user, "Delivr", 13, true, true, 10, category);
		companyDao.save(company);

		postalAddressService.addAddress("Rosalia 18", "15700", city.getId(), company.getId());

		assertEquals(numberOfAddresses + 1, addressDao.count());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCity() throws InstanceNotFoundException {

		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company = new Company(user, "Delivr", 13, true, true, 10, category);
		companyDao.save(company);

		postalAddressService.addAddress("Rosalia 18", "15700", NON_EXISTENT_CITY_ID, company.getId());
	}

	/*
	 * @Test public void testModifyAddress() throws InstanceNotFoundException { City
	 * city = new City("A Coruña"); cityDao.save(city);
	 * 
	 * Address address = addressService.addAddress("Rosalia 18", "15700",
	 * city.getId());
	 * 
	 * Address modifiedAddress = addressService.modifyAddress(address.getId(),
	 * "Monelos 23", "15870", city.getId());
	 * 
	 * assertEquals(modifiedAddress.getStreet(), "Monelos 23");
	 * assertEquals(modifiedAddress.getCp(), "15870");
	 * assertEquals(modifiedAddress.getCity().getId(), city.getId());
	 * 
	 * }
	 * 
	 * @Test(expected = InstanceNotFoundException.class) public void
	 * testModifyToNonExistingAddress() throws InstanceNotFoundException {
	 * 
	 * City city = new City("A Coruña"); cityDao.save(city);
	 * 
	 * addressService.modifyAddress(NON_EXISTENT_ADDRESS_ID, "Monelos 23", "15870",
	 * city.getId()); }
	 */

	@Test
	public void testDeleteAddress() throws InstanceNotFoundException {
		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company = new Company(user, "Delivr", 13, true, true, 10, category);
		companyDao.save(company);
		City city = new City("A Coruña");
		cityDao.save(city);

		Address address1 = postalAddressService.addAddress("Rosalia 18", "15700", city.getId(), company.getId());
		postalAddressService.addAddress("Magan 23", "13456", city.getId(), company.getId());

		long numberOfAddresses = addressDao.count();
		long numberOfCompanyAddresses = companyAddressDao.count();

		postalAddressService.deleteAddress(address1.getId());

		assertEquals(numberOfAddresses - 1, 1);
		assertEquals(numberOfCompanyAddresses - 1, 1);

	}

	@Test
	public void testFindAddresses() throws InstanceNotFoundException {
		User user = signUpUser("user");
		CompanyCategory category = new CompanyCategory("Tradicional");
		companyCategoryDao.save(category);
		Company company1 = new Company(user, "Delivr", 13, true, true, 10, category);
		companyDao.save(company1);
		Company company2 = new Company(user, "Foodfast", 14, true, true, 15, category);
		companyDao.save(company2);
		City city = new City("A Coruña");
		cityDao.save(city);

		Address address1 = postalAddressService.addAddress("Rosalia 18", "15700", city.getId(), company1.getId());
		Address address2 = postalAddressService.addAddress("Manuel 36", "12760", city.getId(), company1.getId());
		postalAddressService.addAddress("Juan 48", "14900", city.getId(), company2.getId());

		Block<Address> expectedBlock = new Block<>(Arrays.asList(address1, address2), false);
		Block<Address> actual = postalAddressService.findAddresses(company1.getId(), 0, 10);

		assertEquals(expectedBlock, actual);

	}

	@Test
	public void testFindAllCities() {

		City city1 = new City("Barcelona");
		City city2 = new City("Madrid");

		cityDao.save(city1);
		cityDao.save(city2);

		assertEquals(Arrays.asList(city1, city2), postalAddressService.findAllCities());

	}

}
