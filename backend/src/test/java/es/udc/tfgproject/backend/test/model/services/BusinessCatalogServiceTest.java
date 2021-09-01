package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.Province;
import es.udc.tfgproject.backend.model.entities.ProvinceDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessCatalogService;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.UserService;

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

	@Autowired
	private ProvinceDao provinceDao;

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
		return new Company(user, name, 20, true, true, 15, false, companyCategory, LocalTime.of(1, 0),
				LocalTime.of(23, 0), LocalTime.of(14, 0), LocalTime.of(21, 0));
	}

	@Test
	public void testFindCompaniesByCompanyCategory() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);
		Company company3 = createCompany(user, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);
		companyDao.save(company3);

		CompanyAddress ca = businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company2.getId());

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(category2.getId(), null, null, null, 0,
				1);

		List<CompanyAddress> list = new ArrayList<>();
		list.add(ca);

		/*
		 * Hasta que la compañía no tenga asignada una dirección no se podrá obtener
		 * como resultado
		 */
		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindCompaniesByStreet() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
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

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, null, "ROSalia 28", null, 0, 1);
		assertEquals(company1, actualBlock.getItems().get(0).getCompany());

	}

	@Test
	public void testFindCompaniesByCity() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
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

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, city.getId(), null, null, 0, 2);

		assertEquals(company1, actualBlock.getItems().get(0).getCompany());
		assertEquals(company2, actualBlock.getItems().get(1).getCompany());

	}

	@Test
	public void testFindCompaniesByKeywords() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);

		CompanyAddress ca1 = businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company1.getId());
		CompanyAddress ca2 = businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());

		List<CompanyAddress> list = new ArrayList<>();
		list.add(ca1);
		list.add(ca2);

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, null, null, "ComPa", 0, 2);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindCompaniesByAllCriteria() throws InstanceNotFoundException {

		User user = signUpUser("user");
		User user2 = signUpUser("user2");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user2, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);

		CompanyAddress ca1 = businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company1.getId());
		businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());

		List<CompanyAddress> list = new ArrayList<>();
		list.add(ca1);

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(category1.getId(), city.getId(),
				"rosalia", "company", 0, 2);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindAllCompanies() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);

		CompanyAddress ca1 = businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company1.getId());
		CompanyAddress ca2 = businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());

		List<CompanyAddress> list = new ArrayList<>();
		list.add(ca1);
		list.add(ca2);

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, null, null, "", 0, 2);
		assertEquals(list, actualBlock.getItems());

		actualBlock = businessCatalogService.findCompanies(null, null, null, null, 0, 2);
		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindNoCompanies() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
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

		List<CompanyAddress> list = new ArrayList<>();

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, null, null, "non-existent", 0,
				1);

		assertEquals(list, actualBlock.getItems());

	}

	@Test
	public void testFindProductsByPages() throws InstanceNotFoundException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
		cityDao.save(city);

		CompanyCategory category1 = new CompanyCategory("Tradicional");
		CompanyCategory category2 = new CompanyCategory("Vegano");
		Company company1 = createCompany(user, "Company1", category1);
		Company company2 = createCompany(user, "Company2", category2);
		Company company3 = createCompany(user, "Company3", category2);

		companyCategoryDao.save(category1);
		companyCategoryDao.save(category2);
		companyDao.save(company1);
		companyDao.save(company2);
		companyDao.save(company3);

		CompanyAddress ca1 = businessService.addCompanyAddress("Rosalia 28", "15700", city.getId(), company1.getId());
		CompanyAddress ca2 = businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());
		CompanyAddress ca3 = businessService.addCompanyAddress("Jose 5", "15700", city.getId(), company3.getId());

		List<CompanyAddress> list1 = new ArrayList<>();
		list1.add(ca1);
		list1.add(ca2);

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, null, null, null, 0, 2);
		assertEquals(list1, actualBlock.getItems());

		List<CompanyAddress> list2 = new ArrayList<>();
		list2.add(ca3);

		actualBlock = businessCatalogService.findCompanies(null, null, null, null, 1, 2);
		assertEquals(list2, actualBlock.getItems());

		List<CompanyAddress> list3 = new ArrayList<>();

		actualBlock = businessCatalogService.findCompanies(null, null, null, null, 2, 2);
		assertEquals(list3, actualBlock.getItems());

	}

	@Test
	public void testFindNoCompaniesBlocked() throws InstanceNotFoundException, PermissionException {

		User user = signUpUser("user");
		Province province = new Province("Lugo");
		provinceDao.save(province);
		City city = new City("Lugo", province);
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
		CompanyAddress ca = businessService.addCompanyAddress("Jorge 21", "15700", city.getId(), company2.getId());

		businessService.blockCompany(user.getId(), company1.getId());

		List<CompanyAddress> list = new ArrayList<>();
		list.add(ca);

		Block<CompanyAddress> actualBlock = businessCatalogService.findCompanies(null, city.getId(), null, null, 0, 1);
		assertEquals(list, actualBlock.getItems());

		List<CompanyAddress> list2 = new ArrayList<>();

		actualBlock = businessCatalogService.findCompanies(null, null, null, "company1", 0, 1);
		assertEquals(list2, actualBlock.getItems());

	}

}
