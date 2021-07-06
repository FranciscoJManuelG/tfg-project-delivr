package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import es.udc.tfgproject.backend.model.entities.DiscountTicket.DiscountType;
import es.udc.tfgproject.backend.model.entities.DiscountTicketDao;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalDao;
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
import es.udc.tfgproject.backend.model.entities.User;
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
	private GoalDao goalDao;

	@Autowired
	private EmailUtil emailUtil;

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

		User user = permissionChecker.checkUser(userId);
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

		// Obtengo los goals de la compañia
		List<Goal> goals = goalDao.findByCompanyId(companyId);

		for (Goal goal : goals) {
			switch (goal.getGoalType().getGoalName()) {
			case Constantes.NUMERO_PEDIDOS:
				checkNumberOfOrdersGoalAndSendEmail(user, company, goal);
				break;

			default:
				break;
			}
		}

		return order;
	}

	private void checkNumberOfOrdersGoalAndSendEmail(User user, Company company, Goal goal) {
		// Obtener el número de pedidos que ha realizado el usuario para una compañia
		int numberOfOrders = orderDao.findByUserIdAndCompanyId(user.getId(), company.getId()).size();

		if (goal.getGoalQuantity() == numberOfOrders) {

			String code = generateRandomCode();

			DiscountTicket newDiscountTicket = new DiscountTicket();
			newDiscountTicket.setCode(code);
			newDiscountTicket.setExpirationDate(LocalDateTime.now().plusDays(Long.valueOf(Constantes.EXPIRATION_DAYS)));
			newDiscountTicket.setUsed(false);
			newDiscountTicket.setUser(user);
			newDiscountTicket.setGoal(goal);
			if (!new BigDecimal(0).equals(goal.getDiscountCash())) {
				newDiscountTicket.setDiscountType(DiscountType.CASH);
			} else {
				newDiscountTicket.setDiscountType(DiscountType.PERCENTAGE);
			}

			discountTicketDao.save(newDiscountTicket);

			StringBuilder texto = new StringBuilder();
			texto = texto.append("Hola ").append(user.getUserName()).append(",").append(System.lineSeparator())
					.append(System.lineSeparator()).append("Has conseguido un ticket descuento para usar en ")
					.append(company.getName());

			if (!new BigDecimal(0).equals(goal.getDiscountCash())) {
				texto.append(". Con este ticket, tendrás ").append(goal.getDiscountCash()).append("€ de descuento.")
						.append(System.lineSeparator()).append(System.lineSeparator());
			} else {
				texto.append(". Con este ticket, tendrás un ").append(goal.getDiscountPercentage())
						.append("% de descuento.").append(System.lineSeparator()).append(System.lineSeparator());
			}

			String textoString = texto.append("Puedes usar el código antes del ")
					.append(newDiscountTicket.getExpirationDate()
							.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
					.append(System.lineSeparator()).append(System.lineSeparator()).append("Código descuento: ")
					.append(newDiscountTicket.getCode()).toString();

			emailUtil.sendMail(user.getEmail(), "¡Nuevo código descuento!", textoString);
		}

	}

	private String generateRandomCode() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String code = random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		Optional<DiscountTicket> ticket = discountTicketDao.findByCode(code);

		// Si ya existe un ticket con ese código, generamos uno nuevo
		if (ticket.isPresent()) {
			code = generateRandomCode();
		}

		return code;
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
		DiscountTicket discountTicket = permissionChecker.checkDiscountTicketExistsAndBelongsToUser(code, userId);
		if (discountTicket.getExpirationDate().isBefore(LocalDateTime.now())) {
			throw new DiscountTicketHasExpiredException();
		}
		if (discountTicket.getUsed()) {
			throw new DiscountTicketUsedException();
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

		default:
		}

		return discountedPrice;
	}

	private BigDecimal calculatePriceFromPercentage(int discountPercentage, BigDecimal totalPrice) {
		BigDecimal valorToDiscount = totalPrice.multiply(new BigDecimal(discountPercentage));
		BigDecimal discountedPrice = totalPrice.subtract(valorToDiscount);

		return discountedPrice;
	}

	@Override
	@Transactional(readOnly = true)
	public Block<DiscountTicket> findUserDiscountTicketsNotUsed(Long userId, int page, int size)
			throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);

		Slice<DiscountTicket> slice = discountTicketDao.findByUserIdWhereUsedIsFalseOrderByExpirationDateDesc(userId,
				PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

	@Override
	public Goal addGoal(Long userId, Long companyId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException {
		Goal goal = new Goal();
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		GoalType goalType = permissionChecker.checkGoalType(goalTypeId);

		switch (discountType) {
		case CASH:
			goal = new Goal(discountCash, 0, company, goalType, goalQuantity);
			break;
		case PERCENTAGE:
			goal = new Goal(new BigDecimal(0), discountPercentage, company, goalType, goalQuantity);
			break;

		default:
		}

		goalDao.save(goal);
		return goal;
	}

	@Override
	public Goal modifyGoal(Long userId, Long companyId, Long goalId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Goal goal = permissionChecker.checkGoalAndBelongsToCompany(goalId, companyId);
		GoalType goalType = permissionChecker.checkGoalType(goalTypeId);

		switch (discountType) {
		case CASH:
			goal.setDiscountCash(discountCash);
			goal.setDiscountPercentage(0);
			goal.setGoalQuantity(goalQuantity);
			goal.setGoalType(goalType);
			break;

		case PERCENTAGE:
			goal.setDiscountPercentage(discountPercentage);
			goal.setDiscountCash(new BigDecimal(0));
			goal.setGoalQuantity(goalQuantity);
			goal.setGoalType(goalType);
			break;

		default:
			break;
		}

		return goal;
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Goal> findCompanyGoals(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException {
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<Goal> slice = goalDao.findByCompanyIdOrderByIdDesc(company.getId(), PageRequest.of(page, size));
		return new Block<>(slice.getContent(), slice.hasNext());
	}

	@Override
	public void removeGoal(Long userId, Long companyId, Long goalId)
			throws InstanceNotFoundException, PermissionException {
		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Goal goal = permissionChecker.checkGoalExistsAndBelongsToCompany(goalId, companyId);

		goalDao.delete(goal);

	}

}
