package es.udc.tfgproject.backend.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;

@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAddressDao companyAddressDao;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private CityDao cityDao;

	@Override
	public Company addCompany(Long userId, String name, int capacity, Boolean reserve, Boolean homeSale,
			int reservePercentage, Long companyCategoryId) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		Company company = new Company(user, name, capacity, reserve, homeSale, reservePercentage, false,
				companyCategory);
		companyDao.save(company);

		return company;
	}

	@Override
	public Company modifyCompany(Long userId, Long companyId, String name, int capacity, Boolean reserve,
			Boolean homeSale, int reservePercentage, Long companyCategoryId)
			throws InstanceNotFoundException, WrongUserException {

		User user = permissionChecker.checkUser(userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		Company company = checkCompany(companyId);

		/*
		 * Comprobamos que el usuario que va a modificar los datos de la empresa es el
		 * mismo que está asignado a esa empresa
		 */
		if (company.getUser().getId().equals(user.getId())) {
			company.setName(name);
			company.setCapacity(capacity);
			company.setReserve(reserve);
			company.setHomeSale(homeSale);
			company.setReservePercentage(reservePercentage);
			company.setCompanyCategory(companyCategory);

			return company;
		} else {
			throw new WrongUserException();
		}

	}

	@Override
	public void deregister(Long userId, Long companyId) throws InstanceNotFoundException, WrongUserException {

		User user = permissionChecker.checkUser(userId);

		Company company = checkCompany(companyId);

		List<CompanyAddress> companyAddresses = companyAddressDao.findByCompanyId(companyId);

		/*
		 * Comprobamos que el usuario que va a dar de baja la empresa es el mismo que
		 * está asignado a esa empresa
		 */
		if (company.getUser().getId().equals(user.getId())) {
			/*
			 * Cuando eliminamos una empresa, también debemos eliminar las direcciones a las
			 * cuales estaba asignada
			 */
			for (CompanyAddress companyAddress : companyAddresses) {
				addressDao.deleteById(companyAddress.getAddress().getId());
				companyAddressDao.deleteById(companyAddress.getId());
			}

			companyDao.deleteById(companyId);

		} else {
			throw new WrongUserException();
		}

	}

	@Override
	public List<CompanyCategory> findAllCompanyCategories() {

		Iterable<CompanyCategory> categories = companyCategoryDao.findAll();
		List<CompanyCategory> categoriesList = new ArrayList<>();

		categories.forEach(category -> categoriesList.add(category));

		return categoriesList;

	}

	@Override
	public Address addAddress(String street, String cp, Long cityId, Long companyId) throws InstanceNotFoundException {

		City city = checkCity(cityId);
		Company company = checkCompany(companyId);

		Address address = addressDao.save(new Address(street, cp, city));

		companyAddressDao.save(new CompanyAddress(company, address));

		return address;
	}

	@Override
	public void deleteAddress(Long addressId) throws InstanceNotFoundException {

		companyAddressDao.delete(companyAddressDao.findByAddressId(addressId).get());

		addressDao.deleteById(addressId);
	}

	@Override
	public Block<CompanyAddress> findAddresses(Long companyId, int page, int size) {
    // TODO Revisar implementación !! ¿debe devolver un Block? En ese caso, paginar ...
		List<CompanyAddress> list = companyAddressDao.findByCompanyId(companyId);

		return new Block<>(list, false);

	}

	@Override
	public List<City> findAllCities() {

		Iterable<City> cities = cityDao.findAll();
		List<City> citiesList = new ArrayList<>();

		cities.forEach(city -> citiesList.add(city));

		return citiesList;

	}

	/*
	 * Método privado que comprueba que la ciudad está registrada en el sistema
	 */
	private City checkCity(Long cityId) throws InstanceNotFoundException {
		Optional<City> cityOpt = cityDao.findById(cityId);
		City city = null;

		if (!cityOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);
		} else
			city = cityOpt.get();

		return city;
	}

	/*
	 * Método privado que comprueba que la categoría está registrada en el sistema
	 */
	private CompanyCategory checkCompanyCategory(Long companyCategoryId) throws InstanceNotFoundException {
		Optional<CompanyCategory> companyCategoryOpt = companyCategoryDao.findById(companyCategoryId);
		CompanyCategory companyCategory = null;

		if (!companyCategoryOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.companyCategory", companyCategoryId);
		} else
			companyCategory = companyCategoryOpt.get();

		return companyCategory;

	}

	/*
	 * Método privado que comprueba que la empresa está registrada en el sistema
	 */
	private Company checkCompany(Long companyId) throws InstanceNotFoundException {
		Optional<Company> companyOpt = companyDao.findById(companyId);
		Company company = null;

		if (!companyOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		} else
			company = companyOpt.get();

		return company;
	}

}
