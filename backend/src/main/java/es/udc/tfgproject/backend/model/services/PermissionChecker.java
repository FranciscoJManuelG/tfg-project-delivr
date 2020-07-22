package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface PermissionChecker {

	public void checkUserExists(Long userId) throws InstanceNotFoundException;

	public User checkUser(Long userId) throws InstanceNotFoundException;

	public Company checkCompanyExistsAndBelongsTo(Long companyId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public CompanyAddress checkCompanyAddressExistsAndBelongsTo(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Product checkProductExistsAndBelongsTo(Long productId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

}
