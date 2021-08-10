package es.udc.tfgproject.backend.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.DiscountTicketDao;
import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.EventEvaluationDao;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.FavouriteAddressDao;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalDao;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.GoalTypeDao;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuDao;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.ReserveDao;
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

	@Autowired
	private DiscountTicketDao discountTicketDao;

	@Autowired
	private GoalDao goalDao;

	@Autowired
	private GoalTypeDao goalTypeDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private ReserveDao reserveDao;

	@Autowired
	private EventEvaluationDao eventEvaluationDao;

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
	public Order checkOrderExistsAndBelongsToUserOrCompany(Long orderId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Order> order = orderDao.findById(orderId);

		if (!order.isPresent()) {
			throw new InstanceNotFoundException("project.entities.order", orderId);
		}

		if (!order.get().getUser().getId().equals(userId) && !order.get().getCompany().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return order.get();

	}

	@Override
	public DiscountTicket checkDiscountTicketExistsAndBelongsToUser(String code, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<DiscountTicket> discountTicket = discountTicketDao.findByCode(code);

		if (!discountTicket.isPresent()) {
			throw new InstanceNotFoundException("project.entities.discountTicket", code);
		}

		if (!discountTicket.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return discountTicket.get();

	}

	@Override
	public Goal checkGoalAndBelongsToCompany(Long goalId, Long companyId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Goal> goal = goalDao.findById(goalId);

		if (!goal.isPresent()) {
			throw new InstanceNotFoundException("project.entities.goal", goalId);
		}

		if (!goal.get().getCompany().getId().equals(companyId)) {
			throw new PermissionException();
		}

		return goal.get();
	}

	@Override
	public Goal checkGoalExistsAndBelongsToCompany(Long goalId, Long companyId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Goal> goal = goalDao.findById(goalId);

		if (!goal.isPresent()) {
			throw new InstanceNotFoundException("project.entities.goal", goalId);
		}

		if (!goal.get().getCompany().getId().equals(companyId)) {
			throw new PermissionException();
		}

		return goal.get();
	}

	@Override
	public GoalType checkGoalType(Long goalTypeId) throws InstanceNotFoundException, PermissionException {
		Optional<GoalType> goalType = goalTypeDao.findById(goalTypeId);

		if (!goalType.isPresent()) {
			throw new InstanceNotFoundException("project.entities.goalType", goalTypeId);
		}

		return goalType.get();
	}

	@Override
	public Company checkCompany(Long companyId) throws InstanceNotFoundException {
		Optional<Company> company = companyDao.findById(companyId);

		if (!company.isPresent()) {
			throw new InstanceNotFoundException("project.entities.comapny", companyId);
		}

		return company.get();
	}

	@Override
	public Menu checkMenuExistsAndBelongsToUser(Long menuId, Long userId)
			throws PermissionException, InstanceNotFoundException {
		Optional<Menu> menu = menuDao.findById(menuId);

		if (!menu.isPresent()) {
			throw new InstanceNotFoundException("project.entities.menu", menuId);
		}

		if (!menu.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return menu.get();
	}

	@Override
	public Reserve checkReserveExistsAndBelongsToUserOrCompany(Long reserveId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Reserve> reserve = reserveDao.findById(reserveId);

		if (!reserve.isPresent()) {
			throw new InstanceNotFoundException("project.entities.reserve", reserveId);
		}

		if (!reserve.get().getUser().getId().equals(userId) && !reserve.get().getCompany().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return reserve.get();

	}

	@Override
	public Reserve checkReserveExistsAndBelongsToUser(Long reserveId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<Reserve> reserve = reserveDao.findById(reserveId);

		if (!reserve.isPresent()) {
			throw new InstanceNotFoundException("project.entities.reserve", reserveId);
		}

		if (!reserve.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return reserve.get();

	}

	@Override
	public EventEvaluation checkEventEvaluationBelongsToReserve(Long reserveId) throws InstanceNotFoundException {
		Optional<EventEvaluation> eventEvaluationOptional = eventEvaluationDao.findByReserveId(reserveId);

		if (!eventEvaluationOptional.isPresent()) {
			throw new InstanceNotFoundException("project.entities.eventEvaluation", null);
		}

		return eventEvaluationOptional.get();
	}

	@Override
	public EventEvaluation checkEventEvaluationExistsAndBelongsToUser(Long eventEvaluationId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Optional<EventEvaluation> eventEvaluation = eventEvaluationDao.findById(eventEvaluationId);

		if (!eventEvaluation.isPresent()) {
			throw new InstanceNotFoundException("project.entities.eventEvaluation", eventEvaluationId);
		}

		if (!eventEvaluation.get().getReserve().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return eventEvaluation.get();
	}

}
