package es.udc.tfgproject.backend.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.UserDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAddressDao companyAddressDao;

	@Override
	public void checkUserExists(Long userId) throws InstanceNotFoundException {

		if (!userDao.existsById(userId)) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

	}

	@Override
	public User checkUser(Long userId) throws InstanceNotFoundException {

		Optional<User> user = userDao.findById(userId);

		if (!user.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		return user.get();

	}

	@Override
	public Company checkCompanyExistsAndBelongsTo(Long companyId, Long userId)
			throws PermissionException, InstanceNotFoundException {

		Optional<Company> company = companyDao.findById(companyId);

		if (!company.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		}

		if (!company.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return company.get();

	}

	@Override
	public CompanyAddress checkCompanyAddressExistsAndBelongsTo(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException {

		Optional<CompanyAddress> companyAddress = companyAddressDao.findByAddressId(addressId);

		if (!companyAddress.isPresent()) {
			throw new InstanceNotFoundException("project.entities.companyAddress", addressId);
		}

		if (!companyAddress.get().getCompany().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return companyAddress.get();

	}

}
