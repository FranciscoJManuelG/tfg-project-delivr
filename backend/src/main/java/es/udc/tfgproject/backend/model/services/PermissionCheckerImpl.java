package es.udc.tfgproject.backend.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.FavouriteAddressDao;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.User.RoleType;
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

	@Autowired
	private FavouriteAddressDao favouriteAddressDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ShoppingCartDao shoppingCartDao;

	@Autowired
	private OrderDao orderDao;

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
	public Company checkCompanyExistsAndBelongsToUser(Long companyId, Long userId)
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
	public Company checkCompanyExistsAndUserOrAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException {

		Optional<Company> company = companyDao.findById(companyId);
		Optional<User> user = userDao.findById(userId);

		if (!company.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		}

		if (!company.get().getUser().getId().equals(userId) && !user.get().getRole().equals(RoleType.ADMIN)) {
			throw new PermissionException();
		}

		return company.get();

	}

	@Override
	public Company checkCompanyExistsAndOnlyAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException {

		Optional<Company> company = companyDao.findById(companyId);
		Optional<User> user = userDao.findById(userId);

		if (!company.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		}

		if (!user.get().getRole().equals(RoleType.ADMIN)) {
			throw new PermissionException();
		}

		return company.get();

	}

	@Override
	public CompanyAddress checkCompanyAddressExistsAndBelongsToUser(Long addressId, Long userId)
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

	@Override
	public FavouriteAddress checkFavouriteAddressExistsAndBelongsToUser(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException {

		Optional<FavouriteAddress> favouriteAddress = favouriteAddressDao.findByAddressId(addressId);

		if (!favouriteAddress.isPresent()) {
			throw new InstanceNotFoundException("project.entities.favouriteAddress", addressId);
		}

		if (!favouriteAddress.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return favouriteAddress.get();

	}

	@Override
	public Product checkProductExistsAndBelongsToCompany(Long productId, Long companyId)
			throws PermissionException, InstanceNotFoundException {

		Optional<Product> product = productDao.findById(productId);

		if (!product.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		if (!product.get().getCompany().getId().equals(companyId)) {
			throw new PermissionException();
		}

		return product.get();

	}

	@Override
	public Product checkProductExistsAndBelongsToUser(Long productId, Long userId)
			throws PermissionException, InstanceNotFoundException {

		Optional<Product> product = productDao.findById(productId);

		if (!product.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		if (!product.get().getCompany().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return product.get();

	}

	@Override
	public ShoppingCart checkShoppingCartExistsAndBelongsToUser(Long shoppingCartId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<ShoppingCart> shoppingCart = shoppingCartDao.findById(shoppingCartId);

		if (!shoppingCart.isPresent()) {
			throw new InstanceNotFoundException("project.entities.shoppingCart", shoppingCartId);
		}

		if (!shoppingCart.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return shoppingCart.get();

	}

	@Override
	public Order checkOrderExistsAndBelongsToUser(Long orderId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Order> order = orderDao.findById(orderId);

		if (!order.isPresent()) {
			throw new InstanceNotFoundException("project.entities.order", orderId);
		}

		if (!order.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return order.get();

	}

}
