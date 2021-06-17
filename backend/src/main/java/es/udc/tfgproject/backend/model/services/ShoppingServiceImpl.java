package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.DiscountTicketDao;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.OrderItem;
import es.udc.tfgproject.backend.model.entities.OrderItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketHasExpiredException;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketUsedException;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectDiscountCodeException;
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
	private DiscountTicketDao discountTicketDao;

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
			String codeDiscount) throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException,
			IncorrectDiscountCodeException, DiscountTicketHasExpiredException, DiscountTicketUsedException {

		Company company = companyDao.findById(companyId).get();
		Order order = new Order();

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		if (shoppingCart.isEmpty()) {
			throw new EmptyShoppingCartException();
		}

		// Comprobamos si el codigo del ticket descuento introducido es el correcto o
		// no, si se ha introducido algún código
		if (!StringUtils.isEmpty(codeDiscount)) {
			DiscountTicket discountTicket = checkDiscountTicket(userId, companyId, codeDiscount);
			BigDecimal totalPrice = getDiscountedPrice(discountTicket, shoppingCart);
			order = new Order(shoppingCart.getUser(), company, LocalDateTime.now(), homeSale, street, cp,
					discountTicket, totalPrice);
			discountTicket.setUsed(true);
			discountTicket.setOrder(order);

		} else {
			order = new Order(shoppingCart.getUser(), company, LocalDateTime.now(), homeSale, street, cp,
					shoppingCart.getTotalPrice());
		}

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

	private DiscountTicket checkDiscountTicket(Long userId, Long companyId, String code)
			throws InstanceNotFoundException, PermissionException, IncorrectDiscountCodeException,
			DiscountTicketHasExpiredException, DiscountTicketUsedException {
		System.out.println("code" + code + "userId" + userId);
		DiscountTicket discountTicket = permissionChecker.checkDiscountTicketExistsAndBelongsToUser(code, userId);
		if (discountTicket.getUsed()) {
			throw new DiscountTicketUsedException();
		}
		if (discountTicket.getExpirationDate().isAfter(LocalDateTime.now())) {
			throw new DiscountTicketHasExpiredException();
		}
		if (!discountTicket.getCode().equals(code)) {
			throw new IncorrectDiscountCodeException();
		}

		return discountTicket;

	}

	@Override
	public BigDecimal redeemDiscountTicket(Long userId, Long companyId, Long shoppingCartId, String code)
			throws InstanceNotFoundException, PermissionException, IncorrectDiscountCodeException,
			EmptyShoppingCartException, DiscountTicketHasExpiredException, DiscountTicketUsedException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		DiscountTicket discountTicket = checkDiscountTicket(userId, companyId, code);

		if (shoppingCart.isEmpty()) {
			throw new EmptyShoppingCartException();
		}

		return getDiscountedPrice(discountTicket, shoppingCart);

	}

	private BigDecimal getDiscountedPrice(DiscountTicket discountTicket, ShoppingCart shoppingCart) {
		BigDecimal discountedPrice = shoppingCart.getTotalPrice();

		switch (discountTicket.getDiscountType()) {
		case CASH:
			discountedPrice = shoppingCart.getTotalPrice().subtract(discountTicket.getGoal().getDiscountCash());
			break;

		case PERCENTAGE:
			discountedPrice = shoppingCart.getTotalPrice().subtract(calculatePriceFromPercentage(
					discountTicket.getGoal().getDiscountPercentage(), shoppingCart.getTotalPrice()));
			break;
		}

		return discountedPrice;
	}

	private BigDecimal calculatePriceFromPercentage(int discountPercentage, BigDecimal totalPrice) {
		BigDecimal valorToDiscount = totalPrice.multiply(new BigDecimal(discountPercentage));
		BigDecimal discountedPrice = totalPrice.subtract(valorToDiscount);

		return discountedPrice;
	}

	@Override
	public Block<DiscountTicket> findDiscountTickets(Long userId, Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Goal addGoal(Long companyId, BigDecimal discountCash, int discountPercentage, GoalType goalType, int goal) {
		// TODO Auto-generated method stub
		return null;
	}

}
