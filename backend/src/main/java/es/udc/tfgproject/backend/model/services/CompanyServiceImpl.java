package es.udc.tfgproject.backend.model.services;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.AddressDao;
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
public class CompanyServiceImpl implements CompanyService {

	private final class SortImplementation implements org.hibernate.annotations.Sort {
		@Override
		public Class<? extends Annotation> annotationType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SortType type() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class comparator() {
			// TODO Auto-generated method stub
			return null;
		}
	}

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

	@Override
	public Company addCompany(Long userId, String name, int capacity, Boolean reserve, Boolean homeSale,
			int reservePercentage, Long companyCategoryId) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		Company company = new Company(user, name, capacity, reserve, homeSale, reservePercentage, companyCategory);
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
