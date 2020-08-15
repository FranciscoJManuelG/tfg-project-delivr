package es.udc.tfgproject.backend.model.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.FavouriteAddressDao;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.OrderItem;
import es.udc.tfgproject.backend.model.entities.OrderItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ShoppingCartItemDao shoppingCartItemDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private FavouriteAddressDao favAddressDao;

	@Override
	public ShoppingCart addToShoppingCart(Long userId, Long shoppingCartId, Long productId, Long companyId,
			int quantity) throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		Optional<Product> product = productDao.findById(productId);

		if (!product.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (existingCartItem.isPresent()) {
			existingCartItem.get().incrementQuantity(quantity);
		} else {
			ShoppingCartItem newCartItem = new ShoppingCartItem(product.get(), shoppingCart, quantity);
			shoppingCart.addItem(newCartItem);
			shoppingCartItemDao.save(newCartItem);
		}

		return filterShoppingCart(shoppingCart, companyId);

	}

	@Override
	public ShoppingCart updateShoppingCartItemQuantity(Long userId, Long shoppingCartId, Long productId, Long companyId,
			int quantity) throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (!existingCartItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		existingCartItem.get().setQuantity(quantity);

		return filterShoppingCart(shoppingCart, companyId);

	}

	@Override
	public ShoppingCart removeShoppingCartItem(Long userId, Long shoppingCartId, Long productId, Long companyId)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (!existingCartItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		shoppingCart.removeItem(existingCartItem.get());
		shoppingCartItemDao.delete(existingCartItem.get());

		return filterShoppingCart(shoppingCart, companyId);

	}

	@Override
	@Transactional(readOnly = true)
	public ShoppingCart findShoppingCartProducts(Long userId, Long shoppingCartId, Long companyId)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		return filterShoppingCart(shoppingCart, companyId);

	}

	@Override
	public ShoppingCart changeShoppingCartHomeSale(Long userId, Long shoppingCartId, Long companyId, Boolean homeSale)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		shoppingCart.setHomeSale(homeSale);

		return filterShoppingCart(shoppingCart, companyId);
	}

	@Override
	public Order buy(Long userId, Long shoppingCartId, Long companyId, Boolean homeSale, String street, String cp,
			Long cityId, Boolean saveAsFavAddress)
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		User user = permissionChecker.checkUser(userId);
		Company company = companyDao.findById(companyId).get();
		City city = cityDao.findById(cityId).get();
		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		if (shoppingCart.isEmpty()) {
			throw new EmptyShoppingCartException();
		}

		if (saveAsFavAddress) {
			Address address = new Address(street, cp, city);
			addressDao.save(address);
			FavouriteAddress favAddress = new FavouriteAddress(user);
			favAddress.setUser(user);
			favAddress.setCity(city);
			favAddress.setCp(cp);
			favAddress.setStreet(street);
			favAddressDao.save(favAddress);

		}

		Order order = new Order(shoppingCart.getUser(), company, LocalDateTime.now(), homeSale, street, cp);

		orderDao.save(order);

		shoppingCart = filterShoppingCart(shoppingCart, company.getId());

		for (ShoppingCartItem shoppingCartItem : shoppingCart.getItems()) {

			OrderItem orderItem = new OrderItem(shoppingCartItem.getProduct(), shoppingCartItem.getProduct().getPrice(),
					shoppingCartItem.getQuantity());

			order.addItem(orderItem);
			orderItemDao.save(orderItem);
			shoppingCartItemDao.delete(shoppingCartItem);

		}

		shoppingCart.removeAll();

		return order;

	}

	@Override
	@Transactional(readOnly = true)
	public Order findOrder(Long userId, Long orderId) throws InstanceNotFoundException, PermissionException {
		return permissionChecker.checkOrderExistsAndBelongsToUser(orderId, userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Order> findUserOrders(Long userId, int page, int size) {

		Slice<Order> slice = orderDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());

	}

	@Override
	@Transactional(readOnly = true)
	public Block<Order> findCompanyOrders(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException {
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<Order> slice = orderDao.findByCompanyIdOrderByDateDesc(company.getId(), PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

	private ShoppingCart filterShoppingCart(ShoppingCart cart, Long companyId) {
		Set<ShoppingCartItem> items = new HashSet<>();
		items.addAll(shoppingCartItemDao.findByProductCompanyId(companyId));
		cart.setItems(items);

		return cart;
	}

}
