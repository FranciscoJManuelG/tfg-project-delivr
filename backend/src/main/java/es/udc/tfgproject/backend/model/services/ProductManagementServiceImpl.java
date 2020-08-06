package es.udc.tfgproject.backend.model.services;

import java.io.File;
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

		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		ProductCategory productCategory = checkProductCategory(productCategoryId);

		if (path != null) {
			Image image = new Image();

			image.setPath(File.separator + "img" + File.separator + path);
			imageDao.save(image);
			Product product = new Product(name, description, price, false, productCategory, company, image);
			productDao.save(product);
			return product;
		} else {
			Product product = new Product(name, description, price, false, productCategory, company, null);
			productDao.save(product);
			return product;
		}
	}

	@Override
	public Product editProduct(Long userId, Long companyId, Long productId, String name, String description,
			BigDecimal price, String newPath, Long productCategoryId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsToCompany(productId, companyId);

		ProductCategory productCategory = checkProductCategory(productCategoryId);

		/* Antes de añadir la nueva foto, hay que eliminar del sistema la anterior */

		if (product.getImage() != null && newPath != null) {
			Image imageToRemove = imageDao.findByPath(product.getImage().getPath()).get();
			// deleteFile(imageToRemove.getPath());
			imageDao.delete(imageToRemove);
		}

		if (newPath != null) {
			Image image = new Image(File.separator + "img" + File.separator + newPath);
			imageDao.save(image);
			product.setImage(image);
		}

		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setProductCategory(productCategory);

		return product;
	}

	@Override
	public void removeProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {
		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsToCompany(productId, companyId);

		/*
		 * Cuando eliminamos un producto, también eliminamos la imagen asociada a éste
		 */
		if (product.getImage() != null) {
			Image imageToRemove = imageDao.findByPath(product.getImage().getPath()).get();
			productDao.delete(product);
			// deleteFile(imageToRemove.getPath());
			imageDao.delete(imageToRemove);
		} else {
			productDao.delete(product);
		}
	}

	@Override
	public Product blockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsToCompany(productId, companyId);

		product.setBlock(true);

		return product;
	}

	@Override
	public Product unlockProduct(Long userId, Long companyId, Long productId)
			throws InstanceNotFoundException, PermissionException {
		/*
		 * TODO: revisar si esto es mejor hacerlo solo con el
		 * checkProductExistsAndBelongsToUserproductId, userId)
		 */
		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Product product = permissionChecker.checkProductExistsAndBelongsToCompany(productId, companyId);

		product.setBlock(false);

		return product;
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProduct(Long userId, Long productId) throws InstanceNotFoundException, PermissionException {

		return permissionChecker.checkProductExistsAndBelongsToUser(productId, userId);
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
	public List<Product> findAllCompanyProducts(Long userId, Long companyId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

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

	/*
	 * private void deleteFile(String filename) { try { Path path =
	 * Paths.get(File.separator + "img" + File.separator + filename);
	 * 
	 * Files.delete(path); } catch (Exception e) { throw new
	 * RuntimeException("Internal error"); } }
	 */

}
