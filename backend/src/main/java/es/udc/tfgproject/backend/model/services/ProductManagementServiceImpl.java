package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.Image;
import es.udc.tfgproject.backend.model.entities.ImageDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductCategoryDao;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class ProductManagementServiceImpl implements ProductManagementService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ImageDao imageDao;

	@Override
	public Product addProduct(Long userId, Long companyId, String name, String description, BigDecimal price,
			String path, Long productCategoryId) throws InstanceNotFoundException, PermissionException {

		Company company = permissionChecker.checkCompanyExistsAndBelongsTo(companyId, userId);

		ProductCategory productCategory = checkProductCategory(productCategoryId);

		Image image = new Image(path);
		imageDao.save(image);

		Product product = new Product(name, description, price, false, productCategory, company, image);
		productDao.save(product);

		return product;
	}

	@Override
	public Product editProduct(Long userId, Long companyId, Long productId, String name, String description,
			BigDecimal price, String newPath, Long productCategoryId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsTo(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsTo(productId, companyId);

		ProductCategory productCategory = checkProductCategory(productCategoryId);

		/* Antes de añadir la nueva foto, hay que eliminar del sistema la anterior */

		Image imageToRemove = imageDao.findByPath(product.getImage().getPath()).get();
		imageDao.delete(imageToRemove);
		Image image = new Image(newPath);
		imageDao.save(image);

		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setProductCategory(productCategory);
		product.setImage(image);

		return product;
	}

	@Override
	public void removeProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {
		permissionChecker.checkCompanyExistsAndBelongsTo(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsTo(productId, companyId);

		/*
		 * Cuando eliminamos un producto, también eliminamos la imagen asociada a éste
		 */
		Image imageToRemove = imageDao.findByPath(product.getImage().getPath()).get();
		imageDao.delete(imageToRemove);
		productDao.delete(product);
	}

	@Override
	public Product blockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsTo(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsTo(productId, companyId);

		product.setBlock(true);

		return product;
	}

	@Override
	public Product unlockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {
		permissionChecker.checkCompanyExistsAndBelongsTo(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsTo(productId, companyId);

		product.setBlock(false);

		return product;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findAllProductCategories() {
		Iterable<ProductCategory> categories = productCategoryDao.findAll();
		List<ProductCategory> categoriesList = new ArrayList<>();

		categories.forEach(category -> categoriesList.add(category));

		return categoriesList;
	}

	@Override
	public List<Product> findAllCompanyProducts(Long companyId) {

		return productDao.findByCompanyId(companyId);
	}

	/*
	 * Método privado que comprueba que la categoría está registrada en el sistema
	 */
	private ProductCategory checkProductCategory(Long productCategoryId) throws InstanceNotFoundException {
		Optional<ProductCategory> productCategoryOpt = productCategoryDao.findById(productCategoryId);
		ProductCategory productCategory = null;

		if (!productCategoryOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.productCategory", productCategoryId);
		} else
			productCategory = productCategoryOpt.get();

		return productCategory;

	}

}
