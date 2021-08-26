package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import es.udc.tfgproject.backend.model.entities.WeeklyBalance;
import es.udc.tfgproject.backend.model.entities.WeeklyBalanceDao;
import es.udc.tfgproject.backend.model.exceptions.CompanyDoesNotAllowHomeSaleException;
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

	@Autowired
	private WeeklyBalanceDao weeklyBalanceDao;

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
			throws InstanceNotFoundException, PermissionException, CompanyDoesNotAllowHomeSaleException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		Company company = permissionChecker.checkCompany(companyId);

		if (company.getHomeSale()) {
			shoppingCart.setHomeSale(homeSale);
		} else {
			throw new CompanyDoesNotAllowHomeSaleException();
		}

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

		shoppingCart = filterShoppingCart(shoppingCart, company.getId());

		// Comprobamos si el codigo del ticket descuento introducido es el correcto o
		// no, si se ha introducido algún código
		if (!StringUtils.isEmpty(codeDiscount)) {
			DiscountTicket discountTicket = checkDiscountTicket(userId, companyId, codeDiscount);
			BigDecimal totalPrice = getDiscountedPrice(discountTicket, shoppingCart);
			setWeeklyBalance(company.getUser(), totalPrice);
			order = new Order(shoppingCart.getUser(), company, LocalDateTime.now(), homeSale, street, cp,
					discountTicket, totalPrice);
			discountTicket.setUsed(true);
			discountTicket.setOrder(order);

		} else {
			order = new Order(shoppingCart.getUser(), company, LocalDateTime.now(), homeSale, street, cp,
					shoppingCart.getTotalPrice());
			setWeeklyBalance(company.getUser(), shoppingCart.getTotalPrice());
		}

		orderDao.save(order);

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
			if (goal.getActive()) {
				switch (goal.getGoalType().getGoalName()) {
				case Constantes.NUMERO_PEDIDOS:
					checkNumberOfOrdersGoalAndSendEmail(user, company, goal);
					break;

				default:
					break;
				}
			}
		}

		return order;
	}

	private void setWeeklyBalance(User user, BigDecimal totalPrice) {
		Integer year = LocalDate.now().getYear();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		Integer weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

		BigDecimal balanceToCompany = totalPrice.multiply(Constantes.RELIVRY_PERCENTAGE);

		System.out.println("WeekNumber = " + weekNumber + ", year = " + year + ", userId = " + user.getId());

		Optional<WeeklyBalance> weeklyBalanceOptional = weeklyBalanceDao.findByWeekNumberAndYearAndUserId(weekNumber,
				year, user.getId());

		if (!weeklyBalanceOptional.isPresent()) {
			WeeklyBalance balance = new WeeklyBalance(balanceToCompany, weekNumber, year, user);
			weeklyBalanceDao.save(balance);
		} else {
			WeeklyBalance weeklyBalance = weeklyBalanceOptional.get();
			BigDecimal newBalance = balanceToCompany.add(weeklyBalance.getBalance());
			weeklyBalance.setBalance(newBalance);
		}

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
			if (!Integer.valueOf(0).equals(goal.getDiscountPercentage())) {
				newDiscountTicket.setDiscountType(DiscountType.PERCENTAGE);
			} else {
				newDiscountTicket.setDiscountType(DiscountType.CASH);
			}
			discountTicketDao.save(newDiscountTicket);

			StringBuilder texto = new StringBuilder();
			texto = texto.append("Hola ").append(user.getUserName()).append(",").append(System.lineSeparator())
					.append(System.lineSeparator()).append("Has conseguido un ticket descuento para usar en ")
					.append(company.getName());

			if (!Integer.valueOf(0).equals(goal.getDiscountPercentage())) {
				texto.append(". Con este ticket, tendrás un ").append(goal.getDiscountPercentage())
						.append("% de descuento.").append(System.lineSeparator()).append(System.lineSeparator());
			} else {
				texto.append(". Con este ticket, tendrás ").append(goal.getDiscountCash()).append("€ de descuento.")
						.append(System.lineSeparator()).append(System.lineSeparator());
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
		return permissionChecker.checkOrderExistsAndBelongsToUserOrCompany(orderId, userId);
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

	// Devuelve el carrito con los productos que pertenezcan a una compañía
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

		return discountedPrice.setScale(Constantes.SCALE, RoundingMode.FLOOR);
	}

	private BigDecimal calculatePriceFromPercentage(int discountPercentage, BigDecimal totalPrice) {
		double percentage = (double) discountPercentage / 100;
		BigDecimal valorToDiscount = totalPrice.multiply(new BigDecimal(percentage));
		BigDecimal discountedPrice = totalPrice.subtract(valorToDiscount);

		return discountedPrice.setScale(Constantes.SCALE, RoundingMode.FLOOR);
	}

	@Override
	@Transactional(readOnly = true)
	public Block<DiscountTicket> findUserDiscountTicketsNotUsed(Long userId, int page, int size)
			throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);

		Slice<DiscountTicket> slice = discountTicketDao
				.findByUserIdWhereUsedIsFalseAndExpirationDateOrderByExpirationDateDesc(userId, LocalDateTime.now(),
						PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

}
