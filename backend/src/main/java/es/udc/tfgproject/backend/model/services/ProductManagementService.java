package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.util.List;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface ProductManagementService {

	Product addProduct(Long userId, Long companyId, String name, String description, BigDecimal price, String path,
			Long productCategoryId) throws InstanceNotFoundException, PermissionException;

	Product editProduct(Long userId, Long companyId, Long productId, String name, String description, BigDecimal price,
			String newPath, Long productCategoryId) throws InstanceNotFoundException, PermissionException;

	void removeProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException;

	Product blockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException;

	Product unlockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException;

	Product findProduct(Long userId, Long productId) throws InstanceNotFoundException, PermissionException;

	List<ProductCategory> findAllProductCategories();

	ProductCategory addProductCategory(Long userId, String name) throws InstanceNotFoundException, PermissionException;

	ProductCategory modifyProductCategory(Long userId, Long productCategoryId, String name)
			throws InstanceNotFoundException, PermissionException;

	List<Product> findAllCompanyProducts(Long userId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

}
