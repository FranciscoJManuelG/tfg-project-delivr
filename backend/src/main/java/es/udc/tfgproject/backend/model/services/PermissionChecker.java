package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface PermissionChecker {

	public void checkUserExists(Long userId) throws InstanceNotFoundException;

	public User checkUser(Long userId) throws InstanceNotFoundException;

	public Company checkCompanyExistsAndBelongsToUser(Long companyId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Company checkCompanyExistsAndUserOrAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public Company checkCompanyExistsAndOnlyAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public CompanyAddress checkCompanyAddressExistsAndBelongsToUser(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Product checkProductExistsAndBelongsToCompany(Long productId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public Product checkProductExistsAndBelongsToUser(Long productId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public ShoppingCart checkShoppingCartExistsAndBelongsToUser(Long shoppingCartId, Long userId)
			throws PermissionException, InstanceNotFoundException;

}
