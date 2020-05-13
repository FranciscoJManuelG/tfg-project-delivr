package es.udc.tfgproject.backend.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Address;
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
			int reservePercentage, Long companyCategoryId, List<Address> addresses) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		Company company = new Company(user, name, capacity, reserve, homeSale, reservePercentage, companyCategory);
		companyDao.save(company);

		for (Address address : addresses) {
			companyAddressDao.save(new CompanyAddress(company, address));
		}

		return company;
	}

	@Override
	public Company modifyCompany(Long userId, Long companyId, String name, int capacity, Boolean reserve,
			Boolean homeSale, int reservePercentage, Long companyCategoryId, List<Address> newAddresses)
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

			/* Asigno las nuevas direcciones añadidas a Company */
			for (Address address : newAddresses) {
				companyAddressDao.save(new CompanyAddress(company, address));
			}

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
			 * Cuando eliminamos una empresa, también debemos eliminar la dirección a la
			 * cual estaba asignada
			 */
			for (CompanyAddress companyAddress : companyAddresses) {
				addressDao.deleteById(companyAddress.getAddress().getId());
			}

			/*
			 * DUDA : puede que al eliminar Address antes y tener ON CASCADE, no es
			 * necesario, o me puede saltar error, al tenerlo también en CompanyAddress
			 * relacionado con Company
			 */
			companyDao.deleteById(companyId);

		} else {
			throw new WrongUserException();
		}

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
